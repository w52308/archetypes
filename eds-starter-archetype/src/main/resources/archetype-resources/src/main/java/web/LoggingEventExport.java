#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.web;

import java.io.OutputStream;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ${package}.entity.LoggingEvent;
import ${package}.entity.LoggingEventException;
import ${package}.entity.LoggingEventProperty;
import ${package}.repository.LoggingEventRepository;

@Controller
public class LoggingEventExport {

	@Autowired
	private LoggingEventRepository loggingEventRepository;

	@Autowired
	private MessageSource messageSource;

	@Transactional(readOnly = true)
	@RequestMapping(value = "/loggingEventExport.xls", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void loggingEventExport(HttpServletResponse response, Locale locale,
			@RequestParam(required = false) final String level) throws Exception {

		response.setContentType("application/vnd.ms-excel");
		response.addHeader("Content-disposition", "attachment;filename=logs.xls");

		Workbook workbook = new HSSFWorkbook();

		CreationHelper createHelper = workbook.getCreationHelper();

		Font font = workbook.createFont();
		Font titleFont = workbook.createFont();

		font.setColor(IndexedColors.BLACK.getIndex());
		font.setFontName("Arial");
		font.setFontHeightInPoints((short) 10);
		font.setBoldweight(Font.BOLDWEIGHT_NORMAL);

		titleFont.setColor(IndexedColors.BLACK.getIndex());
		titleFont.setFontName("Arial");
		titleFont.setFontHeightInPoints((short) 10);
		titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);

		CellStyle normalStyle = workbook.createCellStyle();
		normalStyle.setFont(font);

		CellStyle titleStyle = workbook.createCellStyle();
		titleStyle.setFont(titleFont);

		Sheet sheet = workbook.createSheet("Logs");
		HSSFPatriarch patr = ((HSSFSheet) sheet).createDrawingPatriarch();

		Row row = sheet.createRow(0);
		createCell(row, 0, "ID", titleStyle);
		createCell(row, 1, messageSource.getMessage("logevents_timestamp", null, locale), titleStyle);
		createCell(row, 2, messageSource.getMessage("user", null, locale), titleStyle);
		createCell(row, 3, "IP", titleStyle);
		createCell(row, 4, messageSource.getMessage("logevents_message", null, locale), titleStyle);
		createCell(row, 5, messageSource.getMessage("logevents_level", null, locale), titleStyle);
		createCell(row, 6, messageSource.getMessage("logevents_callerclass", null, locale), titleStyle);
		createCell(row, 7, messageSource.getMessage("logevents_callerline", null, locale), titleStyle);

		List<LoggingEvent> events;
		if (StringUtils.hasText(level)) {
			events = loggingEventRepository.findByLevelString(level);
		} else {
			events = loggingEventRepository.findAll();
		}

		int rowNo = 1;
		for (LoggingEvent event : events) {
			row = sheet.createRow(rowNo);
			rowNo++;

			Cell cell = row.createCell(0);
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell.setCellValue(event.getEventId());
			cell.setCellStyle(normalStyle);

			cell = row.createCell(1);
			cell.setCellType(Cell.CELL_TYPE_STRING);
			cell.setCellValue(ISODateTimeFormat.dateTime().print(event.getTimestmp().longValue()));
			cell.setCellStyle(normalStyle);

			String userName = null;
			String ip = null;

			Set<LoggingEventProperty> properties = event.getLoggingEventProperty();
			for (LoggingEventProperty prop : properties) {
				if ("userName".equals(prop.getId().getMappedKey())) {
					userName = prop.getMappedValue();
				} else if ("ip".equals(prop.getId().getMappedKey())) {
					ip = prop.getMappedValue();
				}
			}

			if (userName != null) {
				cell = row.createCell(2);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(userName);
				cell.setCellStyle(normalStyle);
			}

			if (ip != null) {
				cell = row.createCell(3);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(ip);
				cell.setCellStyle(normalStyle);
			}

			cell = row.createCell(4);
			cell.setCellType(Cell.CELL_TYPE_STRING);
			cell.setCellValue(event.getFormattedMessage());
			cell.setCellStyle(normalStyle);

			if (!event.getLoggingEventException().isEmpty()) {

				StringBuilder sb = new StringBuilder();
				for (LoggingEventException loggingEventException : event.getLoggingEventException()) {
					sb.append(loggingEventException.getTraceLine());
					sb.append("${symbol_escape}n");
				}

				Comment comment = patr.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 13, 33));
				comment.setString(createHelper.createRichTextString(sb.toString()));
				comment.setAuthor(null);
				cell.setCellComment(comment);
			}

			cell = row.createCell(5);
			cell.setCellType(Cell.CELL_TYPE_STRING);
			cell.setCellValue(event.getLevelString());
			cell.setCellStyle(normalStyle);

			cell = row.createCell(6);
			cell.setCellType(Cell.CELL_TYPE_STRING);
			cell.setCellValue(event.getCallerClass());
			cell.setCellStyle(normalStyle);

			cell = row.createCell(7);
			cell.setCellType(Cell.CELL_TYPE_STRING);
			cell.setCellValue(event.getCallerLine());
			cell.setCellStyle(normalStyle);
		}

		sheet.autoSizeColumn(0);
		sheet.autoSizeColumn(1);
		sheet.autoSizeColumn(2);
		sheet.autoSizeColumn(3);
		sheet.setColumnWidth(4, 10000);
		sheet.autoSizeColumn(5);
		sheet.autoSizeColumn(6);
		sheet.autoSizeColumn(7);

		try (OutputStream out = response.getOutputStream()) {
		workbook.write(out);
		}
	}

	private static void createCell(Row row, int column, String value, CellStyle style) {
		Cell cell = row.createCell(column);
		cell.setCellValue(value);
		cell.setCellStyle(style);
	}

}

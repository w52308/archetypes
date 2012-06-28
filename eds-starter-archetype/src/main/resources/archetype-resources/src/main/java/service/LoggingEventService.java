#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.service;

import static ch.ralscha.extdirectspring.annotation.ExtDirectMethodType.STORE_READ;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ${package}.entity.LoggingEvent;
import ${package}.entity.QLoggingEvent;
import ${package}.repository.LoggingEventRepository;
import ${package}.util.Util;
import ch.ralscha.extdirectspring.annotation.ExtDirectMethod;
import ch.ralscha.extdirectspring.bean.ExtDirectStoreReadRequest;
import ch.ralscha.extdirectspring.bean.ExtDirectStoreResponse;
import ch.ralscha.extdirectspring.filter.StringFilter;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

@Service
public class LoggingEventService {

	private static final ImmutableMap<String, String> mapGuiColumn2DbField = new ImmutableMap.Builder<String, String>()
			.put("dateTime", "timestmp").put("message", "formattedMessage").put("level", "levelString").build();

	@Autowired
	private LoggingEventRepository loggingEventRepository;

	@ExtDirectMethod(STORE_READ)
	@Transactional(readOnly = true)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ExtDirectStoreResponse<LoggingEventDto> load(final ExtDirectStoreReadRequest request) {

		Pageable pageRequest = Util.createPageRequest(request, mapGuiColumn2DbField);

		Page<LoggingEvent> page;
		if (request.getFilters().isEmpty()) {
			page = loggingEventRepository.findAll(pageRequest);
		} else {
			StringFilter levelFilter = (StringFilter) request.getFilters().iterator().next();
			String levelValue = levelFilter.getValue();
			page = loggingEventRepository.findAll(QLoggingEvent.loggingEvent.levelString.eq(levelValue), pageRequest);
		}

		List<LoggingEventDto> loggingEventList = Lists.newArrayList();
		for (LoggingEvent event : page.getContent()) {
			loggingEventList.add(new LoggingEventDto(event));
		}

		return new ExtDirectStoreResponse<LoggingEventDto>((int) page.getTotalElements(), loggingEventList);
	}

	@ExtDirectMethod
	@Transactional
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void deleteAll(final String level) {
		if (StringUtils.hasText(level)) {
			loggingEventRepository.delete(loggingEventRepository.findByLevelString(level));
		} else {
			loggingEventRepository.delete(loggingEventRepository.findAll());
		}
	}

	@ExtDirectMethod
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void addTestData() {
		Logger logger = LoggerFactory.getLogger(getClass());

		logger.debug("a simple debug log entry");
		logger.info("this is a info log entry");
		logger.warn("a warning", new IllegalArgumentException());
		logger.error("a serious error", new NullPointerException());
	}

	@ExtDirectMethod
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void changeLogLevel(final String levelString) {
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		ch.qos.logback.classic.Logger logger = lc.getLogger("${package}");
		Level level = Level.toLevel(levelString);
		if (level != null) {
			logger.setLevel(level);
		}
	}

	@ExtDirectMethod
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String getCurrentLevel() {
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		ch.qos.logback.classic.Logger logger = lc.getLogger("${package}");
		return logger != null && logger.getLevel() != null ? logger.getLevel().toString() : "ERROR";
	}

}

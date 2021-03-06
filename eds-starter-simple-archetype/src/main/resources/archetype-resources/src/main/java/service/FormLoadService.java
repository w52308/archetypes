#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.service;

import static ch.ralscha.extdirectspring.annotation.ExtDirectMethodType.FORM_LOAD;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

import org.springframework.stereotype.Service;

import ch.ralscha.extdirectspring.annotation.ExtDirectMethod;
import ${package}.FormBean;

@Service
public class FormLoadService {

	@ExtDirectMethod
	public String getRemark() {
		return "Used Heap: " + ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed() + " bytes";
	}

	@ExtDirectMethod(FORM_LOAD)
	public FormBean getFormData() {
		OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();

		FormBean bean = new FormBean();
		bean.setAvailableProcessors(osBean.getAvailableProcessors());
		bean.setOsName(osBean.getName());
		bean.setOsVersion(osBean.getVersion());
		return bean;
	}

}

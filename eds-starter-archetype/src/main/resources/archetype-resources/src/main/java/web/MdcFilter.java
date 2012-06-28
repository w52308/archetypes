#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class MdcFilter implements Filter {

	@Override
	public void init(final FilterConfig config) {
		//no action
	}

	@Override
	public void destroy() {
		//no action
	}

	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
			throws IOException, ServletException {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			MDC.put("userName", authentication.getName());
		}

		MDC.put("ip", request.getRemoteAddr());
		chain.doFilter(request, response);
	}
}
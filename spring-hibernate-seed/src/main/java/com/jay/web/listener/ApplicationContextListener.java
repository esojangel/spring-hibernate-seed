package com.jay.web.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.jay.util.ContextUtils;

/**
 * 
 * @author Jay Zhang
 *
 */
public class ApplicationContextListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent event) {
		final ServletContext servletContext = event.getServletContext();
		final WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
		ContextUtils.setApplicationContext(context);
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		ContextUtils.setApplicationContext(null);
		
	}
}


package com.jay.util;

import javax.servlet.ServletRequest;

import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 * 获取spring-servlet.xml中定义的bean
 * @author Jay Zhang
 *
 */
public class ReqContextUtils {
	
	public static ApplicationContext getApplicationContext(ServletRequest request) {
		return RequestContextUtils.getWebApplicationContext(request);
	}
	
    @SuppressWarnings("unchecked")
	public static <T extends Object> T getBean(ServletRequest request, String beanId) {
        return (T) getApplicationContext(request).getBean(beanId);
    }

	public static <T extends Object> T getBean(ServletRequest request, Class<T> clazz) {
        return (T) getApplicationContext(request).getBean(clazz);
    }
	
}

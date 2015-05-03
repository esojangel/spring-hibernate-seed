
package com.jay.util;

import javax.servlet.ServletRequest;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 * 获取spring-context.xml中定义的bean
 * @author Jay Zhang
 *
 */
public class AppContextUtils {
	
    private static final Object monitor = new Object();
    protected static ApplicationContext applicationContext;
    
    public static ApplicationContext getApplicationContext() {
    	if(applicationContext == null){
    		synchronized (monitor) {
            	if(applicationContext == null){
            		applicationContext = ContextLoader.getCurrentWebApplicationContext();
                }
            }
    	}
        return applicationContext;
    }

    @SuppressWarnings("unchecked")
	public static <T extends Object> T getBean(String beanId) {
        return (T) getApplicationContext().getBean(beanId);
    }

	public static <T extends Object> T getBean(Class<T> clazz) {
        return (T) getApplicationContext().getBean( clazz);
    }
	
	public static ApplicationContext getApplicationContext(ServletRequest request) {
		return RequestContextUtils.getWebApplicationContext(request);
	}
}

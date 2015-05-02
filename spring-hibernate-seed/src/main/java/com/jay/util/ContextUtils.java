
package com.jay.util;

import org.springframework.context.ApplicationContext;

/**
 * 
 * @author Jay Zhang
 *
 */
public class ContextUtils {
	
    private static final Object monitor = new Object();
    protected static ApplicationContext applicationContext;

    public static void setApplicationContext(ApplicationContext applicationContext) {
        ContextUtils.applicationContext = applicationContext;
        synchronized (monitor) {
            monitor.notifyAll();
        }
    }

    public static ApplicationContext getApplicationContext() {
    	if(ContextUtils.applicationContext == null){
    		try {
                synchronized (monitor) {
                    while (ContextUtils.applicationContext == null) {
                        monitor.wait();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    	}
        return ContextUtils.applicationContext;
    }

    @SuppressWarnings("unchecked")
	public static <T extends Object> T getBean(String beanId) {
        return (T) getApplicationContext().getBean(beanId);
    }

	public static <T extends Object> T getBean(Class<T> clazz) {
        return (T) getApplicationContext().getBean( clazz);
    }

    public static ApplicationContext getApplicationContextOrNull() {
        return ContextUtils.applicationContext;
    }
}

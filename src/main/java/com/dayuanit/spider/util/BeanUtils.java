package com.dayuanit.spider.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BeanUtils {
	
	private static final ApplicationContext context;
	
	static {
		context = new ClassPathXmlApplicationContext("spring/spring-config.xml");
	}
	
	public static <T> T getBean(Class<T> calzz) {
		return context.getBean(calzz);
	}

}

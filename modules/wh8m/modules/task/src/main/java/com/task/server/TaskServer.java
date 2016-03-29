package com.task.server;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TaskServer {

	public static ApplicationContext context;

	public static void main(final String[] args) {
		context = new ClassPathXmlApplicationContext("spring/*.xml");
	}

}

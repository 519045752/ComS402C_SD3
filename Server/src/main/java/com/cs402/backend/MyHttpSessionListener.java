package com.cs402.backend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@Component
public class MyHttpSessionListener implements HttpSessionListener {
	
	private static final Logger logger = LoggerFactory.getLogger(HttpSessionListener.class);

	public Integer userCounter = 0;
	
	@Override
	public synchronized void sessionCreated(HttpSessionEvent httpSessionEvent) {
		logger.info("One new user is online.");
		userCounter++;
		httpSessionEvent.getSession().getServletContext().setAttribute("count", userCounter);
	}
	
	@Override
	public synchronized void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
		logger.info("User is offline.");
		userCounter--;
		httpSessionEvent.getSession().getServletContext().setAttribute("count", userCounter);
	}
}

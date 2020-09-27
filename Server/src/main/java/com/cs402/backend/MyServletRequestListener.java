package com.cs402.backend;

import com.cs402.backend.utility.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.ServletComponentScan;

import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSessionListener;


public class MyServletRequestListener implements ServletRequestListener {
	private static final Logger logger = LoggerFactory.getLogger(ServletRequestListener.class);

	// HttpServletRequest request;
	//
	// String ip = request.getRemoteAddr();
	// String path = request.getRequestURI();
	// String parma = request.getQueryString();
	// String localAddr = request.getLocalAddr();
	// String localName = request.getLocalName();
	private static final String logFormat = "[%s] Requests: %s";
	public void requestInitialized(ServletRequestEvent arg0) {
		ServletRequest req= arg0.getServletRequest();
		HttpServletRequest request = (HttpServletRequest) req;
		String ip = Utility.getIpAddress(request);
		String path = request.getRequestURI();
		String parma = request.getQueryString();
		String localAddr = request.getLocalAddr();
		String localName = request.getLocalName();
		String str;
		if(parma==null){
			str = path;
		}else{
			str = path+"?"+parma;
		}
		logger.info(String.format(logFormat, ip,str));
	}

	public void requestDestroyed(ServletRequestEvent arg0) {
		//Do Nothing.
	}
	

}
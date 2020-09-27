package com.cs402.backend.test;

import com.cs402.backend.model.RandomQuote;
import com.cs402.backend.utility.Utility;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class Test {
	
	@GetMapping("/test")
	public Object test(HttpServletRequest request){
		String ip = Utility.getIpAddress(request);
		String path = request.getRequestURI();
		String parma = request.getQueryString();
		String localAddr = request.getLocalAddr();
		String localName = request.getLocalName();
		String session = request.getSession().toString();
		String str = "Welcome to the backend server of "+localName+"@"+localAddr+"!";
		str += "\n Server Time: "+Utility.getServerTime();
		str += "\n Your IP: "+ ip;
		str += "\n Your Session: "+ session;
		str += "\n\n" +Utility.getRandomQuote();
		return str;
	}
}

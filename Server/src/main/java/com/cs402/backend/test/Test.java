package com.cs402.backend.test;

import com.cs402.backend.model.RandomQuote;
import com.cs402.backend.utility.Utility;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@Api(value = "test APIs", tags = {"some utility request for testing"})
public class Test {
	
	@GetMapping("/test")
	@ApiOperation(value = "Hi visitor!", notes = "simply ping the sever")
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
	
	@GetMapping("/apis")
	public void apisForward(HttpServletResponse response) throws Exception {
		response.sendRedirect("/swagger-ui.html#/");
	}
}

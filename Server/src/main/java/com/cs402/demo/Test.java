package com.cs402.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Test {
	@GetMapping("/test")
	public Object test(){
		return "This is a test message!";
	}
}

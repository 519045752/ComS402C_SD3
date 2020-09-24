package com.cs402.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicLong;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserRepository userRepository;
	
	private static final Logger log = LoggerFactory.getLogger(UserController.class);
	private static final String template = "Hello,%s!";
	private final AtomicLong counter = new AtomicLong();
	
	
	
	@GetMapping("/all")
	public @ResponseBody Iterable<User> getAllUser() {
		return userRepository.findAll();
	}
	
	@GetMapping("/login")
	public User greeting(@RequestParam String name, @RequestParam String password) {
		//TODO
		// String.format(template, user)
		return new User();
	}
	
	@RequestMapping(path="/add")
	public @ResponseBody String addNewUser (@RequestParam String username, @RequestParam String password,@RequestParam String category) {
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		user.setCategory(category);
		userRepository.save(user);
		return "["+utility.getServerTime()+ "] : User [" + username +"] added!";
	}
}

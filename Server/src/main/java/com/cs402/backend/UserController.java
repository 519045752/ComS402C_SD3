package com.cs402.backend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
	public @ResponseBody
	Iterable<User> getAllUser() {
		return userRepository.findAll();
	}
	
	@ResponseBody
	@GetMapping("/login")
	public User login(@RequestParam String username, @RequestParam String password) {
		List<User> user = userRepository.login(username, password);
		log.debug(user.toString());
		if (user != null) {
			return user.get(0);
		}
		return null;
	}
	
	//test only, please use /register
	@RequestMapping(path = "/add")
	@ResponseBody
	public String addNewUser(@RequestParam String username, @RequestParam String password, @RequestParam String category) {
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		user.setCategory(category);
		userRepository.save(user);
		return "[" + Utility.getServerTime() + "] : User [" + username + "] added!";
	}
	
	@ResponseBody
	@GetMapping(value = "/userlist")
	public List<String> getUserlist() {
		return userRepository.getUserlist(3);
	}
	
	@ResponseBody
	@GetMapping("/getUserByID")
	public User findUserById(@RequestParam Long uid) {
		List<User> user = userRepository.findUserById(uid);
		log.debug("[/getUserbyID] " + user.toString());
		if (user != null) {
			return user.get(0);
		}
		return null;
	}
	
	//true: not used
	@ResponseBody
	@GetMapping("/checkUsernameNotUsed")
	public Boolean checkUsernameNotUsed(@RequestParam String username) {
		List<User> list = userRepository.checkIfExist(username);
		if (list.isEmpty()) {
			return true;
		}
		else {
			return false;
		}
	}
	
	@RequestMapping(path = "/register")
	@ResponseBody
	public String register(@RequestParam String username, @RequestParam String password, @RequestParam String category) {
		if (!checkUsernameNotUsed(username)) {
			return "[" + Utility.getServerTime() + "] " + "Username is already used!";
		}
		else if (!Utility.validateCategory(category)) {
			return "[" + Utility.getServerTime() + "] " + "Wrong User Category!";
		}
		else {
			User user = new User();
			user.setUsername(username);
			user.setPassword(password);
			user.setCategory(category);
			userRepository.save(user);
			return "[" + Utility.getServerTime() + "] : User [" + username + "] added!";
		}
		
	}
}

package com.cs402.backend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
	private UserRepository userRepository;
	private static final Logger log = LoggerFactory.getLogger(MainController.class);
	
	// @GetMapping("/user/{userId}")
	// public RespondJsonBase test(@PathVariable Long userId) {
	// 	return RespondJson.out(RespondCodeEnum.SUCCESS,uc.findUserById(userId));
	// }
	
}

package com.cs402.backend.user;

import com.cs402.backend.respond.RespondCodeEnum;
import com.cs402.backend.respond.RespondJson;
import com.cs402.backend.utility.Utility;
import com.cs402.backend.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	private static final Logger log = LoggerFactory.getLogger(UserController.class);
	private static final String greeting = "Hello,%s!";
	private final AtomicLong counter = new AtomicLong();
	
	@GetMapping("/all")
	public RespondJson<Iterable<User>> getAllUser() {
		return RespondJson.out(RespondCodeEnum.SUCCESS, userRepository.findAll());
	}
	
	@GetMapping("/{uid}")
	public Object test(@PathVariable Long uid) {
		try {
			User user = findUserByIdUtil(uid).get(0);
			String username = user.getUsername();
			RespondJson<User> ret = RespondJson.out(RespondCodeEnum.SUCCESS, user);
			ret.setMsg(String.format(greeting, username));
			return ret;
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
			return RespondJson.out(RespondCodeEnum.FAIL_USER_NOT_FOUND);
		} catch (Exception e) {
			e.printStackTrace();
			RespondCodeEnum res = RespondCodeEnum.FAIL;
			res.setMgs(e.toString());
			return RespondJson.out(res);
		}
	}
	
	
	@GetMapping("/getUserByID")
	public Object findUserById(@RequestParam Long uid) {
		List<User> list = findUserByIdUtil(uid);
		if (list.isEmpty()) {
			return RespondJson.out(RespondCodeEnum.FAIL_USER_NOT_FOUND);
		}
		else {
			return RespondJson.out(RespondCodeEnum.SUCCESS, list.get(0));
		}
	}
	
	public List<User> findUserByIdUtil(Long uid) {
		List<User> list = userRepository.findUserById(uid);
		log.debug("[/getUserbyID] " + list.toString());
		return list;
	}
	
	
	@GetMapping("/login")
	public Object login(@RequestParam String username, @RequestParam String password) {
		List<User> list = userRepository.login(username, password);
		log.debug(list.toString());
		if (list.isEmpty()) {
			return RespondJson.out(RespondCodeEnum.FAIL_LOGIN_MISMATCH);
		}
		else {
			User userinfo = list.get(0);
			return RespondJson.out(RespondCodeEnum.SUCCESS, userinfo);
		}
	}
	
	@GetMapping("/loginByUid")
	public Object loginByUid(@RequestParam Long uid, @RequestParam String password) {
		List<User> list = userRepository.loginByUid(uid, password);
		log.debug(list.toString());
		if (list.isEmpty()) {
			return RespondJson.out(RespondCodeEnum.FAIL_LOGIN_MISMATCH);
		}
		else {
			User userinfo = list.get(0);
			return RespondJson.out(RespondCodeEnum.SUCCESS, userinfo);
		}
	}
	
	//test only, please use /register
	@RequestMapping(path = "/add")
	public String addNewUser(@RequestParam String username, @RequestParam String password, @RequestParam String category) {
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		user.setCategory(category);
		userRepository.save(user);
		return "[" + Utility.getServerTime() + "] : user [" + username + "] added!";
	}
	
	@GetMapping(value = "/userlist")
	public Object getUserlist() {
		return RespondJson.out(RespondCodeEnum.SUCCESS, userRepository.getUserlist());
	}
	
	
	public Boolean checkUsernameNotUsedUtil(String username) {
		List<User> list = userRepository.checkIfExist(username);
		return list.isEmpty();
	}
	
	//true: not used
	@GetMapping("/checkUsernameNotUsed")
	public Object checkUsernameNotUsed(@RequestParam String username) {
		String res = checkUsernameNotUsedUtil(username).toString();
		return RespondJson.out(RespondCodeEnum.SUCCESS, res);
	}
	
	@RequestMapping(path = "/register")
	public Object register(@RequestParam String username, @RequestParam String password, @RequestParam String category) {
		if (!checkUsernameNotUsedUtil(username)) {
			return RespondJson.out(RespondCodeEnum.FAIL_USERNAME_USED);
		}
		else if (!Utility.validateCategory(category)) {
			return RespondJson.out(RespondCodeEnum.FAIL_WRONG_INFO);
		}
		else {
			User user = new User();
			user.setUsername(username);
			user.setPassword(password);
			user.setCategory(category);
			userRepository.save(user);
			return RespondJson.out(RespondCodeEnum.SUCCESS, user);
		}
	}
	
	@RequestMapping(path = "/setEmail")
	public Object setEmail(@RequestParam String username, @RequestParam String password, @RequestParam String email) {
		if (checkUsernameNotUsedUtil(username)) {
			return RespondJson.out(RespondCodeEnum.FAIL_USER_NOT_FOUND);
		}
		else if (userRepository.login(username, password).isEmpty()) {
			return RespondJson.out(RespondCodeEnum.FAIL_LOGIN_MISMATCH);
		}
		else if (!Utility.validateEmail(email)) {
			return RespondJson.out(RespondCodeEnum.FAIL_WRONG_INFO);
		}
		else {
			Long uid = findUidByUsernameUtil(username);
			User user = userRepository.findById(uid).get();
			user.setEmail(email);
			userRepository.save(user);
			return RespondJson.out(RespondCodeEnum.SUCCESS, user);
		}
	}
	
	@RequestMapping(path = "/setPassword")
	public Object setPassword(@RequestParam String username, @RequestParam String password, @RequestParam String newPassword) {
		if(password.equals(newPassword)){
			return RespondJson.out(RespondCodeEnum.WTF);
		}
		else if (checkUsernameNotUsedUtil(username)) {
			return RespondJson.out(RespondCodeEnum.FAIL_USER_NOT_FOUND);
		}
		else if (userRepository.login(username, password).isEmpty()) {
			return RespondJson.out(RespondCodeEnum.FAIL_LOGIN_MISMATCH);
		}
		else if (!Utility.validatePassword(newPassword)) {
			return RespondJson.out(RespondCodeEnum.FAIL_WRONG_INFO);
		}
		else {
			Long uid = findUidByUsernameUtil(username);
			User user = userRepository.findById(uid).get();
			user.setPassword(newPassword);
			userRepository.save(user);
			return RespondJson.out(RespondCodeEnum.SUCCESS, user);
		}
	}
	
	@RequestMapping(path = "/setUsername")
	public Object setUsername(@RequestParam String username, @RequestParam String password, @RequestParam String newUsername) {
		if(username.equals(newUsername)){
			return RespondJson.out(RespondCodeEnum.WTF);
		}
		else if (checkUsernameNotUsedUtil(username)) {
			return RespondJson.out(RespondCodeEnum.FAIL_USER_NOT_FOUND);
		}
		else if (userRepository.login(username, password).isEmpty()) {
			return RespondJson.out(RespondCodeEnum.FAIL_LOGIN_MISMATCH);
		}
		else if (!Utility.validateUsername(newUsername)) {
			return RespondJson.out(RespondCodeEnum.FAIL_WRONG_INFO);
		}else if(!checkUsernameNotUsedUtil(newUsername)){
			return RespondJson.out(RespondCodeEnum.FAIL_USERNAME_USED);
		}
		else {
			Long uid = findUidByUsernameUtil(username);
			User user = userRepository.findById(uid).get();
			user.setUsername(newUsername);
			userRepository.save(user);
			return RespondJson.out(RespondCodeEnum.SUCCESS, user);
		}
	}
	
	@RequestMapping(path = "/setPhone")
	public Object setPhone(@RequestParam String username, @RequestParam String password, @RequestParam String phone) {
		if (checkUsernameNotUsedUtil(username)) {
			return RespondJson.out(RespondCodeEnum.FAIL_USER_NOT_FOUND);
		}
		else if (userRepository.login(username, password).isEmpty()) {
			return RespondJson.out(RespondCodeEnum.FAIL_LOGIN_MISMATCH);
		}
		else if (!Utility.validatePhone(phone)) {
			return RespondJson.out(RespondCodeEnum.FAIL_WRONG_INFO);
		}
		else {
			Long uid = findUidByUsernameUtil(username);
			User user = userRepository.findById(uid).get();
			user.setPhone(phone);
			userRepository.save(user);
			return RespondJson.out(RespondCodeEnum.SUCCESS, user);
		}
	}
	
	@RequestMapping(path = "/findUidByUsername")
	public Object findUidByUsername(@RequestParam String username) {
		Long uid = findUidByUsernameUtil(username);
		if (uid == null) {
			return RespondJson.out(RespondCodeEnum.FAIL_USER_NOT_FOUND);
		}
		else {
			return RespondJson.out(RespondCodeEnum.SUCCESS, uid);
		}
	}
	
	public Long findUidByUsernameUtil(String username) {
		return userRepository.findUidByUsername(username);
	}
	
}

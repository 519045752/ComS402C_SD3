package com.cs402.backend.user;

import com.cs402.backend.house.House;
import com.cs402.backend.respond.RespondCodeEnum;
import com.cs402.backend.respond.RespondJson;
import com.cs402.backend.utility.Utility;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;


@RestController
@RequestMapping("/user")
@Api(value = "/user", tags = {"API collection of user service"})
public class UserController {
	
	@Autowired
	UserRepository userRepository;
	private static final Logger log = LoggerFactory.getLogger(UserController.class);
	private static final String greeting = "Hello,%s!";
	private final AtomicLong counter = new AtomicLong();
	
	
	@GetMapping("/all")
	@ApiOperation(value = "get user information", notes = "test only")
	public RespondJson<Iterable<User>> getAllUser() {
		return RespondJson.out(RespondCodeEnum.SUCCESS, userRepository.findAll());
	}
	
	@GetMapping("/{uid}")
	@JsonIgnoreProperties(value={"password"})
	@ApiOperation(value = "visit user page by uid", notes = "")
	@ApiImplicitParam(name = "uid", value = "uid", required = true, dataType = "Long")
	public Object test(@PathVariable Long uid) {
		try {
			User user = getUserByIdUtil(uid);
			String username = user.getUsername();
			user.setPassword("PRIVATE");
			RespondJson<User> ret = RespondJson.out(RespondCodeEnum.SUCCESS, user);
			ret.setMsg(String.format(greeting, username));
			return ret;
		} catch (IndexOutOfBoundsException|NullPointerException e) {
			e.printStackTrace();
			return RespondJson.out(RespondCodeEnum.FAIL_Uid_NOT_FOUND);
		} catch (Exception e) {
			e.printStackTrace();
			RespondCodeEnum res = RespondCodeEnum.FAIL;
			res.setMgs(e.toString());
			return RespondJson.out(res);
		}
	}
	
	
	@GetMapping("/login")
	@ApiOperation(value = "login by username")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "username", value = "username", required = true, paramType = "query", dataType = "String"),
			@ApiImplicitParam(name = "password", value = "password", required = true, paramType = "query", dataType = "string")
	})
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
	@ApiOperation(value = "login by UID")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "uid", value = "uid", required = true, paramType = "query", dataType = "Long"),
			@ApiImplicitParam(name = "password", value = "password", required = true, paramType = "query", dataType = "String")
	})
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
	@PostMapping(path = "/add")
	@ApiOperation(value = "TEST ONLY! get all information from the database")
	public String addNewUser(@RequestParam String username, @RequestParam String password, @RequestParam String category) {
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		user.setCategory(category);
		userRepository.save(user);
		return "[" + Utility.getServerTime() + "] : user [" + username + "] added!";
	}
	
	@GetMapping(value = "/userlist")
	@ApiOperation(value = "get the list of all the usernames")
	public Object getUserlist() {
		return RespondJson.out(RespondCodeEnum.SUCCESS, userRepository.getUserlist());
	}
	
	
	public Boolean checkUsernameNotUsedUtil(String username) {
		List<User> list = userRepository.checkIfExist(username);
		return list.isEmpty();
	}
	
	//true: not used
	@GetMapping("/checkUsernameNotUsed")
	@ApiOperation(value = "check if the username is already in use")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "username", value = "username", required = true, paramType = "query", dataType = "String")
	})
	public Object checkUsernameNotUsed(@RequestParam String username) {
		String res = checkUsernameNotUsedUtil(username).toString();
		return RespondJson.out(RespondCodeEnum.SUCCESS, res);
	}
	
	
	@PostMapping(path = "/register")
	@ApiOperation(value = "register a new user")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "username", value = "unused username", required = true, paramType = "query", dataType = "String"),
			@ApiImplicitParam(name = "password", value = "password", required = true, paramType = "query", dataType = "string"),
			@ApiImplicitParam(name = "category", value = "user role, must be\"landlord\" or \"tenant\".", required = true, paramType = "query", dataType = "string"),
			@ApiImplicitParam(name = "email", value = "valid email address", paramType = "query", dataType = "string"),
			@ApiImplicitParam(name = "phone", value = "valid cellphone number", paramType = "query", dataType = "string")
	})
	public Object register(@RequestParam String username, @RequestParam String password, @RequestParam String category, String email, String phone) {
		if (!checkUsernameNotUsedUtil(username)) {
			return RespondJson.out(RespondCodeEnum.FAIL_USERNAME_USED);
		}
		else if (!Utility.validatePassword(password)) {
			return RespondJson.out(RespondCodeEnum.FAIL_WRONG_INFO);
		}
		else if (!Utility.validateCategory(category)) {
			return RespondJson.out(RespondCodeEnum.FAIL_WRONG_INFO);
		}
		else if (email != null && !Utility.validateEmail(email)) {
			return RespondJson.out(RespondCodeEnum.FAIL_WRONG_INFO);
		}
		else if (phone != null && !Utility.validatePhone(phone)) {
			return RespondJson.out(RespondCodeEnum.FAIL_WRONG_INFO);
		}
		else {
			User user = new User();
			user.setUsername(username);
			user.setPassword(password);
			user.setCategory(category);
			user.setEmail(email);
			user.setPhone(phone);
			userRepository.save(user);
			return RespondJson.out(RespondCodeEnum.SUCCESS, user);
		}
	}
	
	
	@PostMapping(path = "/setPassword")
	@ApiOperation(value = "reset the password for a user")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "username", value = "username", required = true, paramType = "query", dataType = "String"),
			@ApiImplicitParam(name = "password", value = "password", required = true, paramType = "query", dataType = "string"),
			@ApiImplicitParam(name = "newPassword", value = "new password", required = true, paramType = "query", dataType = "string")
	})
	public Object setPassword(@RequestParam String username, @RequestParam String password, @RequestParam String newPassword) {
		if (password.equals(newPassword)) {
			return RespondJson.out(RespondCodeEnum.WTF);
		}
		else if (checkUsernameNotUsedUtil(username)) {
			return RespondJson.out(RespondCodeEnum.FAIL_Uid_NOT_FOUND);
		}
		else if (userRepository.login(username, password).isEmpty()) {
			return RespondJson.out(RespondCodeEnum.FAIL_LOGIN_MISMATCH);
		}
		else if (!Utility.validatePassword(newPassword)) {
			return RespondJson.out(RespondCodeEnum.FAIL_WRONG_INFO);
		}
		else {
			Long uid = getUidByUsernameUtil(username);
			User user = userRepository.findById(uid).get();
			user.setPassword(newPassword);
			userRepository.save(user);
			return RespondJson.out(RespondCodeEnum.SUCCESS, user);
		}
	}
	
	@PostMapping(path = "/setUsername")
	@ApiOperation(value = "reset the username for a user")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "username", value = "username", required = true, paramType = "query", dataType = "String"),
			@ApiImplicitParam(name = "password", value = "password", required = true, paramType = "query", dataType = "string"),
			@ApiImplicitParam(name = "newUsername", value = "new username", required = true, paramType = "query", dataType = "string")
	})
	public Object setUsername(@RequestParam String username, @RequestParam String password, @RequestParam String newUsername) {
		if (username.equals(newUsername)) {
			return RespondJson.out(RespondCodeEnum.WTF);
		}
		else if (checkUsernameNotUsedUtil(username)) {
			return RespondJson.out(RespondCodeEnum.FAIL_Uid_NOT_FOUND);
		}
		else if (userRepository.login(username, password).isEmpty()) {
			return RespondJson.out(RespondCodeEnum.FAIL_LOGIN_MISMATCH);
		}
		else if (!Utility.validateUsername(newUsername)) {
			return RespondJson.out(RespondCodeEnum.FAIL_WRONG_INFO);
		}
		else if (!checkUsernameNotUsedUtil(newUsername)) {
			return RespondJson.out(RespondCodeEnum.FAIL_USERNAME_USED);
		}
		else {
			Long uid = getUidByUsernameUtil(username);
			User user = userRepository.findById(uid).get();
			user.setUsername(newUsername);
			userRepository.save(user);
			return RespondJson.out(RespondCodeEnum.SUCCESS, user);
		}
	}
	
	@PostMapping(path = "/setEmail")
	@ApiOperation(value = "set the email address for a user")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "username", value = "username", required = true, paramType = "query", dataType = "String"),
			@ApiImplicitParam(name = "password", value = "password", required = true, paramType = "query", dataType = "string"),
			@ApiImplicitParam(name = "email", value = "vaild email address", required = true, paramType = "query", dataType = "string")
	})
	public Object setEmail(@RequestParam String username, @RequestParam String password, @RequestParam String email) {
		if (checkUsernameNotUsedUtil(username)) {
			return RespondJson.out(RespondCodeEnum.FAIL_Uid_NOT_FOUND);
		}
		else if (userRepository.login(username, password).isEmpty()) {
			return RespondJson.out(RespondCodeEnum.FAIL_LOGIN_MISMATCH);
		}
		else if (!Utility.validateEmail(email)) {
			return RespondJson.out(RespondCodeEnum.FAIL_WRONG_INFO);
		}
		else {
			Long uid = getUidByUsernameUtil(username);
			User user = userRepository.findById(uid).get();
			user.setEmail(email);
			userRepository.save(user);
			return RespondJson.out(RespondCodeEnum.SUCCESS, user);
		}
	}
	
	
	@PostMapping(path = "/setPhone")
	@ApiOperation(value = "set the cellphone number for a user, this might overwrite the original cellphone number")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "username", value = "username", required = true, paramType = "query", dataType = "String"),
			@ApiImplicitParam(name = "password", value = "password", required = true, paramType = "query", dataType = "string"),
			@ApiImplicitParam(name = "phone", value = "cellphone number", required = true, paramType = "query", dataType = "string")
	})
	public Object setPhone(@RequestParam String username, @RequestParam String password, @RequestParam String phone) {
		if (checkUsernameNotUsedUtil(username)) {
			return RespondJson.out(RespondCodeEnum.FAIL_Uid_NOT_FOUND);
		}
		else if (userRepository.login(username, password).isEmpty()) {
			return RespondJson.out(RespondCodeEnum.FAIL_LOGIN_MISMATCH);
		}
		else if (!Utility.validatePhone(phone)) {
			return RespondJson.out(RespondCodeEnum.FAIL_WRONG_INFO);
		}
		else {
			Long uid = getUidByUsernameUtil(username);
			User user = userRepository.findById(uid).get();
			user.setPhone(phone);
			userRepository.save(user);
			return RespondJson.out(RespondCodeEnum.SUCCESS, user);
		}
	}
	
	
	@GetMapping("/getUserByID")
	@ApiOperation(value = "find the uid by UID")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "uid", value = "exist username", required = true, paramType = "query", dataType = "Long")
	})
	public Object getUserById(@RequestParam Long uid) {
		User user = getUserByIdUtil(uid);
		if (user == null) {
			return RespondJson.out(RespondCodeEnum.FAIL_Uid_NOT_FOUND);
		}
		else {
			return RespondJson.out(RespondCodeEnum.SUCCESS, user);
		}
	}
	
	public User getUserByIdUtil(Long uid) {
		User user = userRepository.findUserById(uid);
		log.debug("[/getUserbyID] " + user.toString());
		return user;
	}
	
	
	@GetMapping("/getOwnedHouse")
	@ApiOperation(value = "get the houses which is owned by the user")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "uid", value = "user id", required = true, paramType = "query", dataType = "Long")
	})
	public Object getOwnedHouse(@RequestParam Long uid) {
		User user = getUserByIdUtil(uid);
		if (user == null) {
			return RespondJson.out(RespondCodeEnum.FAIL_Uid_NOT_FOUND);
		}
		else {
			ArrayList<Long> houses = new ArrayList<Long>();
			for (House house:user.getOwn_houses()){
				houses.add(house.getHid());
			}
			return RespondJson.out(RespondCodeEnum.SUCCESS, houses);
		}
	}
	
	
	@PostMapping(path = "/findUidByUsername")
	@ApiOperation(value = "find the uid for a user")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "username", value = "exist username", required = true, paramType = "query", dataType = "String")
	})
	public Object getUidByUsername(@RequestParam String username) {
		Long uid = getUidByUsernameUtil(username);
		if (uid == null) {
			return RespondJson.out(RespondCodeEnum.FAIL_Uid_NOT_FOUND);
		}
		else {
			return RespondJson.out(RespondCodeEnum.SUCCESS, uid);
		}
	}
	
	public Long getUidByUsernameUtil(String username) {
		return userRepository.findUidByUsername(username);
	}
	
}

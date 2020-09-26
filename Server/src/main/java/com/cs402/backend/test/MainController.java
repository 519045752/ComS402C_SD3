package com.cs402.backend.test;

import com.cs402.backend.respond.RespondCodeEnum;
import com.cs402.backend.respond.RespondJson;
import com.cs402.backend.user.UserRepository;
import com.sun.deploy.net.URLEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

@RestController
public class MainController {
	private UserRepository userRepository;
	private static final Logger log = LoggerFactory.getLogger(MainController.class);
	
	// @GetMapping("/user/{userId}")
	// public RespondJsonBase test(@PathVariable Long userId) {
	// 	return RespondJson.out(RespondCodeEnum.SUCCESS,uc.findUserById(userId));
	// }
	@GetMapping("/online")
	public Object getTotalUser(HttpServletRequest request, HttpServletResponse response) {
		Cookie cookie;
		try {
			cookie = new Cookie("JSESSIONID", URLEncoder.encode(request.getSession().getId(), "utf-8"));
			cookie.setPath("/");
			cookie.setMaxAge(60*60*24);//1day cookie
			response.addCookie(cookie);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		Integer count = (Integer) request.getSession().getServletContext().getAttribute("count");
		return RespondJson.out(RespondCodeEnum.SUCCESS,"Current Online User：" + count);
	}
}

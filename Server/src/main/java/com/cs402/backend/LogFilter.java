package com.cs402.backend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.InetAddress;

@WebFilter(filterName = "LogFilter", urlPatterns = "/*")
@Component
public class LogFilter extends OncePerRequestFilter {
	
	private Logger log = LoggerFactory.getLogger(LogFilter.class);
	
	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest httpServletRequest, @NonNull HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
		String remoteAddr = getIpAddress(httpServletRequest);
		log.debug("Starting url: [{}], method: [{}], ip: [{}]", httpServletRequest.getRequestURL(), httpServletRequest.getMethod(), remoteAddr);
		filterChain.doFilter(httpServletRequest, httpServletResponse);
		log.debug("End url: [{}], method: [{}], ip: [{}]", httpServletRequest.getRequestURL(), httpServletRequest.getMethod(), remoteAddr);
	}
	
	public static String getIpAddress(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if(ip == null || ip.length() == 0 || "unknow".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length () == 0 || "unknown".equalsIgnoreCase (ip)) {
			ip = request.getHeader ("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length () == 0 || "unknown".equalsIgnoreCase (ip)) {
			ip = request.getRemoteAddr ();
			if (ip.equals ("127.0.0.1")) {
				InetAddress inet = null;
				try {
					inet = InetAddress.getLocalHost ();
				} catch (Exception e) {
					e.printStackTrace ();
				}
				ip = inet.getHostAddress ();
			}
		}
		if (ip != null && ip.length () > 15) {
			if (ip.indexOf (",") > 0) {
				ip = ip.substring (0, ip.indexOf (","));
			}
		}
		return ip;
	}
}
package com.cs402.backend;

import com.cs402.backend.utility.Utility;
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

import static com.cs402.backend.utility.Utility.getIpAddress;

@WebFilter(filterName = "LogFilter", urlPatterns = "/*")
@Component
public class LogFilter extends OncePerRequestFilter {
	
	private Logger log = LoggerFactory.getLogger(LogFilter.class);
	
	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest httpServletRequest, @NonNull HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
		String remoteAddr = Utility.getIpAddress(httpServletRequest);
		log.debug("Starting url: [{}], method: [{}], ip: [{}]", httpServletRequest.getRequestURL(), httpServletRequest.getMethod(), remoteAddr);
		filterChain.doFilter(httpServletRequest, httpServletResponse);
		log.debug("End url: [{}], method: [{}], ip: [{}]", httpServletRequest.getRequestURL(), httpServletRequest.getMethod(), remoteAddr);
	}
	

}
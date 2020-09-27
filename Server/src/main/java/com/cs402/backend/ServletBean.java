package com.cs402.backend;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.servlet.Servlet;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpSessionListener;

@Component
public class ServletBean {
	
	// @Bean
	// public ServletRegistrationBean<Servlet> registerServlet() {
	// 	ServletRegistrationBean<Servlet> servletRegistrationBean = new ServletRegistrationBean<Servlet>(new MyServlet(),
	// 			"/myservlet");
	// 	servletRegistrationBean.setLoadOnStartup(1);
	// 	return servletRegistrationBean;
	// }
	//
	// @Bean
	// public FilterRegistrationBean<Filter> registerFilter() {
	// 	FilterRegistrationBean<Filter> filter = new FilterRegistrationBean<>();
	// 	filter.setFilter(new MyFilter());
	// 	filter.addUrlPatterns("/*");
	// 	return filter;
	// }

	@Bean
	public ServletListenerRegistrationBean<ServletRequestListener> registerRequestListener() {
		ServletListenerRegistrationBean<ServletRequestListener> servletListenerRegistrationBean = new ServletListenerRegistrationBean<>();
		servletListenerRegistrationBean.setListener(new MyServletRequestListener());
		return servletListenerRegistrationBean;
	}
}

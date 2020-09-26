package com.cs402.backend;

import com.cs402.backend.model.RandomQuote;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Utility {
	private UserRepository userRepository;
	public static String getRandomQuote() {
		// Quote quote = restTemplate.getForObject("https://gturnquist-quoters.cfapps.io/api/random", Quote.class);
		// String value = quote.getValue().getQuote();
		// log.info(quote.toString());
		// return new Greeting(counter.incrementAndGet(), String.format(template, name), value);
		CloseableHttpClient httpClient = HttpClients.custom().setSSLHostnameVerifier(new NoopHostnameVerifier())
				.build();
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		requestFactory.setHttpClient(httpClient);
		RestTemplate restTemplate = new RestTemplate(requestFactory);
		RandomQuote randomQuote = restTemplate.getForObject("https://api.forismatic.com/api/1.0/?method=getQuote&lang=en&format=json", RandomQuote.class);
		return randomQuote.toString();
	}
	
	public static String getServerTime() {
		SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return date.format(new Date());
	}
	
	public static void sendJsonData(HttpServletResponse response, String data) throws Exception {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println(data);
		out.flush();
		out.close();
	}
	
	public static boolean validateUsername(String username) {
			return true;
	}
	
	public static boolean validatePassword(String password) {
		return true;
	}
	
	public static boolean validateCategory(String category) {
		if(!UserCategoryEnum.has(category) || category.equals("admin")){
			return false;
		}else {
			return true;
		}
	}
}
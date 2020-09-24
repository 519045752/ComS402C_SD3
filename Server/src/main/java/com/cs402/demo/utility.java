package com.cs402.demo;

import com.cs402.demo.model.RandomQuote;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;

public class utility {
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
}

package com.cs402.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RandomQuote {
	

	private String quoteText;
	private String quoteAuthor;
	
	public RandomQuote() {
	}
	
	public void setQuoteText(String quoteText) {
		this.quoteText = quoteText;
	}
	
	public void setQuoteAuthor(String quoteAuthor) {
		this.quoteAuthor = quoteAuthor;
	}
	
	
	public String getQuoteText() {
		return quoteText;
	}
	
	public String getQuoteAuthor() {
		return quoteAuthor;
	}
	
	@Override
	public String toString() {
		return quoteText + "\n---by " + quoteAuthor;
	}
}

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
		StringBuilder str = new StringBuilder(quoteText);
		for (int i = 0; i < quoteText.length()-8; i++) {
			str.append(" ");
		}
		str.append("\n---by ").append(quoteAuthor);
		return str.toString();
	}
}

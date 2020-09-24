package com.cs402.demo;

import javax.persistence.*;

@Entity
public class User {
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private Long uid;
	private String username;
	private String password;
	private String category;
	
	public Long getUid() {
		return uid;
	}
	
	public String getCategory() {
		return category;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}
	
	public void setUid(Long uid) {
		this.uid = this.uid;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
}

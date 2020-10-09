package com.cs402.backend.user;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

@Entity
@Table(name="user")
public class User {
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	
	@Column(columnDefinition = "serial")
	@ApiModelProperty(value="uid",name="_UID_")
	private Long uid;
	@ApiModelProperty(value="username",name="_USERNAME_")
	private String username;
	private String password;
	private String category;
	private String email;
	private String phone;
	
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
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}
}

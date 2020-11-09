package com.cs402.backend.user;

import com.cs402.backend.house.House;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="user")
// @JsonIgnoreProperties(value={"password"})
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "uid")
public class User {
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name = "uid")
	private long uid;
	// @Column(columnDefinition = "serial")
	// @ApiModelProperty(value="uid",name="_UID_")
	// private Long uid;

	@ApiModelProperty(value="username",name="_USERNAME_")
	private String username;
	private String password;
	private String category;
	private String email;
	private String phone;
	
	@ManyToMany
	private Set<House> rent_houses;
	
	@OneToMany
	private Set<House> own_houses;
	
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
	
	public Set<House> getRent_houses() {
		return rent_houses;
	}
	
	public void setRent_houses(Set<House> rent_houses) {
		this.rent_houses = rent_houses;
	}
	
	public Set<House> getOwn_houses() {
		return own_houses;
	}
	
	public void setOwn_houses(Set<House> own_houses) {
		this.own_houses = own_houses;
	}
}

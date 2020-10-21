package com.cs402.backend.house;

import com.cs402.backend.user.User;

import javax.persistence.*;

@Entity
@Table(name="anchor")
public class Anchor {
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		@Column(name = "aid")
		private Long aid;
	
		@ManyToOne(cascade=CascadeType.DETACH)
		@JoinColumn(name = "hid" , referencedColumnName = "hid")
		private House hid;
	
		private String cloudid;
		private int type;
		private String description;
	
	public Long getAid() {
		return aid;
	}
	
	public String getCloudid() {
		return cloudid;
	}
	
	public void setCloudid(String cloudid) {
		this.cloudid = cloudid;
	}
	
	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public House getHouse() {
		return hid;
	}
	
	public void setHouse(House house) {
		this.hid = house;
	}
}

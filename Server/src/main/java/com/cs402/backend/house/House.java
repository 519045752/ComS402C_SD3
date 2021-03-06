package com.cs402.backend.house;

import com.cs402.backend.user.User;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.*;
import javax.persistence.*;

@Entity
@Table(name="house")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "hid")
public class House {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "hid")
	private Long hid;
	
	// @Column(columnDefinition = "serial")
	// @ApiModelProperty(value="hid",name="_HID_")
	// private Long hid;
	
	private String address;
	private String addressOpt;
	private String latitude;
	private String longitude;
	
	private String price;
	private String info;
	private String data;
	
	@ManyToOne(cascade=CascadeType.DETACH)
	@JoinColumn(name = "landlord" , referencedColumnName = "uid")
	private User landlord;
	
	@ManyToMany(cascade=CascadeType.DETACH)
	private Set<User> tenant = new HashSet<>();
	
	@OneToMany
	private Set<ArObject> arObjects;
	
	public Long getHid() {
		return hid;
	}
	
	public void setHid(Long hid) {
		this.hid = hid;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getAddressOpt() {
		return addressOpt;
	}
	
	public void setAddressOpt(String addressOpt) {
		this.addressOpt = addressOpt;
	}
	
	public String getLatitude() {
		return latitude;
	}
	
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	
	public String getLongitude() {
		return longitude;
	}
	
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	
	public String getPrice() {
		return price;
	}
	
	public void setPrice(String price) {
		this.price = price;
	}
	
	public String getInfo() {
		return info;
	}
	
	public void setInfo(String info) {
		this.info = info;
	}
	
	public String getData() {
		return data;
	}
	
	public void setData(String data) {
		this.data = data;
	}
	
	// public Long getUidLandlord() {
	// 	return uidLandlord;
	// }
	//
	// public void setUidLandlord(Long uidLandlord) {
	// 	this.uidLandlord = uidLandlord;
	// }
	
	// public List<Long> getUidTenants() {
	// 	return uidTenants;
	// }
	//
	// public void setUidTenants(
	
	public User getLandlord() {
		return landlord;
	}
	
	public void setLandlord(User landlord) {
		this.landlord = landlord;
	}
	
	public Set<User> getTenant() {
		return tenant;
	}
	
	public void setTenant(Set<User> tenant) {
		this.tenant = tenant;
	}
	
	public Set<ArObject> getArObjects() {
		return arObjects;
	}
	
	public void setArObjects(Set<ArObject> has_arObjects) {
		this.arObjects = has_arObjects;
	}
}
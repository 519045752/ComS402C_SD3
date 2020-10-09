package com.cs402.backend.house;

import com.cs402.backend.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;

import java.util.*;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="house")
public class House {
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name = "hid")
	private Long hid;

	// @Column(columnDefinition = "serial")
	// @ApiModelProperty(value="hid",name="_HID_")
	// private Long hid;
	@ManyToMany(cascade = { CascadeType.ALL })
	@JoinTable(name = "tenant_rents_house", joinColumns = @JoinColumn(name = "house_id", referencedColumnName = "uid"), inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "hid"))
	private Set<User> tenant;
	private String address;
	private String addressOpt;
	private String latitude;
	private String longitude;
	
	private String price;
	private String info;
	private String data;
	
	private Long uidLandlord;
	
	// @OneToMany(mappedBy = "", cascade = CascadeType.ALL)
	@ElementCollection
	private Set<Long> uidTenants = new HashSet<>();
	
	
	
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
	
	public Long getUidLandlord() {
		return uidLandlord;
	}
	
	public void setUidLandlord(Long uidLandlord) {
		this.uidLandlord = uidLandlord;
	}
	
	// public List<Long> getUidTenants() {
	// 	return uidTenants;
	// }
	//
	// public void setUidTenants(List<Long> uidTenants) {
	// 	this.uidTenants = uidTenants;
	// }
	
	

}

package com.cs402.backend.rent;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.sql.Time;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="rent")
public class Rent {
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	
	@ManyToOne
	private Long hid;
	private Long uid;
	private Time startTime;
	private Time endTime;
	
	public Long getHid() {
		return hid;
	}
	
	public void setHid(Long hid) {
		this.hid = hid;
	}
	
	public Long getUid() {
		return uid;
	}
	
	public void setUid(Long uid) {
		this.uid = uid;
	}
	
	public Time getStartTime() {
		return startTime;
	}
	
	public void setStartTime(Time startTime) {
		this.startTime = startTime;
	}
	
	public Time getEndTime() {
		return endTime;
	}
	
	public void setEndTime(Time endTime) {
		this.endTime = endTime;
	}
}

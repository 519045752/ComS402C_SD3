package com.cs402.backend.respond;

public class RespondJsonBase {
	
	private Integer code;
	private String msg;
	
	public RespondJsonBase() {
	}
	
	public RespondJsonBase(RespondCodeEnum CodeEnum) {
		this.code = CodeEnum.getCode();
		this.msg = CodeEnum.getMgs();
	}
	
	public Integer getCode() {
		return code;
	}
	
	public void setCode(Integer code) {
		this.code = code;
	}
	
	public String getMsg() {
		return msg;
	}
	
	public void setMsg(String msg) {
		this.msg = msg;
	}
}

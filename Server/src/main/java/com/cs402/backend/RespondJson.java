package com.cs402.backend;

public class RespondJson <T> extends RespondJsonBase {
	
	private T data;
	
	public RespondJson(RespondCodeEnum CodeEnum) {
		super(CodeEnum);
	}
	
	public T getData() {
		return data;
	}
	
	public void setData(T data) {
		this.data = data;
	}
	
	public RespondJson(RespondCodeEnum CodeEnum, T data) {
		super(CodeEnum);
		this.data = data;
	}
	
	public static RespondJsonBase out(RespondCodeEnum codeEnum){
		return new RespondJsonBase(codeEnum);
	}
	
	public static <T> RespondJson<T> out(RespondCodeEnum codeEnum, T data){
		return new RespondJson<>(codeEnum, data);
	}
}

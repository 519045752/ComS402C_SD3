package com.cs402.backend.respond;

/**
   @description
    This class enums the status codes and messages.
*/
public enum RespondCodeEnum {
	
	SUCCESS(200,"Success"),
	
	FAIL(500,"Fail"),
	FAIL_USER_NOT_FOUND(501,"user Not Found"),
	FAIL_LOGIN_MISMATCH(502,"Username/Password Doesn't Match"),
	FAIL_USERNAME_USED(520,"Username is already in used"),
	FAIL_WRONG_INFO(521,"Wrong info provided, check format"),
	WTF(999,"WHAT ARE U DOING QUESTION MARK"),
	;
	
	private final Integer code;
	private String mgs;

	RespondCodeEnum(Integer code, String mgs) {
		this.code = code;
		this.mgs = mgs;
	}
	
	public Integer getCode() {
		return code;
	}
	public String getMgs() {
		return mgs;
	}
	
	// This function breaks the point of using enum, but will use it anyway for testing.
	public String  setMgs(String mgs) {
		this.mgs = mgs;
		return this.toString();
	}
}

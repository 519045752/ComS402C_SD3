package com.cs402.backend.user;

public enum UserCategoryEnum {
	admin, landlord, tenant;
	
	public static boolean has(String str){
		for(UserCategoryEnum choice:values())
			if (choice.name().equals(str))
				return true;
		return false;
	}
}

package com.doclearn.model;

public enum UserRole {
	   student,
	    author,
	    admin;
	    
	    public static UserRole fromString(String role) {
	        return UserRole.valueOf(role.toUpperCase());
	    }
}

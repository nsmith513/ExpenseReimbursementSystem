package com.project.one.model;

/**
 * Model for the user_roles lookup table.
 * 
 * @author Nicholas Smith
 */
public enum UserRole {
    EMPLOYEE(0), FINANCEMAN(1);
	
    private final int value;
    
    private UserRole(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
	
	public static UserRole fromString(String s) {
		switch (s.toLowerCase()) {
		case "employee":
			return EMPLOYEE;
		case "financeman": case "finance manager":
			return FINANCEMAN;
		}
		
		throw new IllegalArgumentException("Cannot convert \"" + s + "\" to " + UserRole.class);
	}
	
    @Override
    public String toString() {
        switch (ordinal()) {
	        case 0: return "Employee";
	        case 1: return "Finance Manager";
        }
        return "";
    }
}

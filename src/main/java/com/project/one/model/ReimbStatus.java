package com.project.one.model;

/**
 * Model for the reimbursement_status lookup table.
 * 
 * @author Nicholas Smith
 */
public enum ReimbStatus {
    PENDING(0), APPROVED(1), DENIED(2);
	
    private final int value;
    
    private ReimbStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

	public static ReimbStatus fromString(String s) {
		return ReimbStatus.valueOf(s.toUpperCase());
	}
	
    @Override
    public String toString() {
        switch (ordinal()) {
	        case 0: return "Pending";
	        case 1: return "Approved";
	        case 2: return "Denied";
        }
        return "";
    }
}

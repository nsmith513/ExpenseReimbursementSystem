package com.project.one.model;

/**
 * Model for the reimbursement_type lookup table.
 * 
 * @author Nicholas Smith
 */
public enum ReimbType {
    LODGING(0), TRAVEL(1), FOOD(2), OTHER(3);
	
    private final int value;
    
    private ReimbType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
	
	public static ReimbType fromString(String s) {
		return ReimbType.valueOf(s.toUpperCase());
	}

    @Override
    public String toString() {
        switch (ordinal()) {
	        case 0: return "Lodging";
	        case 1: return "Travel";
	        case 2: return "Food";
	        case 3: return "Other";
        }
        return "";
    }
}

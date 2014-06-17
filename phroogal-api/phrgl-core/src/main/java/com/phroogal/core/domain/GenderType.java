package com.phroogal.core.domain;

public enum GenderType {
	
	MALE("Male"), FEMALE("Female");
	
	private GenderType(String value) {
		this.value = value;
	}
	
	private String value;
	
	public static GenderType get(String value) {
		for (GenderType genderType : GenderType.values()) {
			if (genderType.getValue().equalsIgnoreCase(value)) {
				return genderType;
			}
		}
		return null;
	}

	public String getValue() {
		return value;
	}
	
}

package com.phroogal.core.domain;

import com.phroogal.core.exception.FlagStatusTypeNotSupportedException;

public enum FlagNotificationStatusType {

	ACTIVE ("active"), DELETED("deleted");
	
	private FlagNotificationStatusType(String value) {
		this.value = value;
	}
	
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public static FlagNotificationStatusType get(String value) {
		for (FlagNotificationStatusType statusType : FlagNotificationStatusType.values()) {
			if (statusType.getValue().equalsIgnoreCase(value)) {
				return statusType;
			}
		}
		throw new FlagStatusTypeNotSupportedException();				
	}
}

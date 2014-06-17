package com.phroogal.core.domain.security;

import org.springframework.security.core.GrantedAuthority;

/**
 * Roles that could be assigned to a user
 * @author Christopher Mariano
 *
 */
public enum UserRoleType implements GrantedAuthority {
	
	USER("User"), ADMIN("Admin");
	
	private String value;
	
	public static UserRoleType get(String authority) {
		for (UserRoleType userRoleType : UserRoleType.values()) {
			if (userRoleType.getAuthority().equalsIgnoreCase(authority)) {
				return userRoleType;
			}
		}
		return null;
	}
	
	UserRoleType(String value) {
		this.value = value;
	}
	
	@Override
	public String getAuthority() {
		return value;
	}
}

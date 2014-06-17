package com.phroogal.core.valueobjects;

import java.io.Serializable;

import org.springframework.util.StringUtils;


/**
 * Value Object that holds User Credentials for use with identity verification
 * @author Christopher Mariano
 *
 */
public class UserCredentials implements Serializable {
	
	private static final long serialVersionUID = 1990426280733799359L;

	public static final UserCredentials NO_CREDENTIALS = new UserCredentials(null, null);

	private final String username;
	private final String password;
	
	public UserCredentials(String username, String password) {
		this.username = StringUtils.hasText(username) ? username : null;
		this.password = StringUtils.hasText(password) ? password : null;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

}

/**
 * 
 */
package com.phroogal.web.bean;

/**
 * Holds User credentials to be used in login validations
 * @author Christopher Mariano
 *
 */
public class UserCredentialsBean {
	
	private String username;
	
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}

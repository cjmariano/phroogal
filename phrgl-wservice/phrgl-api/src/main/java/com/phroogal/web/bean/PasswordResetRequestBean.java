package com.phroogal.web.bean;


/**
 * Bean that holds information that would require for a password reset request
 * @author Christopher Mariano
 *
 */
public class PasswordResetRequestBean {

	private String id;
	
	private String email;
	
	private String requestDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(String requestDate) {
		this.requestDate = requestDate;
	}
}

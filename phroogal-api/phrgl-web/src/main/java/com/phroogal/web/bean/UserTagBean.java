/**
 * 
 */
package com.phroogal.web.bean;


/**
 * Bean to hold documents of type user tags.
 * @author Christopher Mariano
 *
 */
public class UserTagBean extends TagBean{
	
	private String userId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}

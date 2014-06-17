/**
 * 
 */
package com.phroogal.web.bean;

public class SocialProfileBean {
	
	private SocialNetworkTypeBean site;
	
	private String userId;
	
	private String email;
	
	private String profileUrl;
	
	public SocialNetworkTypeBean getSite() {
		return site;
	}

	public void setSite(SocialNetworkTypeBean site) {
		this.site = site;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getProfileUrl() {
		return profileUrl;
	}

	public void setProfileUrl(String profileUrl) {
		this.profileUrl = profileUrl;
	}

		
}

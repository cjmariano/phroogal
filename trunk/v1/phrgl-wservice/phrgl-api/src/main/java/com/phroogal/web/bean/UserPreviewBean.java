/**
 * Contains a preview of a user that contains minimal information for display purposes
 */
package com.phroogal.web.bean;

import com.phroogal.core.social.SocialNetworkType;


public class UserPreviewBean {
	
	private String id;
	
	private String createdOn;
	
	private String lastLoginOn;
	
	private UserProfilePreviewBean profile;
	
	private SocialNetworkType primarySocialNetworkConnection;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	public String getLastLoginOn() {
		return lastLoginOn;
	}

	public void setLastLoginOn(String lastLoginOn) {
		this.lastLoginOn = lastLoginOn;
	}

	public UserProfilePreviewBean getProfile() {
		return profile;
	}

	public void setProfile(UserProfilePreviewBean profile) {
		this.profile = profile;
	}

	public SocialNetworkType getPrimarySocialNetworkConnection() {
		return primarySocialNetworkConnection;
	}

	public void setPrimarySocialNetworkConnection( SocialNetworkType primarySocialNetworkConnection) {
		this.primarySocialNetworkConnection = primarySocialNetworkConnection;
	}
}

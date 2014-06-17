/**
 * 
 */
package com.phroogal.web.bean;



public class UserProfilePreviewBean {
	
	private String firstname;
	
	private String lastname;
	
	private String email;
	
	private String profilePictureUrl;
	
	private String profileSmallPictureUrl;
	
	private String bio; 
	
	//TODO You may use String here, Location has an overriden toString() already -cjm
	private LocationBean location;
	
	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getProfilePictureUrl() {
		return profilePictureUrl;
	}

	public void setProfilePictureUrl(String profilePictureUrl) {
		this.profilePictureUrl = profilePictureUrl;
	}

	public String getProfileSmallPictureUrl() {
		return profileSmallPictureUrl;
	}

	public void setProfileSmallPictureUrl(String profileSmallPictureUrl) {
		this.profileSmallPictureUrl = profileSmallPictureUrl;
	}

	public LocationBean getLocation() {
		return location;
	}

	public void setLocation(LocationBean location) {
		this.location = location;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}
}

/**
 * 
 */
package com.phroogal.web.bean;

import com.phroogal.core.domain.UserProfile;

/**
 * Data Transfer object for {@link UserProfile}
 * @author Christopher Mariano
 *
 */
public class UserProfileBean {
	
	private String firstname;
	
	private String lastname;
	
	private String email;
	
	private String profilePictureUrl;
	
	private String profileSmallPictureUrl;
	
	private String bio;
	
	private LocationBean location;
	
	private String sex;
	
	private String altEmail;
	
	private String dob;
	
	private String maritalStatus;
	
	private String education;
	
	private String college;
	
	private String crUnion;
	
	private String websiteUrl;
	
	private String[] expertise;
	
	private String lifeStyleGoal;
	
	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public LocationBean getLocation() {
		return location;
	}

	public void setLocation(LocationBean location) {
		this.location = location;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
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

	public String getAltEmail() {
		return altEmail;
	}

	public void setAltEmail(String altEmail) {
		this.altEmail = altEmail;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getCrUnion() {
		return crUnion;
	}

	public void setCrUnion(String crUnion) {
		this.crUnion = crUnion;
	}

	public String getCollege() {
		return college;
	}

	public void setCollege(String college) {
		this.college = college;
	}

	public String getWebsiteUrl() {
		return websiteUrl;
	}

	public void setWebsiteUrl(String websiteUrl) {
		this.websiteUrl = websiteUrl;
	}

	public String[] getExpertise() {
		return expertise;
	}

	public void setExpertise(String[] expertise) {
		this.expertise = expertise;
	}

	public String getLifeStyleGoal() {
		return lifeStyleGoal;
	}

	public void setLifeStyleGoal(String lifeStyleGoal) {
		this.lifeStyleGoal = lifeStyleGoal;
	}
}

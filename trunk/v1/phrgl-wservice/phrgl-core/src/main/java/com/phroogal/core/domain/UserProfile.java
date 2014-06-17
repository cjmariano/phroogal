/**
 * 
 */
package com.phroogal.core.domain;

import java.io.Serializable;
import java.util.List;

import org.joda.time.DateTime;

import com.phroogal.core.social.SocialNetwork;


/**
 * Domain that contains all relevant information about a user.
 * @author Christopher Mariano
 *
 */
public class UserProfile implements Serializable {
	
	private static final long serialVersionUID = -1423150476185411480L;

	public static final String ANONYMOUS_ROLE = "anonymousUser";
	
	private String password;
	
	private String firstname;
	
	private String lastname;
	
	private String profilePictureUrl;
	
	private String profileSmallPictureUrl;
	
	private String email;
	
	private String bio;
	
	private Location location;
	
	private GenderType sex;
	
	private String altEmail;
	
	private DateTime dob;
	
	private String maritalStatus;
	
	private String education;
	
	private String crUnion;
	
	private String college; 
	
	private String websiteUrl; 
	
	private List<String> expertise;
	
	private String lifeStyleGoal;
	
	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public Location getLocation() {
		if (location == null) {
			return new Location();
		}
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public GenderType getSex() {
		return sex;
	}

	public void setSex(GenderType sex) {
		this.sex = sex;
	}
	
	public String getAltEmail() {
		return altEmail;
	}

	public void setAltEmail(String altEmail) {
		this.altEmail = altEmail;
	}
	
	/**
	 * Convenience method that transfers data from a social profile connection into the {@link UserProfile} domain
	 * @param social network connection that holds data
	 * @return instance of populated {@link UserProfile} 
	 */
	public static UserProfile createUserProfile(SocialNetwork api) {
		UserProfile user = new UserProfile();
		user.setFirstname(api.getFirstName());
		user.setLastname(api.getLastName());
		user.setBio(api.getBio());
		user.setLocation(api.getLocation());
		user.setProfilePictureUrl(api.getProfilePictureUrl());
		user.setProfileSmallPictureUrl(api.getProfileSmallPictureUrl());
		user.setEmail(api.getPrimaryEmail());
		user.setDob(api.getDateOfBirth());
		user.setSex(api.getGender());
		return user;
	}
	
	public String getUsername() {
		return getEmail();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public String getProfilePictureUrl() {
		return profilePictureUrl;
	}

	public void setProfilePictureUrl(String profilePictureUrl) {
		this.profilePictureUrl = profilePictureUrl;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getProfileSmallPictureUrl() {
		return profileSmallPictureUrl;
	}

	public void setProfileSmallPictureUrl(String profileSmallPictureUrl) {
		this.profileSmallPictureUrl = profileSmallPictureUrl;
	}

	public DateTime getDob() {
		return dob;
	}

	public void setDob(DateTime dob) {
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

	public List<String> getExpertise() {
		return expertise;
	}

	public void setExpertise(List<String> expertise) {
		this.expertise = expertise;
	}

	public String getLifeStyleGoal() {
		return lifeStyleGoal;
	}

	public void setLifeStyleGoal(String lifeStyleGoal) {
		this.lifeStyleGoal = lifeStyleGoal;
	}
}

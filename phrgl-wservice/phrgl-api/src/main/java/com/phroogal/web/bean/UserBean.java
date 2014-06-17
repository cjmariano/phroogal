/**
 * 
 */
package com.phroogal.web.bean;

import java.util.List;

import com.phroogal.core.social.SocialNetworkType;
import com.phroogal.core.utility.CollectionUtil;


public class UserBean {
	
	private String id;
	
	private UserProfileBean profile;
	
	private String createdOn;
	
	private String lastLoginOn;
	
	private SocialNetworkType primarySocialNetworkConnection;
	
	private List<SocialProfileBean> socialProfiles = CollectionUtil.arrayList();
	
	private boolean enabled = true;
	
	private String username;
	
	private boolean emailVerified;
	
	private boolean accountNonExpired = true;
	
	private boolean accountNonLocked = true;
	
	private boolean credentialsNonExpired = true;
	
	private List<String> grantedAuthorities;
	
	private boolean newUser = false;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public UserProfileBean getProfile() {
		if (profile == null) {
			profile = new UserProfileBean();
		}
		return profile;
	}

	public void setProfile(UserProfileBean profile) {
		this.profile = profile;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public List<SocialProfileBean> getSocialProfiles() {
		return socialProfiles;
	}

	public void setSocialProfiles(List<SocialProfileBean> socialProfiles) {
		this.socialProfiles = socialProfiles;
	}
	
	public boolean isNewUser() {
		return newUser;
	}

	public void setNewUser(boolean newUser) {
		this.newUser = newUser;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean isEmailVerified() {
		return emailVerified;
	}

	public void setEmailVerified(boolean emailVerified) {
		this.emailVerified = emailVerified;
	}

	public SocialNetworkType getPrimarySocialNetworkConnection() {
		return primarySocialNetworkConnection;
	}

	public void setPrimarySocialNetworkConnection(
			SocialNetworkType primarySocialNetworkConnection) {
		this.primarySocialNetworkConnection = primarySocialNetworkConnection;
	}

	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	public List<String> getGrantedAuthorities() {
		if (grantedAuthorities == null) {
			grantedAuthorities = CollectionUtil.arrayList();
		}
		return grantedAuthorities;
	}

	public void setGrantedAuthorities(List<String> grantedAuthorities) {
		this.grantedAuthorities = grantedAuthorities;
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
	
}

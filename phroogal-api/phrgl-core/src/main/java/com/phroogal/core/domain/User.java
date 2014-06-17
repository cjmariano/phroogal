/**
 * 
 */
package com.phroogal.core.domain;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;

import com.phroogal.core.domain.security.UserAccountDetails;
import com.phroogal.core.domain.security.UserRoleType;
import com.phroogal.core.exception.SocialNetworkNotSupportedException;
import com.phroogal.core.rule.Fact;
import com.phroogal.core.social.SocialNetworkType;
import com.phroogal.core.utility.CollectionUtil;
import com.phroogal.core.valueobjects.PropertyBag;


/**
 * Domain representation for the users of the application
 * @author Christopher Mariano
 *
 */
@Document(collection = ApplicationConstants.COLLECTION_USERS)
public class User implements Persistent<ObjectId>, Serializable, UserAccountDetails, PropertyModifiable, Fact {
	
	private static final long serialVersionUID = 4824473841485263298L;
	
	@Id
	private ObjectId id;
	
	@Indexed
	private UserProfile profile;
	
	@CreatedDate
	private DateTime createdOn;
	
	@LastModifiedDate
	private DateTime modifiedOn;
	
	private DateTime lastLoginOn;
	
	private SocialNetworkType primarySocialNetworkConnection;
	
	private List<SocialProfile> socialProfiles;
	
	private String username;

	private boolean enabled = true;
	
	private boolean emailVerified;
	
	private boolean accountActive = true;
	
	private boolean accountNonExpired = true;
	
	private boolean accountNonLocked = true;
	
	private boolean credentialsNonExpired = true;
	
	private List<UserRoleType> grantedAuthorities;
	
	@Override
	public void partialUpdate(PropertyBag propertyBag) throws Exception {
		PropertyUtils.setProperty(this, propertyBag.getProperty(), propertyBag.getValue());
	}
	
	@Transient
	private boolean isNewUser = false;
	
	/**
	 * Adds the social profile to this user and sets the attribute {@link SocialProfile}.isPrimaryConnection
	 * to true, if the social profile being added, is the same being used to login.
	 * @param socialProfile
	 */
	public void connectSocialProfile(SocialProfile socialProfile) {
		removeSocialProfile(socialProfile.getSite());
		addSocialProfile(socialProfile);
	}

	/**
	 * Removes a social profile from a user, except if the social profile is the primary connection.
	 * @param socialNetworkType
	 */
	public void disconnectSocialProfile(SocialNetworkType socialNetworkType) {
		if (! socialNetworkType.equals(primarySocialNetworkConnection)) {
			removeSocialProfile(socialNetworkType);	
		}
	}
	
	/**
	 * Returns the social profile that matches the {@link SocialNetworkType}
	 * @param socialProfile for the given social type
	 * @throws SocialNetworkNotSupportedException
	 */
	public SocialProfile getSocialProfile(SocialNetworkType type) {
		for (SocialProfile socialProfile : getSocialProfiles()) {
			if (socialProfile.getSite().equals(type)) {
				return socialProfile;
			}
		}
		throw new SocialNetworkNotSupportedException();
	}
	
	/**
	 * Returns the user's complete name
	 * @return first name _ last name. If user profile is null, returns an empty string
	 */
	public String getUserCompleteName() {
		UserProfile profile = getProfile();
		if (profile != null) {
			return new StringBuffer(profile.getFirstname()).append(" ").append(profile.getLastname()).toString();	
		}
		return StringUtils.EMPTY;
	}
	
	/**
	 * Convenience method that determines if this user has a particular role.
	 * @param role of type {@link UserRoleType} where the user's role would be validated against
	 * @return true if the user has the given role, false otherwise
	 */
	public boolean hasRole(UserRoleType role) {
		for (UserRoleType roleType : getGrantedAuthorities()) {
			if (roleType.equals(role)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public ObjectId getId() {
		return id;
	}

	@Override
	public void setId(ObjectId id) {
		this.id = id;
	}
	
	public UserProfile getProfile() {
		if (profile == null) {
			profile = new UserProfile();
		}
		return profile;
	}

	public void setProfile(UserProfile profile) {
		this.profile = profile;
	}
	
	public List<SocialProfile> getSocialProfiles() {
		if (socialProfiles == null) {
			socialProfiles = CollectionUtil.arrayList();
		}
		return socialProfiles;
	}

	public void setSocialProfiles(List<SocialProfile> socialProfile) {
		this.socialProfiles = socialProfile;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return profile.getPassword();
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void setGrantedAuthorities(List<UserRoleType> grantedAuthorities) {
		this.grantedAuthorities = grantedAuthorities;
	}
	
	public List<UserRoleType> getGrantedAuthorities() {
		if (grantedAuthorities == null) {
			grantedAuthorities = CollectionUtil.arrayList();
		}
		return grantedAuthorities;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return getGrantedAuthorities();
	}

	@Override
	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}
	
	public boolean isAccountActive() {
		return accountActive;
	}
	
	public SocialNetworkType getPrimarySocialNetworkConnection() {
		return primarySocialNetworkConnection;
	}

	public void setPrimarySocialNetworkConnection(SocialNetworkType primarySocialNetworkConnection) {
		this.primarySocialNetworkConnection = primarySocialNetworkConnection;
	}
	
	private void addSocialProfile(SocialProfile socialProfile) {
		if (socialProfile.getSite().equals(primarySocialNetworkConnection)) {
			socialProfile.setPrimaryConnection(true);
		}
		socialProfiles.add(socialProfile);
	}

	private void removeSocialProfile(SocialNetworkType socialNetworkType) {
		for (SocialProfile socialProfile : getSocialProfiles()) {
			if (socialProfile.getSite().equals(socialNetworkType)) {
				socialProfiles.remove(socialProfile);
				break;
			}
		}
	}

	public boolean isNewUser() {
		return isNewUser;
	}

	public void setNewUser(boolean isNewUser) {
		this.isNewUser = isNewUser;
	}

	public boolean isEmailVerified() {
		return emailVerified;
	}

	public void setEmailVerified(boolean emailVerified) {
		this.emailVerified = emailVerified;
	}
	
	public void setAccountActive(boolean accountActive) {
		this.accountActive = accountActive;
	}
	
	public DateTime getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(DateTime createdOn) {
		this.createdOn = createdOn;
	}

	public DateTime getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(DateTime modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	public DateTime getLastLoginOn() {
		return lastLoginOn;
	}

	public void setLastLoginOn(DateTime lastLoginOn) {
		this.lastLoginOn = lastLoginOn;
	}

	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(String.format("User[%s|%s|%s]", id.toString(), username, this.getUserCompleteName()));
		return sb.toString();
	}
}

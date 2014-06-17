/**
 * 
 */
package com.phroogal.core.service;

import org.bson.types.ObjectId;

import com.phroogal.core.domain.User;
import com.phroogal.core.domain.UserSocialContact;
/**
 * Service for {@link UserSocialContact} functions
 * @author Christopher Mariano
 *
 */
public interface UserSocialContactService extends Service<UserSocialContact, ObjectId> {

	/**
	 * Gets a user social contact by user id
	 * @param userId to be fetched
	 * @return the {@link UserSocialContact} that matches the user id 
	 */
	public UserSocialContact getByUserId (ObjectId userId);
	
	/**
	 * Synchronizes the social contacts of the given user
	 * @param user whose social contact is to be refreshed
	 */
	public void refreshUserSocialContact (User user);
}

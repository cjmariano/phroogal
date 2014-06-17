/**
 * 
 */
package com.phroogal.core.service;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.social.connect.Connection;

import com.phroogal.core.domain.SocialContact;
import com.phroogal.core.domain.User;
import com.phroogal.core.social.SocialNetworkType;

/**
 * Service for {@link SocialContact} functions
 * @author Christopher Mariano
 *
 */
public interface SocialContactService extends Service<SocialContact, ObjectId> {

	/**
	 * Retrieves a list of {@link SocialContact} by the given <code>userId</code> and <code>connectedThru</code>
	 * @param userId - the user with the list of social contacts
	 * @param connectedThru - instance of {@link SocialNetworkType} that states the social contact connection type
	 * @return list of {@link SocialContact}
	 */
	public List<SocialContact> getByUserIdAndConnectedThru(ObjectId userId, SocialNetworkType connectedThru);
	
	/**
	 * Retrieves a social contact given the owner userid
	 * @param userId is the user connected to the social contacts
	 * @param connectedThru - the social network that this connection refers to
	 * @return list of {@link SocialContact}
	 */
	public List<SocialContact> getByUserId(ObjectId userId);
	
	/**
	 * Updates a user's social network contacts
	 * @param user we want the contacts to be updated
	 * @param connection - the current social connection. 
	 */
	public void synchSocialNetworkContacts(User user, Connection<?> connection);
	
	/**
	 * Removes the user's social network contacts
	 * @param userId of user we want the contacts to be deleted
	 * @param socialNetworkType - that will be removed 
	 */
	public void removeSocialNetworkContacts(ObjectId userId, SocialNetworkType socialNetworkType);
	
}

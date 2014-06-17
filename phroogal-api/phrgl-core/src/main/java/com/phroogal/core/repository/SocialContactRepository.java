/**
 * 
 */
package com.phroogal.core.repository;

import java.util.List;

import org.bson.types.ObjectId;

import com.phroogal.core.domain.SocialContact;
import com.phroogal.core.social.SocialNetworkType;

/**
 * Provides data store fnctionality for the {@link SocialContact} data
 * @author Reylen Catungal
 *
 */
public interface SocialContactRepository extends BaseMongoRepository<SocialContact> {
	
	/**
	 * Retrieves a social contact given the owner userid and the type of social network it is connected by
	 * @param userId is the user connected to the social contacts
	 * @param connectedThru - the social network that this connection refers to
	 * @return list of social contacts of the given user and social network type
	 */
	public List<SocialContact> findByUserIdAndConnectedThru(ObjectId userId, SocialNetworkType connectedThru);
	
	/**
	 * Retrieves a social contact given the owner userid
	 * @param userId is the user connected to the social contacts
	 * @return list of social contacts of the given user
	 */
	public List<SocialContact> findByUserId(ObjectId userId);
	
	/**
	 * Retrieves a social contact given the contact user id and the type of social network it is connected by
	 * @param contactUserId social network id of the contact
	 * @param connectedThru - the social network that this connection refers to 
	 * @return list of social contacts of the given user contact and social network type
	 */
	public List<SocialContact> findByContactUserIdAndConnectedThru(String contactUserId, SocialNetworkType connectedThru);

}

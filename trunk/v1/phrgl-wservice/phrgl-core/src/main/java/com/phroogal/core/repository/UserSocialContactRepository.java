/**
 * 
 */
package com.phroogal.core.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.phroogal.core.domain.UserSocialContact;

/**
 * Repository for {@link UserSocialContact} data
 * 
 * @author Christopher Mariano
 * 
 */
public interface UserSocialContactRepository extends MongoRepository<UserSocialContact, ObjectId>{	
	
	/**
	 * Retrieves a user social contact by the user id
	 * @param user id
	 * @return the user social contact associated with this user id.
	 */
	public UserSocialContact findByUserId(ObjectId userId);
}

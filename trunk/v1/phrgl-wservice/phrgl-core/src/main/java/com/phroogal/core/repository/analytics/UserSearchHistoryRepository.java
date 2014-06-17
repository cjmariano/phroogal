/**
 * 
 */
package com.phroogal.core.repository.analytics;

import org.bson.types.ObjectId;

import com.phroogal.core.domain.analytics.UserSearchHistory;
import com.phroogal.core.repository.BaseMongoRepository;

/**
 * Repository for {@link UserSearchHistory} data
 * 
 * @author Christopher Mariano
 * 
 */
public interface UserSearchHistoryRepository extends BaseMongoRepository<UserSearchHistory>{

	/**
	 * Retrieves a user search history by the user id
	 * @param user id
	 * @return the user search history associated with this user id.
	 */
	public UserSearchHistory findByUserId(ObjectId userId);
	
}

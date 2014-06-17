/**
 * 
 */
package com.phroogal.core.service;

import java.util.List;

import org.bson.types.ObjectId;

import com.phroogal.core.domain.User;
import com.phroogal.core.domain.UserTag;



public interface UserTagService extends Service<UserTag, ObjectId> {

	/**
	 * Retrieves a list of tags associated with a user
	 * @param userId - id of the user
	 * @return list of tags by a user
	 */
	public List<UserTag> getByUserId(ObjectId userId);
	
	/**
	 * Convenience method that populates the default user tags for the given user
	 * @param user where the default user tags is to be set.
	 */
	public void createDefaultUserTagsFor(User user);
}

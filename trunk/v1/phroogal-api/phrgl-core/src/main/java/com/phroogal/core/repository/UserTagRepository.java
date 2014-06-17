/**
 * 
 */
package com.phroogal.core.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.phroogal.core.domain.UserTag;

/**
 * Repository for {@link UserTag} data
 * 
 * @author Christopher Mariano
 * 
 */
public interface UserTagRepository extends MongoRepository<UserTag, ObjectId>{	
	
	/**
	 * Retrieves a list of tags associated with a user
	 * @param userId - id of the user
	 * @return list of tags by a user
	 */
	public List<UserTag> findByUserId(ObjectId userId);
	
	/**
	 * Retrieves a list of tags with the given name
	 * @param name - the tag name
	 * @return list of tags filtered by name
	 */
	@Query("{userId: ?0, name:{$regex : ?1, $options : 'i'} }")
	public List<UserTag> findByUserIdAndName(ObjectId userId, String name);
}

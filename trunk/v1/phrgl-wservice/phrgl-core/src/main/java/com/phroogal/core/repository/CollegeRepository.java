/**
 * 
 */
package com.phroogal.core.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.phroogal.core.domain.College;

/**
 * Repository for {@link College} data
 * 
 */
public interface CollegeRepository extends MongoRepository<College,ObjectId>{	
	
	/**
	 * Retrieves a list of Colleges based on the given keyword
	 * @param keyword to be used to filter the results
	 * @return the list of Colleges
	 */
	@Query("{name:{$regex : ?0, $options : 'i'}}")
	public List<College> findByNameLike(String keyword);
}

/**
 * 
 */
package com.phroogal.core.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;

import com.phroogal.core.domain.Review;

/**
 * Repository for {@link review} data
 * 
 * 
 */
public interface ReviewRepository extends BaseMongoRepository<Review>{
	
	/**
	 * Returns a list of reviews given a brand id.
	 * @param brandRef
	 * @return
	 */
	public List<Review> findByBrandRef (ObjectId brandRef);
	
	
	/**
	 * Retrieves a list of reviews by user who posted it
	 * @param id is the user id
	 * @return matching reviews
	 */
	@Query("{ 'postBy': {$ref:'users',$id:?0}} }")
	public List<Review> findByPostById(ObjectId userId);
}

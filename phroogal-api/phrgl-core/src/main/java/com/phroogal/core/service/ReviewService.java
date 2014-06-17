/**
 * 
 */
package com.phroogal.core.service;

import java.util.List;

import org.bson.types.ObjectId;

import com.phroogal.core.domain.Review;

/**
 * Service for {@link Reviews} functions
 *
 */
public interface ReviewService extends Service<Review, ObjectId> {
	
	/**
	 * Returns a list of reviews given a brand id.
	 * @param BrandRef
	 * @return
	 */
	public List<Review> getByBrandRef (ObjectId brandRef);
	
	/**
	 * Implement Review Hierarchy rules on the given collection of reviews
	 * @param reviews to be sorted
	 */
	public void sortByReviewHierarchyRules(List<Review> reviews);
	
	
	/**
	 * Returns a {@link Review} that is posted by user of the given id  
	 * @param userId - is the user id
	 * @return a list of matching {@link Review} document
	 */
	public List<Review> getByUserId(ObjectId userId);
}

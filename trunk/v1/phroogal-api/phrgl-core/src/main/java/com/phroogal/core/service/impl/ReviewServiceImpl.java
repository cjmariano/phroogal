package com.phroogal.core.service.impl;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.phroogal.core.domain.Answer;
import com.phroogal.core.domain.Review;
import com.phroogal.core.repository.ReviewRepository;
import com.phroogal.core.service.ReviewService;
import com.phroogal.core.utility.CollectionUtil;

/**
 * Default implementation of the {@link Answer} interface
 *
 */
@Service
public class ReviewServiceImpl extends BaseService<Review, ObjectId, ReviewRepository> implements ReviewService{

	@Autowired
	private ReviewRepository reviewRepository;
	
	
	@Override
	protected ReviewRepository getRepository() {
		return reviewRepository;
	}
	
	@Override
	public List<Review> getByBrandRef(ObjectId brandRef) {
		return reviewRepository.findByBrandRef(brandRef);
	}
	
	
	@Override
	public void sortByReviewHierarchyRules(List<Review> reviews) {
		if (reviews!= null && !reviews.isEmpty()) {
			CollectionUtil.sortElementsByDescRank(reviews);
		}
	}
	
	@Override
	public List<Review> getByUserId(ObjectId userId) {
		return reviewRepository.findByPostById(userId);
	}

}

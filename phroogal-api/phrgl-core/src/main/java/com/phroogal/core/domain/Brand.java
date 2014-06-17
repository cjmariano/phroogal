package com.phroogal.core.domain;


import java.io.Serializable;
import java.util.List;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 
 * Domain representation for the brands of the application
 *
 */
@Document(collection = ApplicationConstants.COLLECTION_BRANDS)
public class Brand extends Post implements Serializable{
	
	private static final long serialVersionUID = 4824473841485263298L;
	
	private String name;
		
	private String shortDescription;
		
	private String longDescription;
		
	private List<String> tags;	
		
	private String profilePictureUrl;
	    
	private String url;
	
	private List<Review> reviews;
	
	@Transient
	private Review topReview;
	
	private boolean isReviewd;
	 
	private int totalReviewCount;
	
	private int totalCommentCount;
	
	@Override
	public
	//TODO: Ei Brand, I don't think you are of type Post.. -cjm 
 PostType getPostType() {
		return null;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getLongDescription() {
		return longDescription;
	}

	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}	

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public String getProfilePictureUrl() {
		return profilePictureUrl;
	}

	public void setProfilePictureUrl(String profilePictureUrl) {
		this.profilePictureUrl = profilePictureUrl;
	}	

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<Review> getReviews() {
		return reviews;
	}

	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}

	public Review getTopReview() {
		return topReview;
	}

	public void setTopReview(Review topReview) {
		this.topReview = topReview;
	}

	public int getTotalReviewCount() {
		return totalReviewCount;
	}
	
	public boolean isReviewd() {
		return isReviewd;
	}

	public void setReviewd(boolean isReviewd) {
		this.isReviewd = isReviewd;
	}

	public void setTotalReviewCount(int totalReviewCount) {
		this.totalReviewCount = totalReviewCount;
	}

	public int getTotalCommentCount() {
		return totalCommentCount;
	}

	public void setTotalCommentCount(int totalCommentCount) {
		this.totalCommentCount = totalCommentCount;
	}
	
}

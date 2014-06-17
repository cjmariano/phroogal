/**
 * 
 */
package com.phroogal.web.bean;



public class BrandPreviewBean{
	
	private String id;
	
	private String name;
	
	private String shortDescription;
		
	private String longDescription;
		
	private String[] tags;	
		
	private String profilePictureUrl;
	    
	private String url;	
	
	private int totalReviewCount;
	
	private int totalCommentCount;
	
	private boolean isReviewd;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String[] getTags() {
		return tags;
	}

	public void setTags(String[] tags) {
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

	public int getTotalReviewCount() {
		return totalReviewCount;
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

	public boolean isReviewd() {
		return isReviewd;
	}

	public void setReviewd(boolean isReviewd) {
		this.isReviewd = isReviewd;
	}
		
}

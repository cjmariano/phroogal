package com.phroogal.web.bean;



/**
 * Bean to hold documents of type Answers
 *
 */
public class ReviewBean extends PostBean<String> {

	private String brandRef;
	
	private String title;
	
	private String[] tags;
	
	private boolean isAnonymous;
    
    private String ratings;
    
    private VotesBean votes;
	
	//private List<CommentBean> comments;

	public String getBrandRef() {
		return brandRef;
	}

	public void setBrandRef(String brandRef) {
		this.brandRef = brandRef;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String[] getTags() {
		return tags;
	}

	public void setTags(String[] tags) {
		this.tags = tags;
	}

	public boolean isAnonymous() {
		return isAnonymous;
	}

	public void setAnonymous(boolean isAnonymous) {
		this.isAnonymous = isAnonymous;
	}

	public String getRatings() {
		return ratings;
	}

	public void setRatings(String ratings) {
		this.ratings = ratings;
	}

	public VotesBean getVotes() {
		return votes;
	}

	public void setVotes(VotesBean votes) {
		this.votes = votes;
	}

//	public List<CommentBean> getComments() {
//		return comments;
//	}
//
//	public void setComments(List<CommentBean> comments) {
//		this.comments = comments;
//	}
	
}

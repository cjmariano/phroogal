package com.phroogal.core.domain;

import java.io.Serializable;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Reviews posted by the community in response to a given brand
 *
 */
@Document(collection = ApplicationConstants.COLLECTION_REVIEWS)
public class Review extends Post implements Serializable, SortableByRank{
	
	private static final long serialVersionUID = -3607044335137992000L;
	
	@Indexed
	private ObjectId brandRef;
	
	private String title;
	
	private List<String> tags;
	
	private boolean isAnonymous;
    
    private String ratings;
    
    private Votes votes = new Votes();
    
    @Transient
	private double rank;
    
    @Override
	public PostType getPostType() {
		return PostType.REVIEW;
	}

    /**
	 * Convenience method to apply votes based on the action type supplied
	 * @param userId of the user doing the vote casting
	 * @param voteAction i.e. {@link VoteActionType.UPVOTE} or {@link VoteActionType.DOWNVOTE}
	 */
	public void applyVote(ObjectId userId, VoteActionType voteAction) {
		voteAction.applyTo(userId, getVotes());		
	}

	public ObjectId getBrandRef() {
		return brandRef;
	}

	public void setBrandRef(ObjectId brandRef) {
		this.brandRef = brandRef;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
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
	
	public Votes getVotes() {
		return votes;
	}

	public void setVotes(Votes votes) {
		this.votes = votes;
	}

	public double getRank() {
		return rank;
	}

	public void setRank(double rank) {
		this.rank = rank;
	}
}

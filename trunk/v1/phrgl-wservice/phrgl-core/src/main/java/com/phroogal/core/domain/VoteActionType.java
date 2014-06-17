package com.phroogal.core.domain;

import java.util.Set;

import org.bson.types.ObjectId;

import com.phroogal.core.exception.VoteActionNotSupportedException;

public enum VoteActionType {

	UPVOTE("upvote") {
		
		@Override
		public void applyTo(ObjectId userId, Votes votes) {
			Set<ObjectId> userUpvotes = votes.getUserUpvotes();
			Set<ObjectId> userDownvotes = votes.getUserDownvotes();
			if (userDownvotes.contains(userId)) {
				userDownvotes.remove(userId);
			} else if (! userUpvotes.contains(userId)) {
				userUpvotes.add(userId);
			}
			votes.setTotal(userUpvotes.size() - userDownvotes.size());
		}
	}, 
	
	DOWNVOTE("downvote") {
		
		@Override
		public void applyTo(ObjectId userId, Votes votes) {
			Set<ObjectId> userUpvotes = votes.getUserUpvotes();
			Set<ObjectId> userDownvotes = votes.getUserDownvotes();
			if (userUpvotes.contains(userId)) {
				userUpvotes.remove(userId);
			} else if (! userDownvotes.contains(userId)) {
				userDownvotes.add(userId);
			}
			int Total = userUpvotes.size() - userDownvotes.size();
			votes.setTotal(ensureNonNegative(Total));	
		}
	};

	private VoteActionType(String value) {
		this.value = value;
	}	
	
	private String value;
	
	public static VoteActionType get(String value) {
		value = value.toLowerCase();
		for (VoteActionType ratingType : VoteActionType.values()) {
			if (ratingType.getValue().equals(value)) {
				return ratingType;
			}
		}
		throw new VoteActionNotSupportedException();				
	}	
	
	public String getValue() {
		return value;
	}
	
	private static int ensureNonNegative(int Total){
		return Total < 0 ? 0 : Total;
	}	
	
	/**
	 * Applies the vote action type function to given argument
	 * @param userId of the user casting the vote
	 * @param currentVote the value where changes are to be applied to
	 */
	public abstract void applyTo(ObjectId userId, Votes vote);
	
}

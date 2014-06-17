package com.phroogal.core.domain;

public enum RatingActionType {

	UPVOTE("upvote"), DOWNVOTE("downvote");

	private RatingActionType(String value) {
		this.value = value;
	}	
	
	private String value;
	
	public static RatingActionType get(String value) {

		value = value.toLowerCase();
		for (RatingActionType ratingType : RatingActionType.values()) {
			if (ratingType.getValue().equals(value)) {
				return ratingType;
			}
		}
		return null;				
	}	
	
	public String getValue() {
		return value;
	}	
	
}

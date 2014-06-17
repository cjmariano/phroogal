package com.phroogal.core.social;

import com.phroogal.core.exception.SocialNetworkNotSupportedException;

public enum SocialNetworkType {
	
	FACEBOOK("facebook"), LINKEDIN("linkedin"), GOOGLE("google"), TWITTER("twitter");
	
	private SocialNetworkType(String id) {
		this.id = id;
	}
	
	private String id;

	public String getId() {
		return id;
	}
	
	public static SocialNetworkType get(String id) {
		for (SocialNetworkType socialNetworkType : SocialNetworkType.values()) {
			if (socialNetworkType.getId().equals(id)) {
				return socialNetworkType;
			}
		}
		throw new SocialNetworkNotSupportedException();
	}
}

package com.phroogal.web.bean;

import com.phroogal.core.exception.SocialNetworkNotSupportedException;

public enum SocialNetworkTypeBean {
	
	FACEBOOK("facebook"), LINKEDIN("linkedin"), GOOGLE("google"), TWITTER("twitter");
	
	private SocialNetworkTypeBean(String id) {
		this.id = id;
	}
	
	private String id;

	public String getId() {
		return id;
	}
	
	public static SocialNetworkTypeBean get(String id) {
		for (SocialNetworkTypeBean socialNetworkType : SocialNetworkTypeBean.values()) {
			if (socialNetworkType.getId().equals(id)) {
				return socialNetworkType;
			}
		}
		throw new SocialNetworkNotSupportedException();
	}
}

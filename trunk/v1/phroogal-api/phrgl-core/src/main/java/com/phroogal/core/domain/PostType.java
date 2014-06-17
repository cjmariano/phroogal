package com.phroogal.core.domain;

import com.phroogal.core.exception.PostTypeNotSupportedException;

public enum PostType {

	QUESTION ("question"), ANSWER("answer"), COMMENT("comment"), REPLY("reply"), REVIEW("review") ;
	
	private PostType(String value) {
		this.value = value;
	}
	
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public static PostType get(String value) {
		for (PostType postType : PostType.values()) {
			if (postType.getValue().equalsIgnoreCase(value)) {
				return postType;
			}
		}
		throw new PostTypeNotSupportedException();				
	}
}

package com.phroogal.web.bean;


/**
 * Bean to hold documents of type Reply.
 * @author Christopher Mariano
 *
 */
public class ReplyBean extends PostBean<String> {

	private String commentRef;

	public String getCommentRef() {
		return commentRef;
	}

	public void setCommentRef(String commentRef) {
		this.commentRef = commentRef;
	}
}

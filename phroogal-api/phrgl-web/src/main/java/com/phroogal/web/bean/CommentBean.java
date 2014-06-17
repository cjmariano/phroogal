package com.phroogal.web.bean;

import java.util.List;

/**
 * Bean to hold documents of type Comments.
 * @author Christopher Mariano
 *
 */
public class CommentBean extends PostBean<String> {

	private String answerRef;
	
	private List<ReplyBean> replies;

	public String getAnswerRef() {
		return answerRef;
	}

	public void setAnswerRef(String answerRef) {
		this.answerRef = answerRef;
	}

	public List<ReplyBean> getReplies() {
		return replies;
	}

	public void setReplies(List<ReplyBean> replies) {
		this.replies = replies;
	}
}

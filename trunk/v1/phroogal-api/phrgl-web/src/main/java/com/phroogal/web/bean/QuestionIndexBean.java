package com.phroogal.web.bean;

import com.phroogal.web.utility.LinkGenerator;


public class QuestionIndexBean {

	private String id;
	
	private long docId;

	private String title;
	
	private String content;
	
	private String postedByUser;
	
	private long totalViewCount;
	
	private int totalAnswerCount;
	
	private int totalCommentCount;
	
	private boolean isAnswered;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public long getDocId() {
		return docId;
	}

	public void setDocId(long docId) {
		this.docId = docId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPostedByUser() {
		return postedByUser;
	}

	public void setPostedByUser(String postedByUser) {
		this.postedByUser = postedByUser;
	}

	public long getTotalViewCount() {
		return totalViewCount;
	}

	public void setTotalViewCount(long totalViewCount) {
		this.totalViewCount = totalViewCount;
	}

	public int getTotalAnswerCount() {
		return totalAnswerCount;
	}

	public void setTotalAnswerCount(int totalAnswerCount) {
		this.totalAnswerCount = totalAnswerCount;
	}

	public int getTotalCommentCount() {
		return totalCommentCount;
	}

	public void setTotalCommentCount(int totalCommentCount) {
		this.totalCommentCount = totalCommentCount;
	}

	public boolean isAnswered() {
		return isAnswered;
	}

	public void setAnswered(boolean isAnswered) {
		this.isAnswered = isAnswered;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getLink() {
		return LinkGenerator.getQuestionLink(this.getDocId() , this.getTitle());
	}
}

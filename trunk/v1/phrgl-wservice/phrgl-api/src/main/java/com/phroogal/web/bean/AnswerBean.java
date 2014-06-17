package com.phroogal.web.bean;

import java.util.List;

/**
 * Bean to hold documents of type Answers
 * @author Christopher Mariano
 *
 */
public class AnswerBean extends PostBean<String> {

	private String questionRef;
	
	private String questionTitle;
	
	private long questionDocId;
	
	private int totalCommentCount;
	
	private VotesBean votes;
	
	private List<CommentBean> comments;

	private List<String> answerSortType;

	public String getQuestionRef() {
		return questionRef;
	}

	public void setQuestionRef(String questionRef) {
		this.questionRef = questionRef;
	}
	
	public VotesBean getVotes() {
		return votes;
	}

	public void setVotes(VotesBean votes) {
		this.votes = votes;
	}

	public List<CommentBean> getComments() {
		return comments;
	}

	public void setComments(List<CommentBean> comments) {
		this.comments = comments;
	}

	public List<String> getAnswerSortType() {
		return answerSortType;
	}

	public void setAnswerSortType(List<String> answerSortType) {
		this.answerSortType = answerSortType;
	}

	public String getQuestionTitle() {
		return questionTitle;
	}

	public void setQuestionTitle(String questionTitle) {
		this.questionTitle = questionTitle;
	}

	public long getQuestionDocId() {
		return questionDocId;
	}

	public void setQuestionDocId(long questionDocId) {
		this.questionDocId = questionDocId;
	}

	public int getTotalCommentCount() {
		return totalCommentCount;
	}

	public void setTotalCommentCount(int totalCommentCount) {
		this.totalCommentCount = totalCommentCount;
	}
}

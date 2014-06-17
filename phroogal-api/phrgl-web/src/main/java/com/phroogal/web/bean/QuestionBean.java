package com.phroogal.web.bean;

import java.util.List;

import com.phroogal.web.utility.LinkGenerator;

/**
 * Bean to hold documents of type Question
 * @author Christopher Mariano
 *
 */
public class QuestionBean extends PostBean<String> {

	private long docId;
	
	private String title;
	
	private String[] tags;
	
	private boolean isAnswered;
	
	private boolean isAnonymous;
	
	private List<AnswerBean> answers;
	
	private long totalViewCount;
	
	private int totalAnswerCount;
	
	private int totalCommentCount;
	
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
	
	public boolean isAnswered() {
		return isAnswered;
	}

	public void setAnswered(boolean isAnswered) {
		this.isAnswered = isAnswered;
	}
	
	public boolean isAnonymous() {
		return isAnonymous;
	}

	public void setAnonymous(boolean isAnonymous) {
		this.isAnonymous = isAnonymous;
	}

	public List<AnswerBean> getAnswers() {
		return answers;
	}

	public void setAnswers(List<AnswerBean> answers) {
		this.answers = answers;
	}

	public String[] getTags() {
		return tags;
	}

	public void setTags(String[] tags) {
		this.tags = tags;
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
	
	public String getLink() {
		return LinkGenerator.getQuestionLink(this.getDocId() , this.getTitle());
	}
}

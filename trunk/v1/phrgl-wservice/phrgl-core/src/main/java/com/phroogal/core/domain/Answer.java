package com.phroogal.core.domain;

import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.phroogal.core.rule.Fact;
import com.phroogal.core.utility.CollectionUtil;
import com.phroogal.core.valueobjects.PropertyBag;

/**
 * Answers posted by the community in response to a given question
 * @author Christopher Mariano
 *
 */
@Document(collection = ApplicationConstants.COLLECTION_ANSWERS)
public class Answer extends Post implements PropertyModifiable, QuestionThreadPost, Fact, SortableByRank {
	
	private static final long serialVersionUID = -3607044335137992814L;
	
	private Votes votes = new Votes();
	
	@Indexed
	private ObjectId questionRef;
	
	private String questionTitle;
	
	private long questionDocId;
	
	private int totalCommentCount;
	
	private List<Comment> comments;
	
	@Transient
	private double rank;
	
	@Transient
	private List<AnswerSortType> answerSortType;

	@Override
	public void partialUpdate(PropertyBag propertyBag) throws Exception {
		PropertyUtils.setProperty(this, propertyBag.getProperty(), propertyBag.getValue());
	}
	
	@Override
	public PostType getPostType() {
		return PostType.ANSWER;
	}
	
	/**
	 * Convenience method to apply votes based on the action type supplied
	 * @param userId of the user doing the vote casting
	 * @param voteAction i.e. {@link VoteActionType.UPVOTE} or {@link VoteActionType.DOWNVOTE}
	 */
	public void applyVote(ObjectId userId, VoteActionType voteAction) {
		voteAction.applyTo(userId, getVotes());		
	}
	
	/**
	 * Convenience method for adding an answer sort type to this answer
	 * @param answerSortType to be added
	 */
	public void addAnswerSortType(AnswerSortType answerSortType) {
		getAnswerSortType().add(answerSortType);
	}
	
	public Votes getVotes() {
		return votes;
	}

	public void setVotes(Votes votes) {
		this.votes = votes;
	}
	
	public ObjectId getQuestionRef() {
		return questionRef;
	}

	public void setQuestionRef(ObjectId questionRef) {
		this.questionRef = questionRef;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public double getRank() {
		return rank;
	}

	public void setRank(double rank) {
		this.rank = rank;
	}

	@Override
	public ObjectId getRootQuestionId() {
		return this.questionRef;
	}

	public List<AnswerSortType> getAnswerSortType() {
		if (answerSortType == null) {
			answerSortType = CollectionUtil.arrayList();
		}
		return answerSortType;
	}

	public void setAnswerSortType(List<AnswerSortType> answerSortType) {
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

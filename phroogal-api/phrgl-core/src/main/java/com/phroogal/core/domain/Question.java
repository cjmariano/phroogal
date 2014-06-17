package com.phroogal.core.domain;

import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.phroogal.core.rule.Fact;
import com.phroogal.core.search.index.QuestionIndex;
import com.phroogal.core.utility.CollectionUtil;
import com.phroogal.core.valueobjects.PropertyBag;

/**
 * Question posted by users
 * @author Christopher Mariano
 *
 */
@Document(collection = ApplicationConstants.COLLECTION_QUESTIONS)
public class Question extends Post implements SearchableIndex<QuestionIndex>, PropertyModifiable, QuestionThreadPost, Fact {

	private static final long serialVersionUID = -2919059142777330126L;
	
	private long docId;
	
	private String title;
    
    private List<String> tags;
    
    private boolean isAnswered;
    
    private boolean isAnonymous;
    
	private List<Answer> answers;
	
	@Transient
	private Answer topAnswer;
	
	private long totalViewCount;
	
	private int totalAnswerCount;
	
	private int totalCommentCount;
	
	private double trendingRank;
	
	@Override
	public void partialUpdate(PropertyBag propertyBag) throws Exception {
		PropertyUtils.setProperty(this, propertyBag.getProperty(), propertyBag.getValue());
	}
	
	@Override
	public PostType getPostType() {
		return PostType.QUESTION;
	}
	
	@Override
	public ObjectId getRootQuestionId() {
		return getId();
	}
	
	/**
	 * Convenience method for incrementing total views count for this question
	 */
	public void incrementTotalViewsCount() {
		synchronized ((Long)totalViewCount) {
			totalViewCount ++;	
		}
	}
	
	/**
	 * Adds the given tag to this question
	 * @param tag to be added
	 */
	public void addTag(String tag) {
		this.getTags().add(tag);
	}
	
	/**
	 * Removes the given tag for this question
	 * @param tag to be removed
	 */
	public void removeTag(String tag) {
		this.getTags().remove(tag);
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

	public List<String> getTags() {
		if (tags == null) {
			tags = CollectionUtil.arrayList();
		}
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
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
	
	public List<Answer> getAnswers() {
		return answers;
	}

	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}

	public Answer getTopAnswer() {
		return topAnswer;
	}

	public void setTopAnswer(Answer topAnswer) {
		this.topAnswer = topAnswer;
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

	public double getTrendingRank() {
		return trendingRank;
	}

	public void setTrendingRank(double trendingRank) {
		this.trendingRank = trendingRank;
	}
	
	@Override
	public QuestionIndex getIndex() {
		QuestionIndex index = new QuestionIndex();
		index.setId(getId().toString());
		index.setDocId(getDocId());
		index.setTitle(getTitle());
		index.setContent(getContent());
		index.setTags(getTags());
		index.setAnswered(isAnswered());
		index.setTotalViewCount(getTotalViewCount());
		index.setTotalAnswerCount(getTotalAnswerCount());
		index.setTotalCommentCount(getTotalCommentCount());
		index.setRank(getTrendingRank());
		index.setPostedByUser(resolvePostByName());
		index.setCreatedOn(getCreatedOn());
		return index;
	}

	private String resolvePostByName() {
		if (getPostBy() != null) {
			return getPostBy().getUserCompleteName();	
		}
		return StringUtils.EMPTY;
	}
}

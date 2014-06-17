/**
 * 
 */
package com.phroogal.core.search.index;

import java.util.List;

import org.apache.solr.client.solrj.beans.Field;
import org.joda.time.DateTime;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.solr.core.mapping.Indexed;

import com.phroogal.core.domain.Persistent;
import com.phroogal.core.domain.Question;
import com.phroogal.core.domain.SortableByRank;
import com.phroogal.core.rule.Fact;

/**
 * Holds information on {@link Question} as indexed data to be used for searching questions.
 * @author Christopher Mariano
 *
 */
@Document(collection = "idx_questions")
public class QuestionIndex extends Index  implements Persistent<String>, Fact, SortableByRank {
	
	@Field
	private String title;
	
	@Field
	private long docId;
	
	@Field
	private String content;
	
	@Field
	private List<String> tags;
	
	@Field
	private long totalViewCount;
	
	@Field
	private int totalAnswerCount;
	
	@Field
	private int totalCommentCount;
	
	@Field
	private boolean isAnswered;
	
	@Field
	private DateTime createdOn;
	
	@Field
	private String postedByUser;
	
	@Field
	@Indexed
	private double rank;
	
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
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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

	public DateTime getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(DateTime createdOn) {
		this.createdOn = createdOn;
	}

	public String getPostedByUser() {
		return postedByUser;
	}

	public void setPostedByUser(String postedByUser) {
		this.postedByUser = postedByUser;
	}

	public double getRank() {
		return rank;
	}

	public void setRank(double rank) {
		this.rank = rank;
	}
}

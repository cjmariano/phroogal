/**
 * 
 */
package com.phroogal.core.domain;

import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.phroogal.core.rule.Fact;

/**
 * Domain representation for the tags that are used to bookmark resources
 * @author Christopher Mariano
 *
 */
@Document(collection = ApplicationConstants.COLLECTION_TAGS)
public class Tag implements Persistent<ObjectId>, SortableByRank, Fact {
	
	@Id
	private ObjectId id;
	
	@Indexed
	private String name;
	
	/**
	 * Holds the total number of questions that are tagged with this tag instance
	 */
	private long totalNumQuestionsTagged;
	
	/**
	 * Holds the total number of views for the questions that are tagged with this tag instance
	 */
	private long totalNumViewsForQuestionsTagged;
	
	private double rank;
	
	@Override
	public ObjectId getId() {
		return id;
	}

	@Override
	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = StringUtils.strip(name);
	}

	public long getTotalNumQuestionsTagged() {
		return totalNumQuestionsTagged;
	}

	public void setTotalNumQuestionsTagged(long totalNumQuestionsTagged) {
		this.totalNumQuestionsTagged = totalNumQuestionsTagged;
	}

	public long getTotalNumViewsForQuestionsTagged() {
		return totalNumViewsForQuestionsTagged;
	}

	public void setTotalNumViewsForQuestionsTagged(long totalNumViewsForQuestionsTagged) {
		this.totalNumViewsForQuestionsTagged = totalNumViewsForQuestionsTagged;
	}

	public double getRank() {
		return rank;
	}

	public void setRank(double rank) {
		this.rank = rank;
	}
}

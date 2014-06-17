package com.phroogal.core.domain;

import java.io.Serializable;

import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Domain representation for any post created by the user (i.e. questions, answers)
 * @author Christopher Mariano
 *
 */
@Document
public abstract class Post implements Persistent<ObjectId>, Serializable {

	public abstract PostType getPostType();
	
	private static final long serialVersionUID = 4090836812396083760L;
	
	@Id
	private ObjectId id;
	
	private String content;
	
	private ObjectId postById;
	
	@DBRef
	private User postBy;
	
	@CreatedDate
	private DateTime createdOn;
	
	@LastModifiedDate
	private DateTime modifiedOn;
	
	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public ObjectId getPostById() {
		return postById;
	}

	public void setPostById(ObjectId postById) {
		this.postById = postById;
	}

	public User getPostBy() {
		return postBy;
	}

	public void setPostBy(User postBy) {
		this.postBy = postBy;
		if (postBy != null) {
			this.setPostById(postBy.getId());	
		}
	}

	public DateTime getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(DateTime createdOn) {
		this.createdOn = createdOn;
	}

	public DateTime getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(DateTime modifiedOn) {
		this.modifiedOn = modifiedOn;
	}
}

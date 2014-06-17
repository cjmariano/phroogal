package com.phroogal.core.domain;

import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Posts that are flagged by a user
 * @author Christopher Mariano
 *
 */
@Document(collection = ApplicationConstants.COLLECTION_FLAG_NOTIFICATION_REQ)
public class FlagNotificationRequest implements Persistent<ObjectId> {

	@Id
	private ObjectId id;
	
	private ObjectId refId;
	
	private PostType type;
	
	private String content;
	
	private FlagNotificationStatusType status = FlagNotificationStatusType.ACTIVE;
	
	@DBRef
	private User postBy;
	
	@DBRef
	@CreatedBy
	private User flaggedBy;
	
	@CreatedDate
	private DateTime createdOn;
	
	@Override
	public ObjectId getId() {
		return id;
	}

	@Override
	public void setId(ObjectId id) {
		this.id = id;
	}

	public ObjectId getRefId() {
		return refId;
	}

	public void setRefId(ObjectId refId) {
		this.refId = refId;
	}

	public PostType getType() {
		return type;
	}

	public void setType(PostType type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public DateTime getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(DateTime createdOn) {
		this.createdOn = createdOn;
	}

	public User getFlaggedBy() {
		return flaggedBy;
	}

	public void setFlaggedBy(User flaggedBy) {
		this.flaggedBy = flaggedBy;
	}

	public User getPostBy() {
		return postBy;
	}

	public void setPostBy(User postBy) {
		this.postBy = postBy;
	}

	public FlagNotificationStatusType getStatus() {
		return status;
	}

	public void setStatus(FlagNotificationStatusType status) {
		this.status = status;
	}
}

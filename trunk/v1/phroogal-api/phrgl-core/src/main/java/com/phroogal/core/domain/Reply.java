package com.phroogal.core.domain;

import org.apache.commons.beanutils.PropertyUtils;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.phroogal.core.valueobjects.PropertyBag;

/**
 * Replies in relation to comments made to an answer
 * @author Christopher Mariano
 *
 */
@Document(collection = ApplicationConstants.COLLECTION_REPLIES)
public class Reply extends Post implements PropertyModifiable, QuestionThreadPost {

	private static final long serialVersionUID = 2195066506246540890L;
	
	@Indexed
	private ObjectId commentRef;
	
	private ObjectId rootQuestionId;
	
	@Override
	public void partialUpdate(PropertyBag propertyBag) throws Exception {
		PropertyUtils.setProperty(this, propertyBag.getProperty(), propertyBag.getValue());
	}
	
	@Override
	public PostType getPostType() {
		return PostType.REPLY;
	}
	
	public ObjectId getCommentRef() {
		return commentRef;
	}

	public void setCommentRef(ObjectId commentRef) {
		this.commentRef = commentRef;
	}

	@Override
	public ObjectId getRootQuestionId() {
		return rootQuestionId;
	}

	public void setRootQuestionId(ObjectId rootQuestionId) {
		this.rootQuestionId = rootQuestionId;
	}
}

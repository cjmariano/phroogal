package com.phroogal.core.domain;

import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.phroogal.core.valueobjects.PropertyBag;

/**
 * Comments that are made to an answer posted to a question.
 * @author Christopher Mariano
 *
 */
@Document(collection = ApplicationConstants.COLLECTION_COMMENTS)
public class Comment extends Post implements PropertyModifiable, QuestionThreadPost {

	private static final long serialVersionUID = 1502543673146357770L;
	
	@Indexed
	private ObjectId answerRef;
	
	private ObjectId rootQuestionId;
	
	private List<Reply> replies;
	
	@Override
	public void partialUpdate(PropertyBag propertyBag) throws Exception {
		PropertyUtils.setProperty(this, propertyBag.getProperty(), propertyBag.getValue());
	}
	
	@Override
	public PostType getPostType() {
		return PostType.COMMENT;
	}
	
	public ObjectId getAnswerRef() {
		return answerRef;
	}

	public void setAnswerRef(ObjectId answerRef) {
		this.answerRef = answerRef;
	}

	public List<Reply> getReplies() {
		return replies;
	}

	public void setReplies(List<Reply> replies) {
		this.replies = replies;
	}

	@Override
	public ObjectId getRootQuestionId() {
		return rootQuestionId;
	}

	public void setRootQuestionId(ObjectId rootQuestionId) {
		this.rootQuestionId = rootQuestionId;
	}
}

/**
 * 
 */
package com.phroogal.core.service;

import java.util.List;

import org.bson.types.ObjectId;

import com.phroogal.core.domain.Comment;

/**
 * Service for {@link Comment} functions
 * @author Christopher Mariano
 *
 */
public interface CommentService extends Service<Comment, ObjectId> {

	/**
	 * Returns a list of Comments given the answer id.
	 * @param questionRef
	 * @return
	 */
	public List<Comment> getByAnswerRef (ObjectId answerRef);
}

/**
 * 
 */
package com.phroogal.core.repository;

import java.util.List;

import org.bson.types.ObjectId;

import com.phroogal.core.domain.Comment;

/**
 * Repository for {@link Comment} data
 * 
 * @author Christopher Mariano
 * 
 */
public interface CommentRepository extends BaseMongoRepository<Comment>{
	
	/**
	 * Returns a list of Comments given the answer id.
	 * @param questionRef
	 * @return
	 */
	public List<Comment> findByAnswerRef (ObjectId answerRef);
}

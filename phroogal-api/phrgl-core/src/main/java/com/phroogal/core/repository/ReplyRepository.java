/**
 * 
 */
package com.phroogal.core.repository;

import java.util.List;

import org.bson.types.ObjectId;

import com.phroogal.core.domain.Reply;

/**
 * Repository for {@link Reply} data
 * 
 * @author Christopher Mariano
 * 
 */
public interface ReplyRepository extends BaseMongoRepository<Reply> {
	
	/**
	 * Returns a list of Replies given the comment id.
	 * @param questionRef
	 * @return
	 */
	public List<Reply> findByCommentRef (ObjectId commentRef);
}

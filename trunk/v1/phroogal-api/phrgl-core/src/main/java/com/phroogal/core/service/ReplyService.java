/**
 * 
 */
package com.phroogal.core.service;

import java.util.List;

import org.bson.types.ObjectId;

import com.phroogal.core.domain.Reply;
/**
 * Service for {@link Reply} functions
 * @author Christopher Mariano
 *
 */
public interface ReplyService extends Service<Reply, ObjectId> {

	/**
	 * Returns a list of Replies given the comment id.
	 * @param questionRef
	 * @return
	 */
	public List<Reply> getByCommentRef (ObjectId commentRef);
}

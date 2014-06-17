/**
 * 
 */
package com.phroogal.core.domain;

import org.bson.types.ObjectId;

/**
 * Indicates that this post belongs to a question thread (i.e question, answer, comments, reply)
 * @author Christopher Mariano
 *
 */
public interface QuestionThreadPost {

	/**
	 * Reference to the question id where this post is attached to.
	 * @return the question id
	 */
	public ObjectId getRootQuestionId();
	
}

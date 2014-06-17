/**
 * 
 */
package com.phroogal.core.service;

import com.phroogal.core.domain.QuestionThreadPost;

/**
 * Service for {@link QuestionThreadPost} functions
 * @author Christopher Mariano
 *
 */
public interface QuestionThreadPostService {

	/**
	 * Recreates a question thread of its associated answers, comments and replies and embeds them unto the
	 * {@link QuestionThreadPost} document
	 * @param questionService for updating the question
	 * @param post - updated question post
	 */
	public void refreshQuestionAnswerThread(QuestionThreadPost post);
}

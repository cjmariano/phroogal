/**
 * 
 */
package com.phroogal.core.service;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;

import com.phroogal.core.domain.Answer;
import com.phroogal.core.domain.FlagNotificationRequest;

/**
 * Service for {@link Answer} functions
 * @author Christopher Mariano
 *
 */
public interface AnswerService extends Service<Answer, ObjectId> {

	/**
	 * Returns a list of Answers given a question id.
	 * @param questionRef
	 * @return
	 */
	public List<Answer> getByQuestionRef (ObjectId questionRef);
	
	/**
	 * Returns a {@link Answer} that is posted by user of the given id  
	 * @param userId - is the user id
	 * @param pageAt - states to return results starting from this page (number).
	 * @param pageSize - number of results from this page.
	 * @return a list of matching {@link Answer} document
	 */
	public Page<Answer> getByUserId(ObjectId userId, int pageAt, int pageSize);
	
	/**
	 * Retrieves answers sorted from most recent down to the oldest.
	 * @param pageAt - states to return results starting from this page (number).
	 * @param pageSize - number of results from this page.
	 * @return a paginated list of recent answers.
	 */
	public Page<Answer> getRecentAnswers(int pageAt, int pageSize);
	
	/**
	 * Retrieves flagged answers.
	 * @param pageAt - states to return results starting from this page (number).
	 * @param pageSize - number of results from this page.
	 * @return list of paginated flagged answers.
	 */
	public Page<Answer> getFlaggedAnswers(int pageAt, int pageSize);
	
	/**
	 * Implement Answer Hierarchy rules on the given collection of answers
	 * @param answers to be sorted
	 */
	public void sortByAnswerHierarchyRules(List<Answer> answers);
	
	/**
	 * Flags the given answer.
	 * @param answer to be flagged
	 * @return the notification request created.
	 */
	public FlagNotificationRequest flagAnswer(Answer answer);
}

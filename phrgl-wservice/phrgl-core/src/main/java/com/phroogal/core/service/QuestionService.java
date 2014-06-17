/**
 * 
 */
package com.phroogal.core.service;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrServerException;
import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;

import com.phroogal.core.domain.FlagNotificationRequest;
import com.phroogal.core.domain.Question;
import com.phroogal.core.exception.InvalidQuestionDocumentIdException;
import com.phroogal.core.search.index.QuestionIndex;

/**
 * Service for {@link Question} functions
 * @author Christopher Mariano
 *
 */
public interface QuestionService extends Service<Question, ObjectId> {

	/**
	 * Returns a {@link Question} that matches the given document id  
	 * @param docId - document id
	 * @return matching {@link Question} document
	 */
	public Question getByDocId(long docId) throws InvalidQuestionDocumentIdException;
	
	/**
	 * Returns a {@link Question} that is posted by user of the given id  
	 * @param userId - is the user id
	 * @param pageAt - states to return results starting from this page (number).
	 * @param pageSize - number of results from this page.
	 * @return a paginated list of matching {@link Question} document
	 */
	public Page<Question> getByUserId(ObjectId userId, int pageAt, int pageSize);
	
	/**
	 * Retrieves questions sorted from most recent down to the oldest.
	 * @param pageAt - states to return results starting from this page (number).
	 * @param pageSize - number of results from this page.
	 * @return paginated list of recent questions.
	 */
	public Page<QuestionIndex> getRecentQuestions(int pageAt, int pageSize);
	
	/**
	 * Retrieves flagged question.
	 * @param pageAt - states to return results starting from this page (number).
	 * @param pageSize - number of results from this page.
	 * @return paginated list of flagged questions.
	 */
	public Page<QuestionIndex> getFlaggedQuestions(int pageAt, int pageSize);
	
	/**
	 * Retrieves a list of questions posted by users belonging to a user's social network
	 * @param pageAt - states to return results starting from this page (number).
	 * @param pageSize - number of results from this page.
	 * @return paginated list of social questions.
	 */
	public Page<Question> getSocialQuestions(ObjectId userId, int pageAt, int pageSize);
	
	/**
	 * Retrieves questions that have not been answered yet.
	 * @param pageAt - states to return results starting from this page (number).
	 * @param pageSize - number of results from this page.
	 * @return paginated list of unanswered questions.
	 */
	public Page<QuestionIndex> getUnansweredQuestions(int pageAt, int pageSize);
	
	/**
	 * Retrieves questions that have not been answered yet, and sorted from most recent down to oldest.
	 * @param pageAt - states to return results starting from this page (number).
	 * @param pageSize - number of results from this page.
	 * @return paginated list of unanswered questions.
	 */
	public Page<QuestionIndex> getRecentUnansweredQuestions(int pageAt, int pageSize);
	
	/**
	 * Returns a list of {@link QuestionIndex} that is filtered based on date ranges
	 * @param from is the lowerbound date
	 * @param to is the upperbound date
	 * @param pageAt - states to return results starting from this page (number).
	 * @param pageSize - number of results from this page.
	 * @return a paginated list of QuestionIndex that matches the date ranges
	 */
	public Page<QuestionIndex> getQuestionsByDateRange(DateTime from, DateTime to, int pageAt, int pageSize);
	
	/**
	 * Returns a list of {@link QuestionIndex} sorted by trending topics between the configured number of days
	 * @param pageAt - states to return results starting from this page (number).
	 * @param pageSize - number of results from this page.
	 * @return paginated list of Trending QuestionIndex
	 */
	public Page<QuestionIndex> getTrendingQuestions(int pageAt, int pageSize);
	
	/**
	 * Returns a list of {@link QuestionIndex} sorted by trending topics
	 * @param pageAt - states to return results starting from this page (number).
	 * @param pageSize - number of results from this page.
	 * @param excludeDocId - doc id to be excluded in the results
	 * @return paginated list of Trending QuestionIndex
	 */
	public Page<QuestionIndex> getTrendingQuestions(int pageAt, int pageSize, long excludeDocId);
	
	/**
	 * Returns a list of {@link QuestionIndex} sorted by trending topics by the number of days from now
	 * @param pageAt - states to return results starting from this page (number).
	 * @param pageSize - number of results from this page.
	 * @param numberOfDays - number of days from current date, which will determine the date range to match 
	 * the questions on
	 * @return paginated list of Trending QuestionIndex that matches the date ranges
	 */
	public Page<QuestionIndex> getTrendingQuestions(int pageAt, int pageSize, int numberOfDays);
	
	/**
	 * Returns a list of {@link QuestionIndex} sorted by most views sorted by the number of days from now
	 * @param pageAt - states to return results starting from this page (number).
	 * @param pageSize - number of results from this page.
	 * @param numberOfDays - number of days from current date, which will determine the date range to match 
	 * the questions on
	 * @return paginated list of most views QuestionIndex that matches the date ranges
	 */
	public Page<QuestionIndex> getMostViewedQuestions(int pageAt, int pageSize, int numberOfDays);
	
	/**
	 * Returns a list of question preview that matches the title from a given keyword. 
	 * Also, Note that the top answer follows what is on the Answer Hierarchy specification
	 * @param keyword - used to match a question's title
	 * @param pageAt - states to return results starting from this page (number).
	 * @param pageSize - number of results from this page.
	 * @return a paginated list of {@link QuestionIndex} matching the keyword.
	 */
	public Page<QuestionIndex> searchQuestion(String keyword, int pageAt, int pageSize);
	
	/**
	 * Performs search that will filter a question by tags.
	 * @param tag to be used to filter questions
	 * @param pageAt - states to return results starting from this page (number).
	 * @param pageSize - number of results from this page.
	 * @return paginated list of {@link QuestionIndex} that matches the given tag.
	 */
	public Page<QuestionIndex> searchQuestionByTag(String tag, int pageAt, int pageSize);
	
	/**
	 * Returns an indexed Question by title
	 * @param keyword - used to match a question's title
	 * @param pageAt - states to return results starting from this page (number).
	 * @param pageSize - number of results from this page.
	 * @return a paginated list of {@link QuestionIndex} matching the keyword.
	 */
	public Page<QuestionIndex> searchIndexedQuestionByTitle(String keyword, int pageAt, int pageSize);
	
	/**
	 * Performs search for similar or related questions.
	 * @param keyword to be used to filter questions
	 * @param pageAt - states to return results starting from this page (number).
	 * @param pageSize - number of results from this page.
	 * @return paginated list of similar questions that matches the given keyword.
	 */
	public Page<QuestionIndex> searchSimilarIndexedQuestion(String keyword, int pageAt, int pageSize);
	
	/**
	 * Performs search for similar or related questions.
	 * @param keyword to be used to filter questions
	 * @param excludeDocId - doc id to be excluded in the results
	 * @param pageAt - states to return results starting from this page (number).
	 * @param pageSize - number of results from this page.
	 * @return paginated list of similar questions that matches the given keyword.
	 */
	public Page<QuestionIndex> searchSimilarIndexedQuestion(String keyword, long excludeDocId, int pageAt, int pageSize);
	
	/**
	 * Flags the given question.
	 * @param question to be flagged
	 * @return the notification request created.
	 */
	public FlagNotificationRequest flagQuestion(Question question);
	
	/**
	 * Increments the total views of this question 
	 * @param docId - document id of the question
	 */
	public void incrementTotalViewsCount(long docId);
	
	/**
	 * Re-calculates the rank of a question index based on trends 
	 * @param question whose rank is to be refreshed
	 */
	public void recalculateTrendingRank(Question question);
	
	/**
	 * Performs reindexing of Solr data
	 * @throws IOException 
	 * @throws SolrServerException 
	 */
	public void reIndex() throws SolrServerException, IOException;
}

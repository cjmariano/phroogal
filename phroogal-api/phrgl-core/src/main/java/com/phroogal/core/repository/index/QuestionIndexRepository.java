/**
 * 
 */
package com.phroogal.core.repository.index;

import static org.springframework.data.solr.core.query.Query.Operator.AND;
import static org.springframework.data.solr.core.query.Query.Operator.OR;

import java.util.List;

import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.repository.Query;
import org.springframework.stereotype.Repository;

import com.phroogal.core.domain.Question;
import com.phroogal.core.search.index.QuestionIndex;

/**
 * Repository for {@link Question} data
 * 
 * @author Christopher Mariano
 * 
 */
@Repository("questionIndexRepository")
public interface QuestionIndexRepository extends BaseSolrIndexRepository<QuestionIndex> {
	
	/**
	 * Returns a paginated list of question index that matches the given list of question Ids
	 * @param ids - list of String representing the IDs to filter results
	 * @param pageable - page options
	 * @return paginated list of QuestionIndex that matches the given list of IDs
	 */
	@Query(value="id:(?0)")
	public Page<QuestionIndex> findAll(List<String> ids, Pageable pageable);
	
	/**
	 * Returns a list of {@link QuestionIndex} that matches the keyword
	 * @param keyword to be used for searching
	 * @param pageable - page options.
	 * @return paginated list of QuestionIndex that matches the keyword
	 */
	@Query(value="title:?0 OR stemmerTitle:?0", defaultOperator=AND)
	public Page<QuestionIndex> searchByTitle(String keyword, Pageable pageable);
	
	/**
	 * Returns a list of {@link QuestionIndex} that is filtered by the given tag
	 * @param tag to be used to filter the questions 
	 * @return list of QuestionIndex that matches the tag
	 */
	@Query(value="tags:(\"?0\")")
	public List<QuestionIndex> searchByTags(String tag);
	
	/**
	 * Returns a list of {@link QuestionIndex} that is filtered by a given tags list.
	 * @param list of tags to be used to filter the questions 
	 * @return list of QuestionIndex that matches the tag
	 */
	@Query(value="tags:?0")
	public List<QuestionIndex> searchByTags(List<String> tags);
	
	/**
	 * Returns a list of {@link QuestionIndex} that is filtered by the given tag.
	 * Overload of the same function to provide paging options
	 * @param tag to be used to filter the questions 
	 * @param pageable - page options.
	 * @return paginated list of QuestionIndex that matches the tag
	 */
	@Query(value="tags:(\"?0\")")
	public Page<QuestionIndex> searchByTags(String tag, Pageable pageable);
	
	/**
	 * Returns a list of {@link QuestionIndex} that is filtered by a given tags list.
	 * Overload of the same function to provide paging options
	 * @param list of tags to be used to filter the questions 
	 * @param pageable - page options.
	 * @return a paginated list of QuestionIndex that matches the tag
	 */
	@Query(value="tags:?0")
	public Page<QuestionIndex> searchByTags(List<String> tags, Pageable pageable);
	
	/**
	 * Performs a search for related questions
	 * @param keyword - to match for related questions
	 * @param pageable - page options
	 * @return paginated list of QuestionIndex that are related questions
	 */
	@Query(value="title:?0 OR stemmerTitle:?0", defaultOperator=OR, requestHandler="mlt")
	public Page<QuestionIndex> searchBySimilarTitle(String keyword, Pageable pageable);
	
	/**
	 * Performs a search for related questions
	 * @param keyword - to match for related questions
	 * @param excludeDocId - excludes question with this document id
	 * @param pageable - page options
	 * @return paginated list of QuestionIndex that are related questions
	 */
	@Query(value="(title:?0 OR stemmerTitle:?0) AND -docId:?1", defaultOperator=OR, requestHandler="mlt")
	public Page<QuestionIndex> searchBySimilarTitleAndNotDocId(String keyword, long excludeDocId, Pageable pageable);
	
	/**
	 * Returns a list of {@link QuestionIndex} that is filtered based if it is answered or not
	 * @param isAnswered , true if list of questions to be returned are answered, false otherwise
	 * @param pageable - provides option for pagination 
	 * @return paginated list of QuestionIndex
	 */
	public Page<QuestionIndex> findByIsAnswered(boolean isAnswered, Pageable pageable);
	
	/**
	 * Returns a list of {@link QuestionIndex} that is filtered based on date ranges
	 * @param from - is the lowerbound date
	 * @param to - is the upperbound date
	 * @param pageable - provides option for pagination
	 * @return a paginated list of QuestionIndex that matches the date ranges
	 */
	//TODO: Excluding "Reviews" was just a workaround. remove query annotation to revert -cjm
	@Query(value="createdOn:[?0 TO ?1] AND -tags: \"Reviews\" AND -tags: \"Credit Union Resources\"", defaultOperator=AND)
	public Page<QuestionIndex> findByCreatedOnBetween(DateTime from, DateTime to, Pageable pageable);
	
	/**
	 * Returns a list of {@link QuestionIndex} that is filtered based on date ranges
	 * @param from - is the lowerbound date
	 * @param to - is the upperbound date
	 * @param isAnswered - true, if questions to be returned should have been answered; false otherwise  
	 * @param pageable - provides option for pagination
	 * @return paginated list of QuestionIndex that matches the date ranges
	 */
	//TODO: Excluding "Reviews" was just a workaround. remove query annotation to revert -cjm
	@Query(value="createdOn:[?0 TO ?1] AND isAnswered:?2 AND -tags: \"Reviews\" AND -tags: \"Credit Union Resources\"", defaultOperator=AND)
	public Page<QuestionIndex> findByCreatedOnBetweenAndIsAnswered(DateTime from, DateTime to, boolean isAnswered, Pageable pageable);
	
	/**
	 * Returns a list of {@link QuestionIndex} that is filtered based on date ranges
	 * @param from - is the lowerbound date
	 * @param to - is the upperbound date
	 * @param isAnswered - true, if questions to be returned should have been answered; false otherwise  
	 * @param excludeDocId - excludes question with this document id
	 * @param pageable - provides option for pagination
	 * @return paginated list of QuestionIndex that matches the date ranges
	 */
	//TODO: Excluding "Reviews" was just a workaround. revert to the commented code after reviews are created -cjm
	//@Query(value="createdOn:[?0 TO ?1] AND isAnswered:?2 AND -docId:?3", defaultOperator=AND)
	@Query(value="createdOn:[?0 TO ?1] AND isAnswered:?2 AND -docId:?3 AND -tags: \"Reviews\" AND -tags: \"Credit Union Resources\"", defaultOperator=AND)
	public Page<QuestionIndex> findByCreatedOnBetweenAndIsAnsweredAndNotDocId(DateTime from, DateTime to, boolean isAnswered, long excludeDocId, Pageable pageable);
	
}

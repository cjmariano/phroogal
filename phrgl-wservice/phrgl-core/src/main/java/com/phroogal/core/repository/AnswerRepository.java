/**
 * 
 */
package com.phroogal.core.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;

import com.phroogal.core.domain.Answer;

/**
 * Repository for {@link Answer} data
 * 
 * @author Christopher Mariano
 * 
 */
public interface AnswerRepository extends BaseMongoRepository<Answer>{
	
	/**
	 * Retrieves a list of answers by user who posted it
	 * @param id is the user id
	 * @param pageable - page options
	 * @return matching answers
	 */
	@Query("{ 'id': {$in: ?0} }")
	public Page<Answer> findAll(List<ObjectId> ids, Pageable pageable);
	
	/**
	 * Returns a list of Answers given a question id.
	 * @param questionRef
	 * @return
	 */
	public List<Answer> findByQuestionRef (ObjectId questionRef);
	
	/**
	 * Retrieves a list of answers by user who posted it
	 * @param id is the user id
	 * @param pageable - provides option for pagination
	 * @return matching answers
	 */
	@Query("{ 'postBy': {$ref:'users',$id:?0}} }")
	public Page<Answer> findByPostById(ObjectId userId, Pageable pageable);
	
	/**
	 * Returns a list of {@link Answer} that is filtered based on date ranges
	 * @param from is the lowerbound date
	 * @param to is the upperbound date
	 * @param pageable - provides option for pagination
	 * @return list of Answer that matches the date ranges
	 */
	public Page<Answer> findByCreatedOnBetween(DateTime from, DateTime to, Pageable pageable);
}

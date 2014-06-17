/**
 * 
 */
package com.phroogal.core.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;

import com.phroogal.core.domain.Question;

/**
 * Repository for {@link Question} data
 * 
 * @author Christopher Mariano
 * 
 */
public interface QuestionRepository extends BaseMongoRepository<Question> {
	
	/**
	 * Retrieves a question based on the Document Id
	 * @param docId
	 * @return the matching question
	 */
	public Question findByDocId(long docId);
	
	/**
	 * Retrieves a list of questions by user who posted it
	 * @param id is the user id
	 * @param pageable - provides option for pagination
	 * @return matching questions
	 */
	@Query("{ 'postBy': {$ref:'users',$id:?0}} }")
	public Page<Question> findByPostById(ObjectId userId, Pageable pageable);
	
	/**
	 * Retrieves a list of questions posted by a user within a given a list of userids 
	 * @param list of the user ids
	 * @param pageable - provides option for pagination
	 * @return matching questions
	 */
	@Query("{ 'postById': {$in: ?0} }")
	public Page<Question> findByPostById(List<ObjectId> userIds, Pageable pageable);
}

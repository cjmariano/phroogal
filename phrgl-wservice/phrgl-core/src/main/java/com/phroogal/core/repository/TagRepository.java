/**
 * 
 */
package com.phroogal.core.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.phroogal.core.domain.Tag;

/**
 * Repository for {@link Tag} data
 * 
 * @author Christopher Mariano
 * 
 */
public interface TagRepository extends MongoRepository<Tag,ObjectId>{	
	
	@Override
	@Cacheable(value="tagsCache")
	public List<Tag> findAll();
	
	/**
	 * Retrieves all tags starting with the keyword 
	 * @param keyword to filter results
	 * @return list of tags starting with the keyword given
	 */
	@Query("{name:{$regex : ?0, $options : 'i'}}")
	public List<Tag> findByNameStartingWith(String keyword);

	/**
	 * Retrieves all tags that matches the given list 
	 * @param tags to filter results
	 * @return list of tags that matches the tags on the given list
	 */
	@Query("{ 'name': {$in: ?0} }")
	public List<Tag> findByNames(List<String> tags);
}

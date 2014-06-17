/**
 * 
 */
package com.phroogal.core.repository;


import java.util.List;

import org.springframework.data.mongodb.repository.Query;

import com.phroogal.core.domain.CreditUnion;

/**
 * Repository for {@link CreditUnion} data
 * 
 */
public interface CreditUnionRepository extends BaseMongoRepository<CreditUnion>{
	
	
	/**
	 * Retrieves a list of Credit Unions based on the given keyword
	 * @param keyword to be used to filter the results
	 * @return the list of Credit Unions
	 */
	@Query("{name:{$regex : ?0, $options : 'i'}}")
	public List<CreditUnion> findByNameLike(String keyword);
}

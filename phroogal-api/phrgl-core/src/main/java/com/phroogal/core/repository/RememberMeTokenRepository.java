package com.phroogal.core.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.phroogal.core.domain.security.RememberMeToken;

@Repository("rememberMeTokenRepository")
public interface RememberMeTokenRepository extends MongoRepository<RememberMeToken, ObjectId>{
	
	/**
	 * Retireves token by series
	 * @param series
	 * @return
	 */
    public RememberMeToken findBySeries(String series);
    
    /**
     * Retrieve tokens by username
     * @param username
     * @return
     */
    public List<RememberMeToken> findByUsername(String username);
}
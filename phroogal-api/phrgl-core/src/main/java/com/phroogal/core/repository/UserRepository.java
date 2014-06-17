/**
 * 
 */
package com.phroogal.core.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.Query;

import com.phroogal.core.domain.User;
import com.phroogal.core.domain.UserProfile;

/**
 * Repository for {@link UserProfile} data
 * 
 * @author Christopher Mariano
 * 
 */
public interface UserRepository extends BaseMongoRepository<User>{

	/**
	 * Retrieves a user given the email
	 * @param email associated with a user
	 * @return the users that matches the email
	 */
	@Query("{'profile.email': { '$regex': ?0, '$options':'i' } }")
	public List<User> findByProfileEmail(String email);
	
	/**
	 * Retrieves a user given the username. 
	 * @param username to be used in retrieving user.
	 * @return the user that matches the username
	 */
	@Query("{'username': { '$regex': ?0, '$options':'i' } }")
	public User findByUsername(String username);
}

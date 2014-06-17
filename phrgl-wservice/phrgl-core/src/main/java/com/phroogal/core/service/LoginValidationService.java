/**
 * 
 */
package com.phroogal.core.service;


import com.phroogal.core.domain.User;
import com.phroogal.core.valueobjects.UserCredentials;

/**
 * This service validates if a user has valid credentials as a prerequisite to logging into the system  
 * @author cmariano
 *
 */
public interface LoginValidationService {
	
	/**
	 * Performs validation on a {@link User}} to verify credentials
	 * @param user credentials to be validated
	 * @return an instance of the validated {@link User}
	 * @throws An application exception if validation fails
	 */
	public User validateCredentials(UserCredentials userCredential) throws Throwable;
}

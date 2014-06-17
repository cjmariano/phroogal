/**
 * 
 */
package com.phroogal.core.service;

import com.phroogal.core.domain.User;

/**
 * Service for signing up a new user
 * @author Christopher Mariano
 *
 */
public interface SignupService {

	/**
	 * Signs up a new user
	 * @param user - to be signed up
	 * @return the user that was signed up
	 */
	public User signup(User user);
}

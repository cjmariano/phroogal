package com.phroogal.core.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;

import com.phroogal.core.domain.User;
import com.phroogal.core.domain.UserProfile;
import com.phroogal.core.exception.UserNotAuthenticatedException;

/**
 * Service to provide authentication details
 * @author Christopher Mariano
 *
 */
public interface AuthenticationDetailsService<ID> {
	
	/**
	 * Retrieves the userID of the Authenticated Principal
	 * @return
	 */
	public ID getAuthenticatedUserId();
	
	/**
	 * Sets the user as the authenticated principal
	 * @param user that should be authenticated
	 */
	public void setAuthenticatedUser(User user);
	
	/**
	 * Retrieves the authenticated user
	 * @return instance of {@link UserProfile} authenticated in the {@link SecurityContext} 
	 */
	public User getAuthenticatedUser();
	
	/**
	 * Retrieves the token for an authentication request or for an authenticated principal 
	 * once the request has been processed
	 * @return instance of {@link Authentication} 
	 */
	public Authentication getAuthentication();
	
	/**
	 * Verifies if a user is still authenticated or logged in within the system.
	 * @throws UserNotAuthenticatedException if no user is authenticated
	 */
	public void verifyUserIsAuthenticated() throws UserNotAuthenticatedException;

}

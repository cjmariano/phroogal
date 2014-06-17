/**
 * 
 */
package com.phroogal.core.service.impl;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.phroogal.core.domain.User;
import com.phroogal.core.exception.AccountNotActiveException;
import com.phroogal.core.exception.EmailNotVerifiedException;
import com.phroogal.core.exception.InvalidUserNamePasswordException;
import com.phroogal.core.exception.LoginFailedException;
import com.phroogal.core.service.AuthenticationDetailsService;
import com.phroogal.core.service.LoginValidationService;
import com.phroogal.core.valueobjects.UserCredentials;

/**
 * Implementation of {@link LoginValidationService} that <br>
 * 1.Validates using a user's username and password<br>
 * 2.Gets the authenticated user id
 * 
 * @author cmariano
 *
 */
@Service("userNamePasswordValidationService")
public class UserNamePasswordValidationService implements LoginValidationService {

	@Autowired
	private AuthenticationDetailsService<ObjectId> authenticationService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Override
	public User validateCredentials(UserCredentials userCredentials) throws Throwable {
		 UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userCredentials.getUsername(), userCredentials.getPassword());
		 try {
  			 Authentication authentication = authenticationManager.authenticate(token);
  		     return retrievePrincipalUser(authentication);	
		} catch (BadCredentialsException bce) {
			throw new InvalidUserNamePasswordException(bce);
		} catch (AuthenticationServiceException ase) {
			throw ase.getCause();
		} catch (EmailNotVerifiedException enve) {
			throw enve;
		} catch (AccountNotActiveException anae) {
			throw anae;
		} catch (Exception e) {
			throw new LoginFailedException(e);
		} 
	}
	
	private User retrievePrincipalUser(Authentication authentication) throws BadCredentialsException {
		if (isAuthenticated(authentication)) {
			 SecurityContextHolder.getContext().setAuthentication(authentication);
			 return authenticationService.getAuthenticatedUser();
		}
		throw new BadCredentialsException("Invalid Credentials");
	}
	
	private boolean isAuthenticated(Authentication authentication) {
        return authentication != null && !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }
}

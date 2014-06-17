package com.phroogal.core.domain.security;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * Extension of Spring's User Details to provide a more customized attributes for the application
 * @author cmarian1
 *
 */
public interface UserAccountDetails extends UserDetails {
	
	/**
     * Indicates whether the user's email has been verified.
     *
     * @return <code>true</code> if the user's email is verified, <code>false</code> otherwise
     */
    boolean isEmailVerified();
    
    /**
     * Indicates whether the user account is active.
     *
     * @return <code>true</code> if the user's account is active, <code>false</code> otherwise
     */
    boolean isAccountActive();

}

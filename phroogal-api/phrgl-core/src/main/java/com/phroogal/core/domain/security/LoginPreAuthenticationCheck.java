/**
 * 
 */
package com.phroogal.core.domain.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;

import com.phroogal.core.exception.AccountNotActiveException;
import com.phroogal.core.exception.EmailNotVerifiedException;

/**
 * Extends the default authentication check for spring security to provide customized business requirements
 * @author Christopher Mariano
 *
 */
public class LoginPreAuthenticationCheck implements UserDetailsChecker {
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	@Value("${mail.client.support}")
	private String supportEmail;

	@Override
	public void check(UserDetails userDetails) {
		if (userDetails instanceof UserAccountDetails) {
			UserAccountDetails userAccountDetails = (UserAccountDetails) userDetails;
			checkAccountActive(userAccountDetails);
			checkEmailVerified(userAccountDetails);
		}
    }

	private void checkAccountActive(UserAccountDetails userAccountDetails) {
		if (! userAccountDetails.isAccountActive()) {
			throw new AccountNotActiveException(new String[] {supportEmail}); 
		}
	}

	private void checkEmailVerified(UserAccountDetails userAccountDetails) {
		if (! userAccountDetails.isEmailVerified()) {
			throw new EmailNotVerifiedException(); 
		}
	}
}

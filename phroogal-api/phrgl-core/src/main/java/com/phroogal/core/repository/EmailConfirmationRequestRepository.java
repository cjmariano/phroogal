/**
 * 
 */
package com.phroogal.core.repository;

import com.phroogal.core.domain.Answer;
import com.phroogal.core.domain.security.EmailConfirmationRequest;

/**
 * Repository for {@link Answer} data
 * 
 * @author Christopher Mariano
 * 
 */
public interface EmailConfirmationRequestRepository extends BaseMongoRepository<EmailConfirmationRequest>{
	
	/**
	 * Returns the email confirmation request by email
	 * @param email associated with the email confirmation request
	 * @return the email confirmation request
	 */
	public EmailConfirmationRequest findByEmail (String email);
	
}

/**
 * 
 */
package com.phroogal.core.service;

import org.bson.types.ObjectId;

import com.phroogal.core.domain.security.PasswordResetRequest;


/**
 * Service class that takes care of password reset related functions
 * @author Christopher Mariano
 *
 */
public interface PasswordResetService  extends Service<PasswordResetRequest, ObjectId>{
	
	/**
	 * Creates a password reset request
	 * @param email - email of the password reset requestor
	 * @return the password reset request
	 */
	public PasswordResetRequest createPasswordResetRequest(String email);
	
	/**
	 * Processes the password reset request, and dispose of the request once done
	 * @param passwordResetRequestId - id of the password reset request
	 * @param newPassword that would be applied
	 * @return the password reset request
	 */
	public PasswordResetRequest processResetRequest(ObjectId passwordResetRequestId, String newPassword);
}

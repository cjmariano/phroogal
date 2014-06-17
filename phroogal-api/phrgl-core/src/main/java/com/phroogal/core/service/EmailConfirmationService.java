package com.phroogal.core.service;

import org.bson.types.ObjectId;

import com.phroogal.core.domain.User;
import com.phroogal.core.domain.security.EmailConfirmationRequest;
import com.phroogal.core.exception.EmailConfirmationRequestIsNotValid;


/**
 * Service class that provides email confirmation functions
 * @author Christopher Mariano
 *
 */
public interface EmailConfirmationService  extends Service<EmailConfirmationRequest, ObjectId>{
	
	/**
	 * Creates a email confirmation request
	 * @param email - email of the user to be confirmed
	 * @return the {@link EmailConfirmationRequest}
	 */
	public EmailConfirmationRequest createRequest(String email);
	
	/**
	 * Processes the email confirmation request, and dispose of the request once done
	 * @param emailConfirmationRequestId - id of the email confirmation request
	 * @return the user once the email is verified 
	 */
	public User processRequest(ObjectId emailConfirmationRequestId) throws EmailConfirmationRequestIsNotValid;
}

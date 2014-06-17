/**
 * 
 */
package com.phroogal.core.service;

import com.phroogal.core.notification.MailNotificationRequest;

/**
 * Service for handling application related mail functionalities 
 * @author Christopher Mariano
 *
 */
public interface MailService {
	
	/**
	 * Sends an email using the parameters given
	 * @param request contains the request for sending the email
	 * @param template that would return the content
	 */
	public void send(MailNotificationRequest request);

}

/**
 * 
 */
package com.phroogal.web.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.phroogal.core.domain.User;
import com.phroogal.core.domain.security.PasswordResetRequest;
import com.phroogal.core.notification.EmailTemplate;
import com.phroogal.core.notification.MailContentContext;
import com.phroogal.core.notification.MailNotificationRequest;
import com.phroogal.core.service.MailService;

/**
 * Sends an email notification to users that password reset is successful 
 * @author Christopher Mariano
 *
 */
public class PasswordResetCompleteNotification {
	
	@Autowired
	@Qualifier("passwordResetCompleteTemplate")
	private EmailTemplate passwordResetCompleteTemplate;
	
	@Autowired
	private MailService mailService;
	
	/**
	 * Sends a password reset notification to the user
	 * @param user who requested the password reset
	 * @param passwordResetReq - request details
	 */
	public void sendNotification(User user, PasswordResetRequest passwordResetReq) {
		MailContentContext context = generateMailContentContext(user);
		MailNotificationRequest notificationRequest = generateMailNotificationRequest(passwordResetReq, context);
		mailService.send(notificationRequest);
	} 
	
	private MailContentContext generateMailContentContext(User user) {
		MailContentContext context = new MailContentContext();
		context.addParam("firstname", user.getProfile().getFirstname());
		return context;
	}
	
	private MailNotificationRequest generateMailNotificationRequest(PasswordResetRequest passwordResetReq, MailContentContext context) {
			MailNotificationRequest request = new MailNotificationRequest();
			request.setRequestId(passwordResetReq.getId().toString());
			request.setEmail(passwordResetReq.getEmail());
			request.setEmailSender(passwordResetReq.getEmailSender());
			request.setEmailSenderName(passwordResetReq.getEmailSenderName());
			request.setEmailSubject(passwordResetReq.getEmailSubject());
			request.setContent(passwordResetCompleteTemplate.getContent(context));
			return request;
	}
}

/**
 * 
 */
package com.phroogal.web.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;

import com.phroogal.core.domain.User;
import com.phroogal.core.domain.security.EmailConfirmationRequest;
import com.phroogal.core.notification.EmailTemplate;
import com.phroogal.core.notification.MailContentContext;
import com.phroogal.core.notification.MailNotificationRequest;
import com.phroogal.core.service.MailService;
import com.phroogal.web.context.WebApplicationContext;

/**
 * Notification for request to confirm email 
 * @author Christopher Mariano
 *
 */
public class EmailConfirmationNotification {
	
	@Value("${application.url}")
	private String applicationUrl;

	@Autowired
	@Qualifier("emailConfirmationTemplate")
	private EmailTemplate emailConfirmationTemplate;
	
	@Autowired
	private MailService mailService;
	
	/**
	 * Sends an email request for a user to verify the email address used in signing up 
	 * @param newUser that will receive the email confirmation notification
	 * @param emailConfirmationRequest - details for processing this request
	 */
	public void sendNotification(User newUser, EmailConfirmationRequest emailConfirmationRequest) {
		MailContentContext context = generateMailContentContext(newUser, emailConfirmationRequest);
		MailNotificationRequest notificationRequest = generateMailNotificationRequest(emailConfirmationRequest, context);
		mailService.send(notificationRequest);
	} 
	
	private MailContentContext generateMailContentContext(User user, EmailConfirmationRequest emailConfirmationRequest) {
		MailContentContext context = new MailContentContext();
		context.addParam("firstname", user.getProfile().getFirstname());
		context.addParam("emailconfirmationLink", String.format(generateEmailConfirmationFormat(), emailConfirmationRequest.getRequestId()));
		return context;
	}

	private MailNotificationRequest generateMailNotificationRequest(EmailConfirmationRequest emailConfirmationRequest, MailContentContext context) {
		MailNotificationRequest request = new MailNotificationRequest();
		request.setRequestId(emailConfirmationRequest.getId().toString());
		request.setEmail(emailConfirmationRequest.getEmail());
		request.setEmailSender(emailConfirmationRequest.getEmailSender());
		request.setEmailSenderName(emailConfirmationRequest.getEmailSenderName());
		request.setEmailSubject(emailConfirmationRequest.getEmailSubject());
		request.setContent(emailConfirmationTemplate.getContent(context));
		return request;
	}
	
	private String generateEmailConfirmationFormat() {
		StringBuffer sb = new StringBuffer();
		sb.append(applicationUrl);
		sb.append(WebApplicationContext.PAGE_EMAIL_CONFIRMATION);
		sb.append("?rid=%s");
		return sb.toString();
	}
}

/**
 * 
 */
package com.phroogal.web.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;

import com.phroogal.core.domain.User;
import com.phroogal.core.domain.security.PasswordResetRequest;
import com.phroogal.core.notification.EmailTemplate;
import com.phroogal.core.notification.MailContentContext;
import com.phroogal.core.notification.MailNotificationRequest;
import com.phroogal.core.service.MailService;
import com.phroogal.web.context.WebApplicationContext;

/**
 * Sends an email notification to users who requested a password reset 
 * @author Christopher Mariano
 *
 */
public class PasswordResetNotification {
	
	@Autowired
	@Qualifier("passwordResetTemplate")
	private EmailTemplate passwordResetTemplate;
	
	@Value("${application.url}")
	private String applicationUrl;
	
	@Value("${mail.password.reset.validity.hours}")
	private int passwordLinkValidity;
	
	@Autowired
	private MailService mailService;
	
	/**
	 * Sends a password reset notification to the user
	 * @param user who requested the password reset
	 * @param passwordResetReq - request details
	 */
	public void sendNotification(User user, PasswordResetRequest passwordResetReq) {
		MailContentContext context = generateMailContentContext(user, passwordResetReq);
		MailNotificationRequest notificationRequest = generateMailNotificationRequest(passwordResetReq, context);
		mailService.send(notificationRequest);
	} 
	
	private MailContentContext generateMailContentContext(User user, PasswordResetRequest passwordResetReq) {
		MailContentContext context = new MailContentContext();
		context.addParam("firstname", user.getProfile().getFirstname());
		context.addParam("passwordResetLink", String.format(generatePasswordFormat(), passwordResetReq.getRequestId()));
		context.addParam("passwordLinkValidity", String.valueOf(passwordLinkValidity));
		return context;
	}
	
	private MailNotificationRequest generateMailNotificationRequest(PasswordResetRequest passwordResetReq, MailContentContext context) {
			MailNotificationRequest request = new MailNotificationRequest();
			request.setRequestId(passwordResetReq.getId().toString());
			request.setEmail(passwordResetReq.getEmail());
			request.setEmailSender(passwordResetReq.getEmailSender());
			request.setEmailSenderName(passwordResetReq.getEmailSenderName());
			request.setEmailSubject(passwordResetReq.getEmailSubject());
			request.setContent(passwordResetTemplate.getContent(context));
			return request;
	}
	
	private String generatePasswordFormat() {
		StringBuffer sb = new StringBuffer();
		sb.append(applicationUrl);
		sb.append("/");
		sb.append(WebApplicationContext.PAGE_PASSWORD_RESET);
		sb.append("?rid=%s");
		return sb.toString();
	}
}

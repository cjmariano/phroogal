/**
 * 
 */
package com.phroogal.web.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;

import com.phroogal.core.domain.User;
import com.phroogal.core.notification.EmailTemplate;
import com.phroogal.core.notification.MailContentContext;
import com.phroogal.core.notification.MailNotificationRequest;
import com.phroogal.core.service.MailService;

/**
 * Welcome mail notification to be sent to new users 
 * @author Christopher Mariano
 *
 */
public class WelcomeMailNotification {
	
	@Autowired
	@Qualifier(value="welcomeMailTemplate")
	private EmailTemplate welcomeMailTemplate;
	
	@Autowired
	private MailService mailService;
	
	@Value("${mail.client.from.name}")
	private String emailSenderName;
	
	@Value("${mail.client.from}")
	private String emailSender;
	
	@Value("${mail.welcome.email.subject}")
	private String emailSubject;
	
	@Value("${application.url}")
	private String applicationUrl;
	
	@Value("${page.url.addQuestion}")
	private String pageUrlAddQuestion;
	
	@Value("${page.url.unansweredQuestion}")
	private String pageUrlUnansweredQuestion;
	
	
	/**
	 * Sends a welcome mail to the user
	 * @param email of the user
	 * @param firstName of the user
	 */
	public void sendWelcomeMail(User user) {
		String firstName = user.getProfile().getFirstname();
		String email = user.getProfile().getEmail();
		sendWelcomeEmailNotification(firstName, email);
	}
	
	private void sendWelcomeEmailNotification(String firstName, String email) {
		MailContentContext context = generateMailContentContextForWelcomeEmail(firstName);
		MailNotificationRequest welcomeEmail = generateMailNotificationRequestForWelcomeEmail(email, context);
		mailService.send(welcomeEmail);
	}
	
	private MailContentContext generateMailContentContextForWelcomeEmail(String userFirstName) {
		MailContentContext context = new MailContentContext();
		context.addParam("firstname", userFirstName);
		context.addParam("addQuestionLink", applicationUrl.concat(pageUrlAddQuestion));
		context.addParam("unansweredQuestionLink", applicationUrl.concat(pageUrlUnansweredQuestion));
		return context;
	}
	
	private MailNotificationRequest generateMailNotificationRequestForWelcomeEmail(String email, MailContentContext context) {
		MailNotificationRequest request = new MailNotificationRequest();
		request.setEmail(email);
		request.setEmailSender(emailSender);
		request.setEmailSenderName(emailSenderName);
		request.setEmailSubject(emailSubject);
		request.setContent(welcomeMailTemplate.getContent(context));
		return request;
	}
}

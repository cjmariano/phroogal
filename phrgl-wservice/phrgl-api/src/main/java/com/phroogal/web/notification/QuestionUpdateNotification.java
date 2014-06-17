package com.phroogal.web.notification;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.phroogal.core.domain.Question;
import com.phroogal.core.domain.User;
import com.phroogal.core.notification.EmailTemplate;
import com.phroogal.core.notification.MailContentContext;
import com.phroogal.core.notification.MailNotificationRequest;
import com.phroogal.core.notification.Subscriber;
import com.phroogal.core.service.MailService;

/**
 * Provides notification when there is a notification that calls for an
 * answer notification 
 * @author Christopher Mariano
 *
 */
@Component("questionUpdateNotification")
public class QuestionUpdateNotification implements Subscriber<Question> {

	private static final Logger log = Logger.getLogger(QuestionUpdateNotification.class);

	@Value("${mail.client.from}")
	private String emailSender;
	
	@Value("${mail.client.from.name}")
	private String emailSenderName;
	
	@Value("${mail.question.notification.subject}")
	private String emailSubject;
	
	@Autowired
	@Qualifier(value="questionUpdateNotificationTemplate")
	private EmailTemplate questionUpdateNotificationTemplate;
	
	@Autowired
	private MailService mailService;

	@Override
	public void onNotify(Question question) {
		if (isNew(question)) {
			if (question != null) {
				sendQuestionUpdateNotification(question);
			}	
		}
	}
	
	private void sendQuestionUpdateNotification(Question question) {
		try {
			User user = question.getPostBy();
			String email = user.getProfile().getEmail(); 
			String content = generateContent(question);
			MailNotificationRequest notificationRequest = createMailNotificationRequest(email, content);
			mailService.send(notificationRequest);
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error(ex.getMessage());
		}
	}

	private String generateContent(Question question) {
		MailContentContext context = new MailContentContext();
		context.addParam("questionPostedBy", question.getPostBy().getProfile().getFirstname());
		return questionUpdateNotificationTemplate.getContent(context);
	}

	private MailNotificationRequest createMailNotificationRequest(String email, String content) {
		MailNotificationRequest sendMailRequest = new MailNotificationRequest();
		sendMailRequest.setEmail(email);
		sendMailRequest.setEmailSender(emailSender);
		sendMailRequest.setEmailSenderName(emailSenderName);
		sendMailRequest.setEmailSubject(emailSubject);
		sendMailRequest.setContent(content);
		return sendMailRequest;
	}

	private boolean isNew(Question question) {
		return question.getCreatedOn().equals(question.getModifiedOn());
	}
}

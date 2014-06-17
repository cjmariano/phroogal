package com.phroogal.web.notification;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.phroogal.core.domain.Answer;
import com.phroogal.core.domain.Question;
import com.phroogal.core.domain.User;
import com.phroogal.core.notification.EmailTemplate;
import com.phroogal.core.notification.MailContentContext;
import com.phroogal.core.notification.MailNotificationRequest;
import com.phroogal.core.notification.Subscriber;
import com.phroogal.core.service.MailService;
import com.phroogal.core.service.QuestionService;
import com.phroogal.core.social.SocialNetworkType;
import com.phroogal.web.utility.SlugGenerator;

/**
 * Provides notification when there is a notification that calls for an
 * answer notification 
 * @author Christopher Mariano
 *
 */
@Component("answerUpdateNotification")
public class AnswerUpdateNotification implements Subscriber<Answer> {

	private static final Logger log = Logger.getLogger(AnswerUpdateNotification.class);

	@Value("${application.url}")
	private String applicationUrl;
	
	@Value("${mail.client.from}")
	private String emailSender;
	
	@Value("${mail.client.from.name}")
	private String emailSenderName;
	
	@Value("${mail.answer.notification.subject}")
	private String emailSubject;
	
	@Autowired
	private QuestionService questionService;
	
	@Autowired
	@Qualifier(value="answerUpdateNotificationTemplate")
	private EmailTemplate answerUpdateNotificationTemplate;
	
	@Autowired
	private MailService mailService;

	@Override
	public void onNotify(Answer answer) {
		if (isNew(answer)) {
			Question question = questionService.findById(answer.getQuestionRef());
			if (question != null) {
				sendAnswerUpdateNotification(answer, question);
			}	
		}
	}
	
	private void sendAnswerUpdateNotification(Answer answer, Question question) {
		try {
			User questionPostedBy = question.getPostBy();
			String email = resolveEmailAddress(questionPostedBy);
			String content = generateContent(question, answer);
			MailNotificationRequest notificationRequest = createMailNotificationRequest(email, content);
			mailService.send(notificationRequest);
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error(ex.getMessage());
		}
	}

	private String generateContent(Question question, Answer answer) {
		MailContentContext context = new MailContentContext();
		context.addParam("questionPostedBy", question.getPostBy().getProfile().getFirstname());
		context.addParam("questionTitle", question.getTitle());
		context.addParam("answer", answer.getContent());
		context.addParam("answeredBy", answer.getPostBy().getProfile().getFirstname());
		context.addParam("questionLink", generateQuestionLink(question));
		return answerUpdateNotificationTemplate.getContent(context);
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
	
	private String resolveEmailAddress(User questionPostedBy) {
		String email = questionPostedBy.getProfile().getEmail();
		if (email == null) {
			SocialNetworkType socialNetwork = questionPostedBy.getPrimarySocialNetworkConnection();
			email = questionPostedBy.getSocialProfile(socialNetwork).getEmail();
		}
		return email;
	}

	private boolean isNew(Answer subject) {
		return subject.getCreatedOn().equals(subject.getModifiedOn());
	}
	
	private String generateQuestionLink(Question question) {
		StringBuffer sb = new StringBuffer();
		sb.append(applicationUrl);
		sb.append("question/");
		sb.append(question.getDocId());
		sb.append("/");
		sb.append(SlugGenerator.toSlug(question.getTitle()));
		return sb.toString();
	}
}

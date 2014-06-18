/**
 * 
 */
package com.phroogal.web.notification;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.phroogal.core.domain.FlagNotificationRequest;
import com.phroogal.core.domain.Post;
import com.phroogal.core.domain.Question;
import com.phroogal.core.domain.QuestionThreadPost;
import com.phroogal.core.notification.EmailTemplate;
import com.phroogal.core.notification.MailContentContext;
import com.phroogal.core.notification.MailNotificationRequest;
import com.phroogal.core.service.MailService;
import com.phroogal.core.service.QuestionService;
import com.phroogal.web.utility.LinkGenerator;

/**
 * Notification for request to flag a post 
 * @author Christopher Mariano
 *
 */
@Component(value="flagNotification")
public class FlagNotification {
	
	@Autowired
	private QuestionService questionService;
	
	@Value("${application.url}")
	private String applicationUrl;
	
	@Value("${mail.client.from}")
	private String emailSender;
	
	@Value("${mail.client.from.name}")
	private String emailSenderName;
	
	@Value("${mail.client.admin}")
	private String emailAdmin;
	
	@Value("${mail.flag.notification.subject}")
	private String emailSubject;

	@Autowired
	@Qualifier("flagNotificationTemplate")
	private EmailTemplate flagNotificationTemplate;
	
	@Autowired
	private MailService mailService;
	
	/**
	 * Sends an email request notifying a user that his/her post has been flagged
	 * @param flagNotificationRequest details for making the notification request
	 * @param post that is flagged
	 */
	public void sendNotification(FlagNotificationRequest flagNotificationRequest, Post post) {
		MailContentContext context = generateMailContentContext(flagNotificationRequest, post);
		MailNotificationRequest notificationRequest = generateMailContentContext(flagNotificationRequest, context);
		mailService.send(notificationRequest);
	} 
	
	private MailContentContext generateMailContentContext(FlagNotificationRequest flagNotificationRequest, Post post) {
		MailContentContext context = new MailContentContext();
		context.addParam("firstname", flagNotificationRequest.getPostBy().getProfile().getFirstname());
		context.addParam("postType", post.getPostType().getValue());
		context.addParam("postContent", flagNotificationRequest.getContent());
		context.addParam("postLink", resolvePostLink(post));
		return context;
	}

	private MailNotificationRequest generateMailContentContext(FlagNotificationRequest flagNotificationRequest, MailContentContext context) {
		MailNotificationRequest request = new MailNotificationRequest();
		request.setRequestId(flagNotificationRequest.getId().toString());
		request.setEmail(flagNotificationRequest.getPostBy().getProfile().getEmail());
		request.setEmailSender(emailSender);
		request.setEmailSenderName(emailSenderName);
		request.setEmailSubject(emailSubject);
		request.setSendToAsBCC(emailAdmin);
		request.setContent(flagNotificationTemplate.getContent(context));
		return request;
	}
	
	private String resolvePostLink(Post post) {
		if (post instanceof QuestionThreadPost) {
			QuestionThreadPost questionThread = (QuestionThreadPost) post;
			Question question = questionService.findById(questionThread.getRootQuestionId());
			return getLink(question);
		}
		return StringUtils.EMPTY;
	}
	
	private String getLink(Question question) {
		StringBuffer sb = new StringBuffer();
		sb.append(applicationUrl);
		sb.append("/");
		sb.append(LinkGenerator.getQuestionLink(question.getDocId() , question.getTitle()));
		return sb.toString();
	}
}

package com.phroogal.core.service.impl;

import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.phroogal.core.notification.MailNotificationRequest;
import com.phroogal.core.service.MailService;

@Service
public class MailServiceImpl implements MailService {
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	@Autowired
	@Qualifier("mailSender")
	private JavaMailSender mailSender;
	
	private final String CONTENT_ENCODING = "UTF-8";
	
	@Override
	@Async
	public void send(MailNotificationRequest request) {
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			addRecepientsIfAvailable(mimeMessage, request);
		    MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, CONTENT_ENCODING);
		    message.setSubject(request.getEmailSubject());
		    message.setFrom(new InternetAddress(request.getEmailSender(), request.getEmailSenderName()));
		    message.setTo(request.getEmail()); 
		    message.setText(request.getContent(), true);
		    mailSender.send(mimeMessage);
		} catch (Exception e) {
			logger.error("Failed to send message: " + e.getMessage());
			e.printStackTrace();
		}
	}


	private void addRecepientsIfAvailable(MimeMessage mimeMessage, MailNotificationRequest request) throws AddressException, MessagingException {
		if ( ! StringUtils.isBlank(request.getSendToAsCC())) {
			mimeMessage.addRecipient(RecipientType.CC, new InternetAddress(request.getSendToAsCC()));	
		}
		if ( ! StringUtils.isBlank(request.getSendToAsBCC())) {
			mimeMessage.addRecipient(RecipientType.BCC, new InternetAddress(request.getSendToAsBCC()));	
		}
	}
}

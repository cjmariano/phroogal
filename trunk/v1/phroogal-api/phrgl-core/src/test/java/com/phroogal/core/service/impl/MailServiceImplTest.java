package com.phroogal.core.service.impl;

import javax.mail.internet.MimeMessage;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.util.ReflectionTestUtils;

import com.phroogal.core.notification.MailNotificationRequest;

public class MailServiceImplTest {

	private MailServiceImpl serviceImpl = new MailServiceImpl();
	

	@Test
	public void testSend() {
		JavaMailSender mailSender = Mockito.mock(JavaMailSenderImpl.class);
		MimeMessage mimeMessage = Mockito.mock(MimeMessage.class);
		MailNotificationRequest request = Mockito.mock(MailNotificationRequest.class);
		ReflectionTestUtils.setField(serviceImpl, "mailSender", mailSender);
		
		Mockito.when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
		Mockito.when(request.getEmailSubject()).thenReturn("Test Subject");
		Mockito.when(request.getEmailSender()).thenReturn("sender@b.com");
		Mockito.when(request.getEmail()).thenReturn("receipient@b.com");
		Mockito.when(request.getContent()).thenReturn("Test Message");
		
		serviceImpl.send(request);
		Mockito.verify(mailSender, Mockito.atLeastOnce()).send(Mockito.any(MimeMessage.class));
	}
}


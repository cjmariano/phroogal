package com.phroogal.web.notification.template;

import org.springframework.stereotype.Service;

import com.phroogal.core.notification.EmailTemplate;

/**
 * Template for Password Reset email content
 * @author Christopher Mariano
 *
 */
@Service("answerUpdateNotificationTemplate")
public class AnswerUpdateNotificationTemplate extends BaseEmailTemplate implements EmailTemplate {

	@Override
	public String getHtmlTemplate() {
		return "add-answer-notification-template";
	}
}

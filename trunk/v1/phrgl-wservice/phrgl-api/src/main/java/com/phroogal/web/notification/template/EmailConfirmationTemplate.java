package com.phroogal.web.notification.template;

import org.springframework.stereotype.Service;

import com.phroogal.core.notification.EmailTemplate;

/**
 * Template for Password Reset email content
 * @author Christopher Mariano
 *
 */
@Service("emailConfirmationTemplate")
public class EmailConfirmationTemplate extends BaseEmailTemplate implements EmailTemplate {

	@Override
	public String getHtmlTemplate() {
		return "email_confirmation_template";
	}
}

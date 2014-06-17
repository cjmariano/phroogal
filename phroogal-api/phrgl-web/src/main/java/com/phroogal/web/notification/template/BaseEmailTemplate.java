/**
 * 
 */
package com.phroogal.web.notification.template;

import org.springframework.beans.factory.annotation.Autowired;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring3.SpringTemplateEngine;

import com.phroogal.core.notification.MailContentContext;

/**
 * Base class for email content generation
 * @author Christopher Mariano
 *
 */
public abstract class BaseEmailTemplate {
	
	@Autowired
	private SpringTemplateEngine templateEngine;

	/**
	 * Template for generating content. File should be under /templates/mail location
	 * @return the html template
	 */
	protected abstract String getHtmlTemplate();
	
	public String getContent(MailContentContext mailContext) {
		Context context = new Context();
		context.setVariables(mailContext.getContext());
	    return templateEngine.process(getHtmlTemplate(), context);
	}
}

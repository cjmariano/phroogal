/**
 * 
 */
package com.phroogal.core.valueobjects;

import com.phroogal.core.service.MailService;

/**
 * Holds information to be sent as parameters for the {@link MailService}
 * @author Christopher Mariano
 *
 */
public class MailParameters {

	private String subject;
	
	private String from;
	
	private String to;
	
	private String text;

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}

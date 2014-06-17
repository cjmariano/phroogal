/**
 * 
 */
package com.phroogal.core.notification;

import org.joda.time.DateTime;

/**
 * Contains information needed to make an email request 
 * @author Christopher Mariano
 *
 */
public class MailNotificationRequest {
	
	private String requestId;
	
	private String email;
	
	private DateTime requestDate;
	
	private String emailSender;
	
	private String emailSenderName;
	
	private String emailSubject;
	
	private String content;
	
	private String sendToAsBCC;
	
	private String sendToAsCC;

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public DateTime getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(DateTime requestDate) {
		this.requestDate = requestDate;
	}

	public String getEmailSender() {
		return emailSender;
	}

	public void setEmailSender(String emailSender) {
		this.emailSender = emailSender;
	}

	public String getEmailSubject() {
		return emailSubject;
	}

	public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getEmailSenderName() {
		return emailSenderName;
	}

	public void setEmailSenderName(String emailSenderName) {
		this.emailSenderName = emailSenderName;
	}

	public String getSendToAsBCC() {
		return sendToAsBCC;
	}

	/**
	 * Allows message to be sent with blind carbon copy (BCC).
	 * @param sendToAsBCC - email address for the BCC option
	 */
	public void setSendToAsBCC(String sendToAsBCC) {
		this.sendToAsBCC = sendToAsBCC;
	}

	public String getSendToAsCC() {
		return sendToAsCC;
	}

	/**
	 * Allows message to be sent with carbon copy (CC)
	 * @param sendToAsCC - email address for the CC option
	 */
	public void setSendToAsCC(String sendToAsCC) {
		this.sendToAsCC = sendToAsCC;
	}
}



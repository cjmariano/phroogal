/**
 * 
 */
package com.phroogal.core.domain.security;

import java.io.Serializable;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.phroogal.core.domain.Persistent;

/**
 * Holds information that would enable a user to do Password Reset
 * @author Christopher Mariano
 *
 */
@Document(collection = "email_confirmation_requests")
public class EmailConfirmationRequest implements Persistent<ObjectId>, Serializable {

	private static final long serialVersionUID = -4217560277152578067L;

	@Id
	private ObjectId id;
	
	private String email;
	
	@Transient
	private String emailSender;
	
	@Transient
	private String emailSenderName;
	
	@Transient
	private String emailSubject;
	
	@Override
	public ObjectId getId() {
		return id;
	}

	@Override
	public void setId(ObjectId id) {
		this.id = id;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getRequestId() {
		return id.toString();
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

	public String getEmailSenderName() {
		return emailSenderName;
	}

	public void setEmailSenderName(String emailSenderName) {
		this.emailSenderName = emailSenderName;
	}
}

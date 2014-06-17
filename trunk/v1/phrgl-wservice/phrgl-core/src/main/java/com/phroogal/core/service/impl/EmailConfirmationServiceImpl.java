package com.phroogal.core.service.impl;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.phroogal.core.domain.User;
import com.phroogal.core.domain.security.EmailConfirmationRequest;
import com.phroogal.core.exception.EmailConfirmationRequestIsNotValid;
import com.phroogal.core.repository.EmailConfirmationRequestRepository;
import com.phroogal.core.service.EmailConfirmationService;
import com.phroogal.core.service.UserService;

@Service
public class EmailConfirmationServiceImpl extends BaseService<EmailConfirmationRequest, ObjectId, EmailConfirmationRequestRepository> implements EmailConfirmationService{

	@Autowired
	private UserService userService;
	
	@Autowired
	private EmailConfirmationRequestRepository emailConfirmationRepository;
	
	@Value("${mail.client.from}")
	private String emailSender;
	
	@Value("${mail.client.from.name}")
	private String emailSenderName;
	
	@Value("${mail.email.confirmation.subject}")
	private String emailSubject;
	
	@Override
	protected EmailConfirmationRequestRepository getRepository() {
		return emailConfirmationRepository;
	}

	@Override
	public EmailConfirmationRequest findById(ObjectId argId) {
		EmailConfirmationRequest request = super.findById(argId); 
		checkIfRequestExists(request);
		return request;
	}

	@Override
	public EmailConfirmationRequest createRequest(String email) {
		checkIfEmailIsValid(email);
		EmailConfirmationRequest emailConfirmationReq = emailConfirmationRepository.findByEmail(email);
		if (emailConfirmationReq == null) {
			emailConfirmationReq = new EmailConfirmationRequest();
		}
		emailConfirmationReq.setEmail(email);
		emailConfirmationReq.setEmailSender(emailSender);
		emailConfirmationReq.setEmailSenderName(emailSenderName);
		emailConfirmationReq.setEmailSubject(emailSubject);
		return super.saveOrUpdate(emailConfirmationReq);
	}
	
	@Override
	public User processRequest(ObjectId emailConfirmationRequestId) throws EmailConfirmationRequestIsNotValid {
		EmailConfirmationRequest emailConfirmationRequest = this.findById(emailConfirmationRequestId);
		User user = userService.getUserByUserName(emailConfirmationRequest.getEmail());
		user.setEmailVerified(true);
		userService.saveOrUpdate(user);
		delete(emailConfirmationRequest);
		return user;
	}

	private void checkIfEmailIsValid(String email) {
		userService.getUserByUserName(email);
	}
	
	private void checkIfRequestExists(EmailConfirmationRequest request) {
		if (request == null) {
			throw new EmailConfirmationRequestIsNotValid();
		}
	}
}

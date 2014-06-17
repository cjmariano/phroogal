package com.phroogal.core.service.impl;

import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.phroogal.core.domain.User;
import com.phroogal.core.domain.security.PasswordResetRequest;
import com.phroogal.core.exception.PasswordResetLinkIsExpiredException;
import com.phroogal.core.exception.PasswordResetRequestIsNotValid;
import com.phroogal.core.repository.PasswordResetRequestRepository;
import com.phroogal.core.service.PasswordResetService;
import com.phroogal.core.service.UserService;
import com.phroogal.core.validator.Validator;

@Service
public class PasswordResetServiceImpl extends BaseService<PasswordResetRequest, ObjectId, PasswordResetRequestRepository> implements PasswordResetService{

	@Autowired
	private UserService userService;
	
	@Autowired
	private PasswordResetRequestRepository passwordResetRepository;
	
	@Autowired
	@Qualifier(value="passwordResetLinkValidator")
	private Validator<PasswordResetRequest> passwordResetLinkValidator;
	
	@Value("${mail.client.from}")
	private String emailSender;
	
	@Value("${mail.client.from.name}")
	private String emailSenderName;
	
	@Value("${mail.password.reset.request.subject}")
	private String emailSubjectOnRequest;
	
	@Value("${mail.password.reset.confirm.subject}")
	private String emailSubjectOnConfirm;
	
	@Override
	protected PasswordResetRequestRepository getRepository() {
		return passwordResetRepository;
	}
	

	@Override
	public PasswordResetRequest createPasswordResetRequest(String email) {
		checkIfEmailIsValid(email);
		PasswordResetRequest passwordResetReq = new PasswordResetRequest();
		passwordResetReq.setEmail(email);
		passwordResetReq.setRequestDate(new DateTime());
		passwordResetReq.setEmailSender(emailSender);
		passwordResetReq.setEmailSenderName(emailSenderName);
		passwordResetReq.setEmailSubject(emailSubjectOnRequest);
		return super.saveOrUpdate(passwordResetReq);
	}
	
	@Override
	public PasswordResetRequest findById(ObjectId argId) {
		PasswordResetRequest request = super.findById(argId); 
		checkIfRequestExists(request);
		checkIfResetLinkIsValid(request);
		return request;
	}
	
	@Override
	public PasswordResetRequest processResetRequest(ObjectId passwordResetRequestId, String newPassword) {
		PasswordResetRequest passwordResetRequest = this.findById(passwordResetRequestId);
		passwordResetRequest.setEmailSender(emailSender);
		passwordResetRequest.setEmailSenderName(emailSenderName);
		passwordResetRequest.setEmailSubject(emailSubjectOnConfirm);
		User user = userService.getUserByUserName(passwordResetRequest.getEmail());
		userService.changePassword(user.getId(), newPassword);
		this.delete(passwordResetRequest);
		return passwordResetRequest;
	}

	private void checkIfEmailIsValid(String email) {
		userService.getUserByUserName(email);
	}
	
	private void checkIfRequestExists(PasswordResetRequest request) {
		if (request == null) {
			throw new PasswordResetRequestIsNotValid();
		}
	}
	
	private void checkIfResetLinkIsValid(PasswordResetRequest request) {
		if ( ! passwordResetLinkValidator.isValid(request)) {
			throw new PasswordResetLinkIsExpiredException();
		}
	}
}

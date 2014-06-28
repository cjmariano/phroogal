package com.phroogal.web.controller.service;

import static com.phroogal.web.context.WebApplicationContext.URI_USER_REQUEST_PASSWORD_RESET;
import static com.phroogal.web.context.WebApplicationContext.URI_USER_REQUEST_PASSWORD_RESET_GET;
import static com.phroogal.web.context.WebApplicationContext.URI_USER_REQUEST_PASSWORD_RESET_POST;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.phroogal.core.domain.User;
import com.phroogal.core.domain.security.PasswordResetRequest;
import com.phroogal.core.service.PasswordResetService;
import com.phroogal.core.service.Service;
import com.phroogal.core.service.UserService;
import com.phroogal.web.bean.PasswordResetRequestBean;
import com.phroogal.web.bean.UserCredentialsBean;
import com.phroogal.web.notification.PasswordResetCompleteNotification;
import com.phroogal.web.notification.PasswordResetNotification;
import com.wordnik.swagger.annotations.Api;

@Controller
@Api(value="password reset", description="Password Reset Operations", position = 4)
public class PasswordResetController extends BasicController<PasswordResetRequest, PasswordResetRequestBean, ObjectId> {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PasswordResetService passwordResetService;

	@Autowired
	@Qualifier(value="passwordResetNotification")
	private PasswordResetNotification passwordResetNotification; 
	
	@Autowired
	@Qualifier(value="passwordResetCompleteNotification")
	private PasswordResetCompleteNotification passwordResetCompleteNotification;
	
	@Override
	protected Service<PasswordResetRequest, ObjectId> returnDomainService() {
		return passwordResetService;
	}
	
	
	@RequestMapping(value = URI_USER_REQUEST_PASSWORD_RESET, method=RequestMethod.POST)
	public @ResponseBody
	Object requestPasswordReset(HttpServletRequest request, HttpServletResponse response, @RequestBody PasswordResetRequestBean passwordResetRequestBean) {
		String email = passwordResetRequestBean.getEmail(); 
		User user = userService.getUserByUserName(email);
		PasswordResetRequest passwordResetReq = passwordResetService.createPasswordResetRequest(email);
		passwordResetNotification.sendNotification(user, passwordResetReq);
		return null;
	}
	
	@RequestMapping(value = URI_USER_REQUEST_PASSWORD_RESET_POST, method = RequestMethod.POST)
	public @ResponseBody
	Object processPasswordReset(@PathVariable ObjectId id, HttpServletRequest request, HttpServletResponse response, @RequestBody UserCredentialsBean userCredentialsBean)  {
		String newPassword = userCredentialsBean.getPassword();
		if (newPassword != null) {
			PasswordResetRequest processedRequest = passwordResetService.processResetRequest(id, newPassword);
			User user = userService.getUserByUserName(processedRequest.getEmail());
			passwordResetCompleteNotification.sendNotification(user, processedRequest);
		}
		return null;
	}
	
	@RequestMapping(value = URI_USER_REQUEST_PASSWORD_RESET_GET, method = RequestMethod.GET)
	public @ResponseBody
	Object getPasswordReset(@PathVariable ObjectId id, HttpServletRequest request, HttpServletResponse response) {
		return super.getResource(id, request, response);
	}
}

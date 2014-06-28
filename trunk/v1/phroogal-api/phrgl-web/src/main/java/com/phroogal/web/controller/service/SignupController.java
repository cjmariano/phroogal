package com.phroogal.web.controller.service;

import static com.phroogal.web.context.WebApplicationContext.URI_SIGNUP_POST;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.phroogal.core.domain.User;
import com.phroogal.core.domain.UserProfile;
import com.phroogal.core.domain.security.EmailConfirmationRequest;
import com.phroogal.core.service.EmailConfirmationService;
import com.phroogal.core.service.SignupService;
import com.phroogal.web.bean.UserBean;
import com.phroogal.web.bean.UserSignupBean;
import com.phroogal.web.bean.mapper.MapperService;
import com.phroogal.web.notification.EmailConfirmationNotification;
import com.wordnik.swagger.annotations.Api;

@Controller
@Api(value="signup", description="Signup Operations", position = 2)
public class SignupController {
	
	@Autowired
	private SignupService signupService;
	
	@Autowired
	private EmailConfirmationService emailConfirmationService;
	
	@Autowired
	private MapperService<User, UserBean> userMapper;
	
	@Autowired
	private MapperService<UserProfile, UserSignupBean> userProfileMapper;

	@Autowired
	@Qualifier(value="emailConfirmationNotification")
	private EmailConfirmationNotification emailConfirmationNotification; 
	
	@RequestMapping(value = URI_SIGNUP_POST, method = RequestMethod.POST)
	public @ResponseBody Object signupUser(HttpServletRequest request, HttpServletResponse response, @RequestBody UserSignupBean userSignupBean)  {
		User user = new User();
		user.setProfile(userProfileMapper.toDomain(userSignupBean, UserProfile.class));
		signupService.signup(user);
		sendEmailConfirmationMessage(user);
		return userMapper.toBean(user, UserBean.class);
	}

	private void sendEmailConfirmationMessage(User newUser) {
		EmailConfirmationRequest emailConfirmationRequest = emailConfirmationService.createRequest(newUser.getProfile().getEmail());
		emailConfirmationNotification.sendNotification(newUser, emailConfirmationRequest);
	}
}

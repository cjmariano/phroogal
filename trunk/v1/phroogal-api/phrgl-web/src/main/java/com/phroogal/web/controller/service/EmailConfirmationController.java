package com.phroogal.web.controller.service;

import static com.phroogal.web.context.WebApplicationContext.PAGE_EMAIL_CONFIRMATION;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.phroogal.core.domain.User;
import com.phroogal.core.domain.security.EmailConfirmationRequest;
import com.phroogal.core.service.AuthenticationDetailsService;
import com.phroogal.core.service.EmailConfirmationService;
import com.phroogal.core.service.UserService;
import com.phroogal.web.notification.EmailConfirmationNotification;
import com.phroogal.web.notification.WelcomeMailNotification;
import com.wordnik.swagger.annotations.Api;

@Controller
@Api(value="email confirmation", description="Email Confirmation Operations", position = 3)
public class EmailConfirmationController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private EmailConfirmationService emailConfirmationService;
	
	@Autowired
	private AuthenticationDetailsService<ObjectId> authenticationService;
	
	@Autowired
	@Qualifier(value="welcomeMailNotification")
	private WelcomeMailNotification welcomeMailNotification; 
	
	@Autowired
	@Qualifier(value="emailConfirmationNotification")
	private EmailConfirmationNotification emailConfirmationNotification; 
	
	
	@RequestMapping(value = PAGE_EMAIL_CONFIRMATION, method = RequestMethod.GET, params="rid")
	public ModelAndView confirmUserEmail(@RequestParam("rid") String requestId, Model model, WebRequest request, RedirectAttributes redirectAttributes) {
		User user = emailConfirmationService.processRequest(new ObjectId(requestId));
		user.setNewUser(true);
		authenticationService.setAuthenticatedUser(user);
		welcomeMailNotification.sendWelcomeMail(user);
		return new ModelAndView("redirect:/");
	}
	
	@RequestMapping(value = PAGE_EMAIL_CONFIRMATION, method = RequestMethod.POST, params="resendToEmail")
	public @ResponseBody
	Object resendEmailConfirmationRequest(@RequestParam("resendToEmail") String email, Model model, WebRequest request, RedirectAttributes redirectAttributes) {
		User user = userService.getUserByUserName(email);
		EmailConfirmationRequest emailConfirmationRequest = emailConfirmationService.createRequest(email);
		emailConfirmationNotification.sendNotification(user, emailConfirmationRequest);
		return null;
	}
}

package com.phroogal.web.controller.service;

import static com.phroogal.web.context.WebApplicationContext.URI_LOGIN;
import static com.phroogal.web.context.WebApplicationContext.URI_LOGIN_PROVIDER;
import static com.phroogal.web.context.WebApplicationContext.URI_LOGIN_PROVIDER_USER;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.mangofactory.swagger.annotations.ApiIgnore;
import com.phroogal.core.domain.User;
import com.phroogal.core.domain.UserProfile;
import com.phroogal.core.exception.UserEmailIsSocialLoginException;
import com.phroogal.core.exception.UserEmailNotFoundException;
import com.phroogal.core.service.AuthenticationDetailsService;
import com.phroogal.core.service.LoginValidationService;
import com.phroogal.core.service.UserService;
import com.phroogal.core.valueobjects.UserCredentials;
import com.phroogal.web.bean.UserBean;
import com.phroogal.web.bean.UserCredentialsBean;
import com.phroogal.web.bean.mapper.MapperService;
import com.wordnik.swagger.annotations.Api;

@Controller
@Api(value="login", description="Login Operations", position = 1)
public class LoginController {
	
	@Autowired
	private MapperService<UserCredentials, UserCredentialsBean> userCredentialsMapper;
	
	@Autowired
	private MapperService<User, UserBean> userMapper;
	
	@Autowired
	private LoginValidationService validationService;
	
	@Autowired
	private UserService userService; 
	
	@Autowired
	private AuthenticationDetailsService<String> authenticationDetailsService;
	
	@Autowired
	@Qualifier("rememberMeServices")
	private PersistentTokenBasedRememberMeServices rememberMeServices;
	
	@Autowired
	@Qualifier("autologinRememberMeServices")
	private PersistentTokenBasedRememberMeServices autologinRememberMeServices;
	
	@Autowired
	private ConnectController connectController;
	
	@RequestMapping(value = URI_LOGIN, method = RequestMethod.POST, params={"_spring_security_remember_me", "redirect"})
	public @ResponseBody
	Object login(@RequestParam("redirect") String redirect, HttpServletRequest request, HttpServletResponse response, @RequestBody UserCredentialsBean userCredentialsBean) throws Throwable {
		try {
			UserCredentials userCredentials = userCredentialsMapper.toDomain(userCredentialsBean, UserCredentials.class);
			User user = validationService.validateCredentials(userCredentials);
			user.setLastLoginOn(DateTime.now());
			userService.saveOrUpdate(user);
			return new ModelAndView("redirect:".concat(StringUtils.isEmpty(redirect) ? "/" : redirect));
		} catch (UserEmailNotFoundException uenfe) {
			handleUsernameNotFoundException(userCredentialsBean.getUsername());
			throw uenfe;
		}
	}
	
	@RequestMapping(value = URI_LOGIN, method = RequestMethod.POST, params="_spring_security_remember_me")
	@Deprecated //Should use the method with redirect parameter
	public @ResponseBody
	Object loginDeprecated(HttpServletRequest request, HttpServletResponse response, @RequestBody UserCredentialsBean userCredentialsBean) throws Throwable {
		try {
			UserCredentials userCredentials = userCredentialsMapper.toDomain(userCredentialsBean, UserCredentials.class);
			User user = validationService.validateCredentials(userCredentials);
			user.setLastLoginOn(DateTime.now());
			userService.saveOrUpdate(user);
			return userMapper.toBean(user, UserBean.class);
		} catch (UserEmailNotFoundException uenfe) {
			handleUsernameNotFoundException(userCredentialsBean.getUsername());
			throw uenfe;
		}
	}

	@ApiIgnore
	@RequestMapping(value = URI_LOGIN_PROVIDER, method = RequestMethod.POST)
	public @ResponseBody
	Object loginProvider(@PathVariable String providerId, @RequestParam("redirect") String redirect, NativeWebRequest request, HttpServletResponse response) {
		request.setAttribute("redirectUri", redirect, RequestAttributes.SCOPE_SESSION);
		return connectController.connect(providerId, request);
	}
	
	@ApiIgnore
	@RequestMapping(value = URI_LOGIN_PROVIDER, method=RequestMethod.GET, params="oauth_token")
	public ModelAndView oauth1Callback(@PathVariable String providerId, NativeWebRequest webRequest, HttpServletRequest request, HttpServletResponse response) {
		connectController.oauth1Callback(providerId, webRequest);
		loginRememberMeServices(request, response, true);
		return new ModelAndView(resolveRedirect(webRequest));
	}

	@ApiIgnore
	@RequestMapping(value = URI_LOGIN_PROVIDER, method=RequestMethod.GET, params="code")
	public ModelAndView oauth2Callback(@PathVariable String providerId, @RequestParam("code") String code, NativeWebRequest webRequest, HttpServletRequest request, HttpServletResponse response) {
		connectController.oauth2Callback(providerId, webRequest);
		loginRememberMeServices(request, response, true);
		return new ModelAndView(resolveRedirect(webRequest));
	}

	@ApiIgnore
	@RequestMapping(value = URI_LOGIN_PROVIDER, method=RequestMethod.DELETE)
	public RedirectView removeConnections(@PathVariable String providerId, NativeWebRequest request) {
		return connectController.removeConnections(providerId, request);
	}

	@ApiIgnore
	@RequestMapping(value = URI_LOGIN_PROVIDER_USER, method=RequestMethod.DELETE)
	public RedirectView removeConnection(@PathVariable String providerId, @PathVariable String providerUserId, NativeWebRequest request) {
		return connectController.removeConnection(providerId, providerUserId, request);
	}
	
	private String resolveRedirect(NativeWebRequest webRequest) {
		StringBuffer sb = new StringBuffer("redirect:");
		String redirectUri = (String) webRequest.getAttribute("redirectUri", RequestAttributes.SCOPE_SESSION);
		sb.append(StringUtils.isEmpty(redirectUri) ? "/" : redirectUri);
		return sb.toString();
	}
	
	private void loginRememberMeServices(HttpServletRequest request, HttpServletResponse response, boolean autoLogin) {
		PersistentTokenBasedRememberMeServices service = autoLogin ? autologinRememberMeServices : rememberMeServices;
		if (!authenticationDetailsService.getAuthentication().equals(UserProfile.ANONYMOUS_ROLE)) {
			service.loginSuccess(request, response, authenticationDetailsService.getAuthentication());	
		}
	}
	
	private void handleUsernameNotFoundException(String username) {
		if( userService.isEmailAssociatedWithSocialLogin(username) ) {
			throw new UserEmailIsSocialLoginException();
		}
	}
}

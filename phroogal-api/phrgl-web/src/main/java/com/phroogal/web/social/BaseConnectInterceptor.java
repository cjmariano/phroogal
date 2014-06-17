package com.phroogal.web.social;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.web.ConnectInterceptor;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import com.phroogal.core.domain.SocialProfile;
import com.phroogal.core.domain.User;
import com.phroogal.core.domain.UserProfile;
import com.phroogal.core.service.AuthenticationDetailsService;
import com.phroogal.core.service.FileUploadService;
import com.phroogal.core.service.UserService;
import com.phroogal.core.social.SocialNetwork;
import com.phroogal.core.social.SocialNetworkResolver;

/**
 * Default interceptor for functionalities before and/or after connecting to the social network
 * 
 * @author Christopher Mariano
 */
public class BaseConnectInterceptor<T> implements ConnectInterceptor<T> {

	private static final String PERMISSIONS_KEY = "scope"; 
	private String permissions = StringUtils.EMPTY;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private FileUploadService fileUploadService;
	
	@Autowired
	@Qualifier("socialConnectPostProcess")
	private SocialConnectPostProcess<T> socialConnectPostProcess;
	
	@Autowired
	private AuthenticationDetailsService<String> authenticationService;
	
	@Autowired
	private SocialNetworkResolver socialNetworkResolver;
	
	@Override
	public void preConnect(ConnectionFactory<T> connectionFactory, MultiValueMap<String, String> parameters, WebRequest request) {
		String redirectUri = (String) request.getAttribute("redirectUri", RequestAttributes.SCOPE_SESSION);
		parameters.putAll(getScopeParameters(redirectUri));
	}

	@Override
	public void postConnect(final Connection<T> connection, WebRequest request) {
		final User user = getCurrentUser(connection);
		updateUserSocialProfile(connection, user);
		authenticationService.setAuthenticatedUser(user);
		user.setLastLoginOn(DateTime.now());
		userService.saveOrUpdate(user);
		socialConnectPostProcess.execute(connection, user);
	}

	private Map<String, List<String>> getScopeParameters(String uri) {
		Map<String, List<String>> scopeParameters = new HashMap<String, List<String>>();
		scopeParameters.put(PERMISSIONS_KEY, Arrays.asList(new String[] {permissions}));
		scopeParameters.put("redirect_uri", Arrays.asList(new String[] {uri}));
		return scopeParameters;
	}

	private User getCurrentUser(Connection<T> connection) {
		User principal = authenticationService.getAuthenticatedUser();
		return principal == null ? userService.getUserFromConnection(connection) : principal;
	}
	
	private void updateUserSocialProfile(Connection<T> connection, User user) {
		SocialNetwork api = socialNetworkResolver.getApi(connection);
		user.connectSocialProfile(SocialProfile.createSocialProfile(api));
		updateUserSocialProfilePicUrl(api, user);
	}
	
	private void updateUserSocialProfilePicUrl(SocialNetwork api, User user) {
		UserProfile profile = user.getProfile();
		if ( ! isUploadedProfilePic(profile.getProfilePictureUrl())) {
			profile.setProfilePictureUrl(api.getProfilePictureUrl());
		}
	}

	private boolean isUploadedProfilePic(String profilePicUrl) {
		return profilePicUrl.contains(fileUploadService.getUploadDirectory());
	}

	public String getPermissions() {
		return permissions;
	}

	public void setPermissions(String permissions) {
		this.permissions = permissions;
	}
}
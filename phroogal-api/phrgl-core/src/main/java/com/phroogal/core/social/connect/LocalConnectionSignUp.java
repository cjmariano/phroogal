/**
 * 
 */
package com.phroogal.core.social.connect;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;

import com.phroogal.core.domain.SocialProfile;
import com.phroogal.core.domain.User;
import com.phroogal.core.domain.UserProfile;
import com.phroogal.core.service.UserService;
import com.phroogal.core.service.UserTagService;
import com.phroogal.core.social.SocialNetwork;
import com.phroogal.core.social.SocialNetworkResolver;

/**
 * Handles local user persistence when a user logins through a social network
 * @author Christopher Mariano
 *
 */
public class LocalConnectionSignUp implements ConnectionSignUp {

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserTagService userTagService;
	
	@Autowired
	private SocialNetworkResolver socialNetworkResolver;
	
	@Override
	public String execute(Connection<?> connection) {
		User user = new User();
		SocialNetwork api = socialNetworkResolver.getApi(connection);
		user.setPrimarySocialNetworkConnection(api.getSocialNetworkType());
		user.setUsername(SocialProfile.createUniqueHandle(connection));
		user.setProfile(generateUserProfileFromConnection(api));
		userService.saveOrUpdate(user);
		userTagService.createDefaultUserTagsFor(user);		
		return user.getId().toString();
	}

	private UserProfile generateUserProfileFromConnection(SocialNetwork api) {
		UserProfile userProfile = UserProfile.createUserProfile(api);
		userProfile.setEmail(api.getPrimaryEmail());
		return userProfile;
	}
}

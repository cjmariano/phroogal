package com.phroogal.web.social;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.social.connect.Connection;
import org.springframework.stereotype.Service;

import com.phroogal.core.domain.User;
import com.phroogal.core.service.SocialContactService;
import com.phroogal.core.service.UserSocialContactService;

/**
 * 
 * @author Christopher Mariano
 *
 */
@Service("socialConnectPostProcess")
public class SocialConnectPostProcessImpl<T> implements SocialConnectPostProcess<T> {

	@Autowired
	private SocialContactService socialContactService;
	
	@Autowired
	private UserSocialContactService userSocialContactService;
	
	@Async
	public void execute(Connection<T> connection, User user) {
		socialContactService.synchSocialNetworkContacts(user, connection);
		userSocialContactService.refreshUserSocialContact(user);
	}
}

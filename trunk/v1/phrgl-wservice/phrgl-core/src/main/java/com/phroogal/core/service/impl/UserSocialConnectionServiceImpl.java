package com.phroogal.core.service.impl;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.stereotype.Service;

import com.phroogal.core.domain.UserSocialConnection;
import com.phroogal.core.repository.UserSocialConnectionRepository;
import com.phroogal.core.service.UserSocialConnectionService;

/**
 * Default implementation of the {@link UserSocialConnectionService} interface
 * @author Christopher Mariano
 *
 */
@Service
public class UserSocialConnectionServiceImpl extends BaseService<UserSocialConnection, ObjectId, UserSocialConnectionRepository> implements UserSocialConnectionService {

	@Autowired
	private UserSocialConnectionRepository userSocialConnectionRepository;
	
	@Override
	protected UserSocialConnectionRepository getRepository() {
		return userSocialConnectionRepository;
	}
	
	@Override
	public UserSocialConnection retrieveUserSocialConnection(Connection<?> connection) {
		String providerId = connection.createData().getProviderId();
		String providerUserId = connection.createData().getProviderUserId();
		UserSocialConnection userConnection = userSocialConnectionRepository.findByProviderIdAndProviderUserId(providerId, providerUserId).get(0);
		return userConnection;
	}
}

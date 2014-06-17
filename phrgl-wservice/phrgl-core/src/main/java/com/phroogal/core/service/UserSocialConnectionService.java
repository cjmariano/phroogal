package com.phroogal.core.service;

import org.bson.types.ObjectId;
import org.springframework.social.connect.Connection;

import com.phroogal.core.domain.UserSocialConnection;

/**
 * Service for UserSocialConnection functions
 * @author Christopher Mariano
 *
 */
public interface UserSocialConnectionService extends Service<UserSocialConnection, ObjectId> {

	/**
	 * Retrieves a user social connection from the current connection given 
	 * @param connection that is established from connecting to a social network
	 * @return the {@link UserSocialConnection} from the given connection
	 */
	public UserSocialConnection retrieveUserSocialConnection(Connection<?> connection);
}

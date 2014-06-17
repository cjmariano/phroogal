package com.phroogal.web.social;

import org.springframework.social.connect.Connection;

import com.phroogal.core.domain.User;

/**
 * 
 * @author Christopher Mariano
 *
 */
public interface SocialConnectPostProcess<T> {

	public void execute(Connection<T> connection, User user);
	
}

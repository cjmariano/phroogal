/**
 * 
 */
package com.phroogal.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.phroogal.core.domain.User;
import com.phroogal.core.service.SignupService;
import com.phroogal.core.service.UserService;
import com.phroogal.core.service.UserTagService;

/**
 * Default implementation of the {@link SignupService} interface
 * @author Christopher Mariano
 *
 */
@Service
public class SignupServiceImpl implements SignupService {

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserTagService userTagService;
	
	@Autowired
	@Qualifier("passwordEncoder")
	private PasswordEncoder passwordEncoder;
	
	@Override
	public User signup(User user) {
		setEmailAsUsername(user);
		encryptUserPassword(user);
		User newUser = userService.saveOrUpdate(user);
		userTagService.createDefaultUserTagsFor(newUser);
		return newUser;
	}

	private void setEmailAsUsername(User user) {
		user.setUsername(user.getProfile().getEmail());
	}

	private void encryptUserPassword(User user) {
		String rawPassword = user.getPassword();
		if (user != null && rawPassword != null) {
			String encryptedPassword = passwordEncoder.encode(rawPassword);
			user.getProfile().setPassword(encryptedPassword);	
		}
	}
}

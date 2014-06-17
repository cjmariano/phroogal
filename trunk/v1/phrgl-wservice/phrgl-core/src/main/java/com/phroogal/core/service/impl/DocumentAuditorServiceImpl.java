package com.phroogal.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Service;

import com.phroogal.core.domain.User;

/**
 * An implementation of {@link AuditorAware} that manages auditing functions for data transactions
 * @author c.j.mariano
 *
 */
@Service("documentAuditor")
public class DocumentAuditorServiceImpl implements AuditorAware<User>{

	@Autowired
	AuthenticationDetailsServiceImpl authenticationService;
	
	@Override
	public User getCurrentAuditor() {
		return authenticationService.getAuthenticatedUser();
	}
}

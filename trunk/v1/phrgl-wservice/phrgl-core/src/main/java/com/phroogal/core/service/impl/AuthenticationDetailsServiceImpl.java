package com.phroogal.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.phroogal.core.domain.User;
import com.phroogal.core.domain.UserProfile;
import com.phroogal.core.exception.UserNotAuthenticatedException;
import com.phroogal.core.repository.UserRepository;
import com.phroogal.core.service.AuthenticationDetailsService;
import com.phroogal.core.utility.CollectionUtil;
import com.phroogal.core.valueobjects.UserCredentials;

/**
 * Default implementation for {@link AuthenticationDetailsService}
 * @author Christopher Mariano
 *
 */
@Service("authenticationDetailsService")
public class AuthenticationDetailsServiceImpl implements AuthenticationDetailsService<String> {
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public String getAuthenticatedUserId() {
		User userDetails = getSecurityContextPrincipal();
		return userDetails != null ?  userDetails.getId().toString() : UserProfile.ANONYMOUS_ROLE;
	}

	@Override
	public void setAuthenticatedUser(User user) {
		List<GrantedAuthority> authorities = CollectionUtil.arrayList();
		UserCredentials userCredentials = new UserCredentials(user.getUsername(), user.getPassword());
        Authentication auth = new UsernamePasswordAuthenticationToken(user, userCredentials, authorities);
        SecurityContextHolder.getContext().setAuthentication(auth);
	}

	@Override
	public User getAuthenticatedUser() {
		return getSecurityContextPrincipal();
	}
	
	@Override
	public Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	@Override
	public void verifyUserIsAuthenticated() throws UserNotAuthenticatedException {
		if (getAuthenticatedUser() == null) {
			throw new UserNotAuthenticatedException();
		}
	}
	
	private User getSecurityContextPrincipal() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && auth.getPrincipal() instanceof User) {
			User user = (User) auth.getPrincipal();
			return userRepository.findOne(user.getId());
		}
		return null;
	}
}

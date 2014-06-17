package com.phroogal.core.service.impl;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.phroogal.core.domain.User;
import com.phroogal.core.domain.analytics.UserSearchHistory;
import com.phroogal.core.repository.analytics.UserSearchHistoryRepository;
import com.phroogal.core.service.AuthenticationDetailsService;
import com.phroogal.core.service.UserSearchHistoryService;

/**
 * Default implementation of the {@link UserSearchHistoryService} interface
 * @author Christopher Mariano
 *
 */
@Service
public class UserSearchHistoryServiceImpl extends BaseService<UserSearchHistory, ObjectId, UserSearchHistoryRepository> implements UserSearchHistoryService{

	@Autowired
	private UserSearchHistoryRepository userSearchHistoryRepository;
	
	@Autowired
	private AuthenticationDetailsService<ObjectId> authenticationService;
	
	@Override
	protected UserSearchHistoryRepository getRepository() {
		return userSearchHistoryRepository;
	}

	@Async
	@Override
	public void addKeywordToUserSearchHistory(String keyword) {
		User user = authenticationService.getAuthenticatedUser();
		if (user != null) {
			UserSearchHistory userSearchHistory = retrieveUserSearchHistory(user);
			userSearchHistory.addSearchTerm(keyword);
			userSearchHistoryRepository.save(userSearchHistory);	
		}
	}

	private UserSearchHistory retrieveUserSearchHistory(User user) {
		UserSearchHistory userSearchHistory = userSearchHistoryRepository.findByUserId(user.getId());
		if (userSearchHistory == null) {
			userSearchHistory = new UserSearchHistory();
			userSearchHistory.setUserId(user.getId());
			userSearchHistory.setUserPrimaryEmail(user.getProfile().getEmail());
		}
		return userSearchHistory;
	}
}

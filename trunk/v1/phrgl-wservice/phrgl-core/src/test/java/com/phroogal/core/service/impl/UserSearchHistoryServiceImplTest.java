package com.phroogal.core.service.impl;

import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.phroogal.core.domain.User;
import com.phroogal.core.domain.analytics.UserSearchHistory;
import com.phroogal.core.repository.TestEntityGenerator;
import com.phroogal.core.repository.analytics.UserSearchHistoryRepository;
import com.phroogal.core.service.AuthenticationDetailsService;
import com.phroogal.core.service.Service;
import com.phroogal.core.service.UserSearchHistoryService;
import com.phroogal.core.test.helper.UnwrapProxyHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/spring/app-context.xml"})
public class UserSearchHistoryServiceImplTest extends BaseServiceTest<UserSearchHistory, ObjectId, UserSearchHistoryRepository> {

	@Autowired
	private UserSearchHistoryService serviceImpl;
	
	@Autowired
	private UserSearchHistoryRepository userSearchHistoryRepository;
	
	@Override
	@SuppressWarnings("unchecked")
	protected Service<UserSearchHistory, ObjectId> returnServiceImpl() {
		return (Service<UserSearchHistory, ObjectId>) UnwrapProxyHelper.unwrapProxy(UserSearchHistoryServiceImpl.class, serviceImpl);
	}
	
	@Override
	protected UserSearchHistoryRepository returnMongoRepository() {
		return Mockito.mock(UserSearchHistoryRepository.class);
	}

	@Override
	protected UserSearchHistory returnDomainInstance() {
		TestEntityGenerator generator = new TestEntityGenerator(); 
		return generator.generateTestUserSearchHistory();
	}
	
	@Test
	@SuppressWarnings("rawtypes")
	public void testAddKeywordToUserSearchHistory() throws Exception {
		AuthenticationDetailsService authenticationService = Mockito.mock(AuthenticationDetailsService.class);
		serviceImpl = (UserSearchHistoryService) returnServiceImpl();
		ReflectionTestUtils.setField(serviceImpl, "authenticationService", authenticationService);
		TestEntityGenerator generator = new TestEntityGenerator();
		User user = generator.generateTestUser();
		Mockito.when(authenticationService.getAuthenticatedUser()).thenReturn(user);
		
		assertThisKeywordIsAddedToUserHistory(user, "What is a credit card");
		assertThisKeywordIsAddedToUserHistory(user, "How can i improve my credit score");
		assertThisKeywordIsAddedToUserHistory(user, "mortgage");
		
		UserSearchHistory userSearchHistory = userSearchHistoryRepository.findByUserId(user.getId());
		Assert.assertTrue(userSearchHistory.getSearchTerms().size() == 3);
	}

	private void assertThisKeywordIsAddedToUserHistory(User user, String keyword) {
		serviceImpl.addKeywordToUserSearchHistory(keyword);
		UserSearchHistory userSearchHistory = userSearchHistoryRepository.findByUserId(user.getId());
		Assert.assertTrue(userSearchHistory.getSearchTerms().contains(keyword.toLowerCase()));
	}
}

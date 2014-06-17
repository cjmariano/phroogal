package com.phroogal.core.repository.analytics;

import java.util.List;

import junit.framework.AssertionFailedError;

import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.phroogal.core.domain.analytics.UserSearchHistory;
import com.phroogal.core.repository.BaseRepositoryTest;
import com.phroogal.core.repository.TestEntityGenerator;
import com.phroogal.core.utility.CollectionUtil;

public class UserSearchHistoryRepositoryTest extends BaseRepositoryTest<UserSearchHistory, ObjectId, UserSearchHistoryRepository> {

	@Autowired
	private UserSearchHistoryRepository repository;
	
	@Override
	protected UserSearchHistoryRepository returnRepository() {
		return repository;
	}

	@Override
	protected Class<UserSearchHistory> returnDomainClass() {
		return UserSearchHistory.class;
	}

	@Override
	protected UserSearchHistory createDomain() {
		TestEntityGenerator generator = new TestEntityGenerator();
		UserSearchHistory userSearchHistory = generator.generateTestUserSearchHistory();
		return userSearchHistory;
	}

	@Override
	protected void modifyDomain(UserSearchHistory userSearchHistory) {
		userSearchHistory.setUserPrimaryEmail("a@b.com.ph");
	}

	@Override
	protected boolean assertDomainEquals(UserSearchHistory domain, UserSearchHistory other) {
		try {
			Assert.assertEquals(domain.getId(), other.getId());
			Assert.assertEquals(domain.getUserPrimaryEmail(), other.getUserPrimaryEmail());
			Assert.assertEquals(domain.getUserId(), other.getUserId());
			Assert.assertEquals(domain.getSearchTerms().size(), other.getSearchTerms().size());
			Assert.assertTrue(domain.getSearchTerms().containsAll(other.getSearchTerms()));
			return true;
		} catch (AssertionFailedError e) {
			return false;
		}
	}
	
	@Test
	public void testFindByUserId() throws Exception {
		ObjectId userId = ObjectId.get();
		UserSearchHistory expectedDomain = createDomain();
		expectedDomain.setUserId(userId);
		repository.save(expectedDomain);
		
		UserSearchHistory notMatchedDomain = createDomain();
		notMatchedDomain.setUserId(ObjectId.get());
		repository.save(notMatchedDomain);
		
		UserSearchHistory queryResult = repository.findByUserId(userId);
		List<ObjectId> expectedDomainIds = CollectionUtil.arrayList();
		expectedDomainIds.add(expectedDomain.getId());
		Assert.assertTrue(queryResult.getId().equals(expectedDomain.getId()));
	}
}

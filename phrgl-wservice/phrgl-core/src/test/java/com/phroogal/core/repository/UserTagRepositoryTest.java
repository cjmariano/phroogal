package com.phroogal.core.repository;

import java.util.Arrays;
import java.util.List;

import junit.framework.AssertionFailedError;

import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.phroogal.core.domain.UserTag;

public class UserTagRepositoryTest extends BaseRepositoryTest<UserTag, ObjectId, UserTagRepository> {

	@Autowired
	private UserTagRepository repository;
	
	@Override
	protected UserTagRepository returnRepository() {
		return repository;
	}

	@Override
	protected Class<UserTag> returnDomainClass() {
		return UserTag.class;
	}

	@Override
	protected UserTag createDomain() {
		TestEntityGenerator generator = new TestEntityGenerator();
		return generator.generateTestUserTag();
	}

	@Override
	protected void modifyDomain(UserTag domain) {
		domain.setName(domain.getName() + "-modified");
	}

	@Override
	protected boolean assertDomainEquals(UserTag domain, UserTag other) {
		try {
			Assert.assertEquals(domain.getId(), other.getId());
			Assert.assertEquals(domain.getName(), other.getName());
			Assert.assertEquals(domain.getUserId(), other.getUserId());
			return true;
		} catch (AssertionFailedError e) {
			return false;
		}
	}
	
	@Test
	public void testFindByUserId() {
		ObjectId testUserId = ObjectId.get();
		UserTag expectedDomain = createDomain();
		expectedDomain.setUserId(testUserId);
		repository.save(expectedDomain);
		
		UserTag notMatchedDomain = createDomain();
		notMatchedDomain.setUserId(ObjectId.get());
		repository.save(notMatchedDomain);
		
		List<UserTag> queryResult = repository.findByUserId(testUserId);
		Assert.assertTrue(queryResult.size() == 1);
		assertListIDsAreContainedInPersistedItems(Arrays.asList(expectedDomain.getId()), queryResult);
	}
	
	@Test
		public void testFindByUserIdAndName() {
			ObjectId userId = ObjectId.get();
			String tagName = "Test Tag";
			UserTag expectedDomain = createDomain();
			expectedDomain.setName(tagName);
			expectedDomain.setUserId(userId);
			repository.save(expectedDomain);
			
			UserTag notMatchedDomain = createDomain();
			ObjectId otherUserId = ObjectId.get();
			notMatchedDomain.setName("NoMatch");
			notMatchedDomain.setUserId(otherUserId);
			repository.save(notMatchedDomain);
			
			List<UserTag> queryResult = repository.findByUserIdAndName(userId, tagName);
			Assert.assertTrue(queryResult.size() == 1);
			assertListIDsAreContainedInPersistedItems(Arrays.asList(expectedDomain.getId()), queryResult);
		}

}

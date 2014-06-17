package com.phroogal.core.repository;

import java.util.List;

import junit.framework.AssertionFailedError;

import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.phroogal.core.domain.UserSocialContact;
import com.phroogal.core.utility.CollectionUtil;

public class UserSocialContactRepositoryTest extends BaseRepositoryTest<UserSocialContact, ObjectId, UserSocialContactRepository> {

	@Autowired
	private UserSocialContactRepository repository;
	
	@Override
	protected UserSocialContactRepository returnRepository() {
		return repository;
	}

	@Override
	protected Class<UserSocialContact> returnDomainClass() {
		return UserSocialContact.class;
	}

	@Override
	protected UserSocialContact createDomain() {
		TestEntityGenerator generator = new TestEntityGenerator();
		return generator.generateTestUserSocialContact();
	}

	@Override
	protected void modifyDomain(UserSocialContact domain) {
		TestEntityGenerator generator = new TestEntityGenerator();
		UserSocialContact userSocialContact = generator.generateTestUserSocialContact();
		domain.addSocialContact(userSocialContact.getSocialContactIds());
	}

	@Override
	protected boolean assertDomainEquals(UserSocialContact domain, UserSocialContact other) {
		try {
			Assert.assertEquals(domain.getId(), other.getId());
			Assert.assertEquals(domain.getUserId(), other.getUserId());
			Assert.assertEquals(domain.getSocialContactIds().size(), other.getSocialContactIds().size());
			return true;
		} catch (AssertionFailedError e) {
			return false;
		}
	}
	
	@Test
	public void testFindByUserId() throws Exception {
		ObjectId userId = ObjectId.get();
		UserSocialContact expectedDomain = createDomain();
		expectedDomain.setUserId(userId);
		repository.save(expectedDomain);
		
		UserSocialContact notMatchedDomain = createDomain();
		notMatchedDomain.setUserId(ObjectId.get());
		repository.save(notMatchedDomain);
		
		UserSocialContact queryResult = repository.findByUserId(userId);
		List<ObjectId> expectedDomainIds = CollectionUtil.arrayList();
		expectedDomainIds.add(expectedDomain.getId());
		Assert.assertTrue(queryResult.getId().equals(expectedDomain.getId()));
	}

}

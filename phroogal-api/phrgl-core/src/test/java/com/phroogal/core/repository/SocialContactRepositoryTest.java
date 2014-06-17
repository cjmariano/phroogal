package com.phroogal.core.repository;

import java.util.List;

import junit.framework.AssertionFailedError;

import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.phroogal.core.domain.Persistent;
import com.phroogal.core.domain.SocialContact;
import com.phroogal.core.social.SocialNetworkType;

public class SocialContactRepositoryTest extends BaseRepositoryTest<SocialContact, ObjectId, SocialContactRepository> {

	@Autowired
	private SocialContactRepository repository;
	
	@Override
	protected SocialContactRepository returnRepository() {
		return repository;
	}

	@Override
	protected Class<SocialContact> returnDomainClass() {
		return SocialContact.class;
	}

	@Override
	protected SocialContact createDomain() {
		TestEntityGenerator generator = new TestEntityGenerator();
		SocialContact socialContact = generator.generateTestSocialContact();
		return socialContact;
	}

	@Override
	protected void modifyDomain(SocialContact socialContact) {
		socialContact.setContactUserId(socialContact.getContactUserId() + "-modified");
	}

	@Override
	protected boolean assertDomainEquals(SocialContact domain, SocialContact other) {
		try {
			Assert.assertEquals(domain.getId(), other.getId());
			Assert.assertEquals(domain.getContactUserId(), other.getContactUserId());
			Assert.assertEquals(domain.getConnectedThru(), other.getConnectedThru());
			Assert.assertEquals(domain.getUserId(), other.getUserId());
			return true;
		} catch (AssertionFailedError e) {
			return false;
		}
	}
	
	@Test
	public void testFindByUserIdAndConnectedThru() {
		ObjectId userId = ObjectId.get();
		SocialContact contact1 = createAndPersistTestSocialContact(userId, SocialNetworkType.FACEBOOK);
		SocialContact contact2 = createAndPersistTestSocialContact(userId, SocialNetworkType.LINKEDIN);
		SocialContact contact3 = createAndPersistTestSocialContact(userId, SocialNetworkType.LINKEDIN);
		SocialContact contact4 = createAndPersistTestSocialContact(userId, SocialNetworkType.LINKEDIN);
		
		List<SocialContact> results = repository.findByUserIdAndConnectedThru(userId, SocialNetworkType.FACEBOOK);
		Assert.assertTrue(results.size() == 1);
		Assert.assertTrue(idContainedInList(contact1.getId(), results));
		
		results = repository.findByUserIdAndConnectedThru(userId, SocialNetworkType.LINKEDIN);
		Assert.assertTrue(results.size() == 3);
		Assert.assertTrue(idContainedInList(contact2.getId(), results));
		Assert.assertTrue(idContainedInList(contact3.getId(), results));
		Assert.assertTrue(idContainedInList(contact4.getId(), results));
	}
	
	@Test
	public void testFindByUserId() {
		ObjectId userId = ObjectId.get();
		ObjectId userIdOther = ObjectId.get();
		SocialContact contact1 = createAndPersistTestSocialContact(userId, SocialNetworkType.FACEBOOK);
		createAndPersistTestSocialContact(userIdOther, SocialNetworkType.FACEBOOK);
		
		List<SocialContact> results = repository.findByUserId(userId);
		Assert.assertTrue(results.size() == 1);
		Assert.assertTrue(idContainedInList(contact1.getId(), results));
	}
	
	@Test
	public void testFindByContactUserIdAndConnectedThru() {
		final String contactUserId = "12345";
		final SocialNetworkType connectedThru = SocialNetworkType.FACEBOOK;
		ObjectId userId = ObjectId.get();
		ObjectId userIdOther = ObjectId.get();
		
		SocialContact contact1 = createAndPersistTestSocialContact(userId, connectedThru);
		contact1.setContactUserId(contactUserId);
		repository.save(contact1);
		
		createAndPersistTestSocialContact(userIdOther, connectedThru);
		
		List<SocialContact> results = repository.findByContactUserIdAndConnectedThru(contactUserId, connectedThru);
		Assert.assertTrue(results.size() == 1);
		Assert.assertTrue(idContainedInList(contact1.getId(), results));
	}

	private SocialContact createAndPersistTestSocialContact(ObjectId userId, SocialNetworkType connectedThru) {
		SocialContact contact = new SocialContact();
		contact.setUserId(userId);
		contact.setConnectedThru(connectedThru);
		return repository.save(contact);
	}
	
	private boolean idContainedInList(ObjectId id, List<? extends Persistent<ObjectId>> list) {
		boolean idContainedInList = false;
		for (Persistent<ObjectId> each : list) {
			if (each.getId().equals(id)) {
				return true;
			}
		}
		return idContainedInList;
	}
}

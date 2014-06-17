package com.phroogal.core.repository;

import java.util.List;

import junit.framework.AssertionFailedError;

import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import com.phroogal.core.domain.FlagNotificationRequest;
import com.phroogal.core.domain.FlagNotificationStatusType;
import com.phroogal.core.domain.PostType;
import com.phroogal.core.service.AuthenticationDetailsService;
import com.phroogal.core.service.UserService;

public class FlagNotificationRequestRepositoryTest extends BaseRepositoryTest<FlagNotificationRequest, ObjectId, FlagNotificationRequestRepository> {

	@Autowired
	private FlagNotificationRequestRepository repository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthenticationDetailsService<ObjectId> authenticationService;
	
	@Override
	protected FlagNotificationRequestRepository returnRepository() {
		return repository;
	}

	@Override
	protected Class<FlagNotificationRequest> returnDomainClass() {
		return FlagNotificationRequest.class;
	}

	@Override
	protected FlagNotificationRequest createDomain() {
		TestEntityGenerator generator = new TestEntityGenerator();
		FlagNotificationRequest flagNotificationRequest = generator.generateTestFlagNotificationRequest();
		return flagNotificationRequest;
	}

	@Override
	protected void modifyDomain(FlagNotificationRequest flagNotificationRequest) {
		flagNotificationRequest.setContent("This is a modified content");
	}

	@Override
	protected boolean assertDomainEquals(FlagNotificationRequest domain, FlagNotificationRequest other) {
		try {
			Assert.assertEquals(domain.getId(), other.getId());
			Assert.assertEquals(domain.getContent(), other.getContent());
			Assert.assertEquals(domain.getRefId(), other.getRefId());
			return true;
		} catch (AssertionFailedError e) {
			return false;
		}
	}
	
	@Test
	public void testFindByRefIdAndType() {
		ObjectId refId = ObjectId.get();
		FlagNotificationRequest expectedDomain = createDomain();
		expectedDomain.setRefId(refId);
		expectedDomain.setType(PostType.QUESTION);
		repository.save(expectedDomain);
		
		FlagNotificationRequest notMatchedDomain = createDomain();
		notMatchedDomain.setRefId(ObjectId.get());
		notMatchedDomain.setType(PostType.QUESTION);
		repository.save(notMatchedDomain);
		
		List<FlagNotificationRequest> queryResult = repository.findByRefIdAndType(refId, PostType.QUESTION);
		Assert.assertTrue(queryResult.size() == 1);
		assertDomainEquals(expectedDomain, queryResult.get(0));
	}
	
	@Test
	public void testFindByStatusAndType() {
		ObjectId refId = ObjectId.get();
		FlagNotificationRequest expectedDomain1 = createDomain();
		expectedDomain1.setRefId(refId);
		expectedDomain1.setStatus(FlagNotificationStatusType.DELETED);
		expectedDomain1.setType(PostType.QUESTION);
		repository.save(expectedDomain1);
		
		FlagNotificationRequest expectedDomain2 = createDomain();
		expectedDomain2.setRefId(ObjectId.get());
		expectedDomain2.setStatus(FlagNotificationStatusType.DELETED);
		expectedDomain2.setType(PostType.QUESTION);
		repository.save(expectedDomain2);
		
		FlagNotificationRequest notMatchedDomain = createDomain();
		notMatchedDomain.setRefId(ObjectId.get());
		notMatchedDomain.setStatus(FlagNotificationStatusType.ACTIVE);
		notMatchedDomain.setType(PostType.QUESTION);
		repository.save(notMatchedDomain);
		
		List<FlagNotificationRequest> queryResult = repository.findByStatusAndType(FlagNotificationStatusType.DELETED, PostType.QUESTION, new PageRequest(0,10));
		Assert.assertTrue(queryResult.size() == 2);
		assertDomainEquals(expectedDomain1, queryResult.get(0));
		assertDomainEquals(expectedDomain2, queryResult.get(1));
	}
}

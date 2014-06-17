package com.phroogal.core.repository;

import junit.framework.AssertionFailedError;

import org.bson.types.ObjectId;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import com.phroogal.core.domain.security.PasswordResetRequest;

public class PasswordResetRequestRepositoryTest extends BaseRepositoryTest<PasswordResetRequest, ObjectId, PasswordResetRequestRepository> {

	@Autowired
	private PasswordResetRequestRepository repository;
	
	@Override
	protected PasswordResetRequestRepository returnRepository() {
		return repository;
	}

	@Override
	protected Class<PasswordResetRequest> returnDomainClass() {
		return PasswordResetRequest.class;
	}

	@Override
	protected PasswordResetRequest createDomain() {
		TestEntityGenerator generator = new TestEntityGenerator();
		PasswordResetRequest passwordResetRequest = generator.generateTestPasswordResetRequest();
		return passwordResetRequest;
	}

	@Override
	protected void modifyDomain(PasswordResetRequest domain) {
		domain.setEmail("modified-" + domain.getEmail());
	}

	@Override
	protected boolean assertDomainEquals(PasswordResetRequest domain, PasswordResetRequest other) {
		try {
			Assert.assertEquals(domain.getId(), other.getId());
			Assert.assertEquals(domain.getEmail(), other.getEmail());
			Assert.assertEquals(domain.getRequestDate(), other.getRequestDate());
			return true;
		} catch (AssertionFailedError e) {
			return false;
		}
	}
}

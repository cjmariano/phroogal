package com.phroogal.core.repository;

import static org.junit.Assert.assertNotNull;
import junit.framework.AssertionFailedError;

import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.phroogal.core.domain.security.EmailConfirmationRequest;

public class EmailConfirmationRequestRepositoryTest extends BaseRepositoryTest<EmailConfirmationRequest, ObjectId, EmailConfirmationRequestRepository> {

	@Autowired
	private EmailConfirmationRequestRepository repository;
	
	@Override
	protected EmailConfirmationRequestRepository returnRepository() {
		return repository;
	}

	@Override
	protected Class<EmailConfirmationRequest> returnDomainClass() {
		return EmailConfirmationRequest.class;
	}

	@Override
	protected EmailConfirmationRequest createDomain() {
		TestEntityGenerator generator = new TestEntityGenerator();
		EmailConfirmationRequest emailConfirmationRequest = generator.generateTestEmailRepositoryRequest();
		return emailConfirmationRequest;
	}

	@Override
	protected void modifyDomain(EmailConfirmationRequest domain) {
		domain.setEmail("modified-" + domain.getEmail());
	}

	@Override
	protected boolean assertDomainEquals(EmailConfirmationRequest domain, EmailConfirmationRequest other) {
		try {
			Assert.assertEquals(domain.getId(), other.getId());
			Assert.assertEquals(domain.getEmail(), other.getEmail());
			return true;
		} catch (AssertionFailedError e) {
			return false;
		}
	}
	
	@Test
	public void testFindByEmail() {
		String email = "a@b.com";
		EmailConfirmationRequest request = createDomain();
		request.setEmail(email);
		repository.save(request);
		
		EmailConfirmationRequest noMatchRequest = createDomain();
		noMatchRequest.setEmail(email + ".ph");
		repository.save(noMatchRequest);
		
		EmailConfirmationRequest result = repository.findByEmail(email);
		assertNotNull(result);
		assertNotNull(result.getEmail().equals(email));
	}
	
}

package com.phroogal.core.repository;

import junit.framework.AssertionFailedError;

import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.phroogal.core.domain.security.RememberMeToken;

public class RememberMeTokenRepositoryTest extends BaseRepositoryTest<RememberMeToken, ObjectId, RememberMeTokenRepository> {

	@Autowired
	private RememberMeTokenRepository repository;
	
	@Before
	public void setUp() {
		super.setUp();
	}
	
	@Override
	protected RememberMeTokenRepository returnRepository() {
		return repository;
	}

	@Override
	protected Class<RememberMeToken> returnDomainClass() {
		return RememberMeToken.class;
	}

	@Override
	protected RememberMeToken createDomain() {
		TestEntityGenerator generator = new TestEntityGenerator();
		return generator.generateTestRememberMeToken();
	}

	@Override
	protected void modifyDomain(RememberMeToken token) {
		token.setTokenValue(token.getTokenValue() + "-modified");
	}

	@Override
	protected boolean assertDomainEquals(RememberMeToken domain, RememberMeToken other) {
		try {
			Assert.assertEquals(domain.getId(), other.getId());
			Assert.assertEquals(domain.getSeries(), other.getSeries());
			Assert.assertEquals(domain.getTokenValue(), other.getTokenValue());
			Assert.assertEquals(domain.getUsername(), other.getUsername());
			Assert.assertEquals(domain.getDate(), other.getDate());
			return true;
		} catch (AssertionFailedError e) {
			return false;
		}
	}
	
	@Test
	public void testFindBySeries() throws Exception {
		String series = "12345";
		RememberMeToken token = createDomain();
		ObjectId id = token.getId();
		token.setSeries(series);
		repository.save(token);
		
		RememberMeToken persistedToken = repository.findBySeries(series);
		Assert.assertEquals(id, persistedToken.getId());
	}

	@Test
	public void testFindByUsername() throws Exception {
		String username = "johnsmith";
		RememberMeToken token = createDomain();
		ObjectId id = token.getId();
		token.setUsername(username);
		repository.save(token);
		
		RememberMeToken persistedToken = repository.findByUsername(username).get(0);
		Assert.assertEquals(id, persistedToken.getId());
	}

}

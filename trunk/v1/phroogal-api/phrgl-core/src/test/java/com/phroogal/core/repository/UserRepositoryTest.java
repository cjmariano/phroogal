package com.phroogal.core.repository;

import java.util.List;

import junit.framework.AssertionFailedError;

import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.phroogal.core.domain.User;

public class UserRepositoryTest extends BaseRepositoryTest<User, ObjectId, UserRepository> {
	
	@Autowired
	private UserRepository repository;

	@Override
	protected UserRepository returnRepository() {
		return repository;
	}
	
	@Override
	protected Class<User> returnDomainClass() {
		return User.class;
	}

	@Override
	protected User createDomain() {
		TestEntityGenerator generator = new TestEntityGenerator();
		return generator.generateTestUser();
	}

	@Override
	protected void modifyDomain(User userProfile) {
		userProfile.getProfile().setPassword("password123");
	}

	@Override
	protected boolean assertDomainEquals(User domain, User other) {
		try {
			Assert.assertEquals(domain.getId(), other.getId());
			Assert.assertEquals(domain.getUsername(), other.getUsername());
			Assert.assertEquals(domain.getPassword(), other.getPassword());
			Assert.assertEquals(domain.getProfile().getFirstname(), other.getProfile().getFirstname());
			Assert.assertEquals(domain.getProfile().getLastname(), other.getProfile().getLastname());
			Assert.assertEquals(domain.getProfile().getEmail(), other.getProfile().getEmail());
			return true;
		} catch (AssertionFailedError e) {
			return false;
		}
	}
	
	@Test
	public void testFindByProfileEmailIgnoreCase() {
		String email = "admin123@server.com";
		User expectedDomain = createDomain();
		expectedDomain.getProfile().setEmail(email);
		repository.save(expectedDomain);
		
		User notMatchedDomain = createDomain();
		notMatchedDomain.getProfile().setEmail(email + ".ph");
		repository.save(notMatchedDomain);
		
		List<User> queryResult = repository.findByProfileEmail(email.toUpperCase());
		Assert.assertTrue(queryResult.size() != 0);
		assertDomainEquals(expectedDomain, queryResult.get(0));
	}
	
	@Test
	public void testFindByUsernameOnEmailSignup() {
		String username = "userName@find.com";
		User user = createDomain();
		user.setUsername(username);
		repository.save(user);
		
		User notMatchedDomain = createDomain();
		notMatchedDomain.setUsername(username + "123");
		repository.save(notMatchedDomain);
		
		User result = repository.findByUsername(username.toUpperCase());
		assertDomainEquals(user, result);
	}
}

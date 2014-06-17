package com.phroogal.core.repository;

import java.util.List;

import junit.framework.AssertionFailedError;

import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.phroogal.core.domain.CreditUnion;
import com.phroogal.core.utility.CollectionUtil;

public class CreditUnionRepositoryTest extends BaseRepositoryTest<CreditUnion, ObjectId, CreditUnionRepository> {

	@Autowired
	private CreditUnionRepository repository;
	
	@Override
	protected CreditUnionRepository returnRepository() {
		return repository;
	}

	@Override
	protected Class<CreditUnion> returnDomainClass() {
		return CreditUnion.class;
	}

	@Override
	protected CreditUnion createDomain() {
		TestEntityGenerator generator = new TestEntityGenerator();
		return generator.generateTestCreditUnion();
	}

	@Override
	protected void modifyDomain(CreditUnion domain) {
		domain.setName(domain.getName() + "-modified");
	}

	@Override
	protected boolean assertDomainEquals(CreditUnion domain, CreditUnion other) {
		try {
			Assert.assertEquals(domain.getId(), other.getId());
			Assert.assertEquals(domain.getName(), other.getName());
			return true;
		} catch (AssertionFailedError e) {
			return false;
		}
	}
	
	@Test
	public void testFindByNameContaining() throws Exception {
		CreditUnion expectedDomain = createDomain();
		expectedDomain.setName("TestCreditUnion");
		repository.save(expectedDomain);
		
		CreditUnion notMatchedDomain = createDomain();
		notMatchedDomain.setName("NoMatch");
		repository.save(notMatchedDomain);
		
		List<CreditUnion> queryResult = repository.findByNameLike("TEST");
		List<ObjectId> expectedDomainIds = CollectionUtil.arrayList();
		expectedDomainIds.add(expectedDomain.getId());
		assertListIDsAreContainedInPersistedItems(expectedDomainIds, queryResult);
	}

}

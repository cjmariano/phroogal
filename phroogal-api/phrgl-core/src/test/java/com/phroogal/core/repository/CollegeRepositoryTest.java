package com.phroogal.core.repository;

import java.util.List;

import junit.framework.AssertionFailedError;

import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.phroogal.core.domain.College;
import com.phroogal.core.utility.CollectionUtil;

public class CollegeRepositoryTest extends BaseRepositoryTest<College, ObjectId, CollegeRepository> {
	
	@Autowired
	private CollegeRepository repository;

	@Override
	protected CollegeRepository returnRepository() {
		return repository;
	}

	@Override
	protected Class<College> returnDomainClass() {
		return College.class;
	}

	@Override
	protected College createDomain() {
		TestEntityGenerator generator = new TestEntityGenerator();
		return generator.generateTestCollege();
	}

	@Override
	protected void modifyDomain(College domain) {
		domain.setName(domain.getName() + "-modified");
	}

	@Override
	protected boolean assertDomainEquals(College domain, College other) {
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
		College expectedDomain = createDomain();
		expectedDomain.setName("TestCollege");
		repository.save(expectedDomain);
		
		College notMatchedDomain = createDomain();
		notMatchedDomain.setName("NoMatch");
		repository.save(notMatchedDomain);
		
		List<College> queryResult = repository.findByNameLike("TEST");
		List<ObjectId> expectedDomainIds = CollectionUtil.arrayList();
		expectedDomainIds.add(expectedDomain.getId());
		assertListIDsAreContainedInPersistedItems(expectedDomainIds, queryResult);
	}
}

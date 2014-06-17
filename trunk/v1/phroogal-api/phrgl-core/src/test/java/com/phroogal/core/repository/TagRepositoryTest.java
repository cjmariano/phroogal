package com.phroogal.core.repository;

import java.util.Arrays;
import java.util.List;

import junit.framework.AssertionFailedError;

import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.phroogal.core.domain.Tag;
import com.phroogal.core.utility.CollectionUtil;

public class TagRepositoryTest extends BaseRepositoryTest<Tag, ObjectId, TagRepository> {

	@Autowired
	private TagRepository repository;
	
	@Override
	protected TagRepository returnRepository() {
		return repository;
	}

	@Override
	protected Class<Tag> returnDomainClass() {
		return Tag.class;
	}

	@Override
	protected Tag createDomain() {
		TestEntityGenerator generator = new TestEntityGenerator();
		return generator.generateTestTag();
	}

	@Override
	protected void modifyDomain(Tag domain) {
		domain.setName(domain.getName() + "-modified");
	}

	@Override
	protected boolean assertDomainEquals(Tag domain, Tag other) {
		try {
			Assert.assertEquals(domain.getId(), other.getId());
			Assert.assertEquals(domain.getName(), other.getName());
			return true;
		} catch (AssertionFailedError e) {
			return false;
		}
	}
	
	@Test
	public void testFindByNameStartingWith() {
		Tag expectedDomain = createDomain();
		expectedDomain.setName("TestTag");
		repository.save(expectedDomain);
		
		Tag notMatchedDomain = createDomain();
		notMatchedDomain.setName("NoMatch");
		repository.save(notMatchedDomain);
		
		List<Tag> queryResult = repository.findByNameStartingWith("tEST");
		List<ObjectId> expectedDomainIds = CollectionUtil.arrayList();
		expectedDomainIds.add(expectedDomain.getId());
		
		Assert.assertTrue(queryResult.size() == 1);
		assertListIDsAreContainedInPersistedItems(expectedDomainIds, queryResult);
	}
	
	@Test
	public void testFindByNames() {
		Tag expectedDomain = createDomain();
		expectedDomain.setName("Accounting");
		repository.save(expectedDomain);
		
		Tag expectedDomain2 = createDomain();
		expectedDomain2.setName("Investment");
		repository.save(expectedDomain2);
		
		Tag notMatchedDomain = createDomain();
		notMatchedDomain.setName("NoMatch");
		repository.save(notMatchedDomain);
		
		List<Tag> queryResult = repository.findByNames(Arrays.asList("Accounting", "Investment"));
		List<ObjectId> expectedDomainIds = CollectionUtil.arrayList();
		expectedDomainIds.add(expectedDomain.getId());
		expectedDomainIds.add(expectedDomain2.getId());
		assertListIDsAreContainedInPersistedItems(expectedDomainIds, queryResult);
	}

}

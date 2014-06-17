/**
 * 
 */
package com.phroogal.core.repository;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.phroogal.core.domain.Persistent;
import com.phroogal.core.search.index.Index;
import com.phroogal.core.test.helper.RepositoryCleanupHelper;
import com.phroogal.core.utility.CollectionUtil;
import com.phroogal.core.utility.SolrUtility;

/**
 * Base class for respository tests that covers basic CRUD operations 
 * @author Christopher Mariano
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/spring/app-context.xml"})
public abstract class BaseRepositoryTest<T extends Persistent<ID>, ID extends Serializable, REPO extends CrudRepository<T,ID>> {
	
	protected abstract REPO returnRepository();
	
	protected abstract Class<T> returnDomainClass();
	
	protected abstract T createDomain();
	
	protected abstract void modifyDomain(T domain);
	
	protected abstract boolean assertDomainEquals(T domain, T other);
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	@Qualifier("solrUtility")
	private SolrUtility solrUtility;
	
	private REPO repository;
	
	private Class<T> domainClazz = returnDomainClass();
	
	@Before
	public void setUp() {
		repository = returnRepository();
		createCollection();
		deleteAllSolrData();
	}
	
	@After
	public void tearDown() {
		dropCollection();
		deleteAllSolrData();
	}
	
	@Test
	public void testFind() {
		T domain = createDomain();
		repository.save(domain);
		
		T persistedDomain = repository.findOne((ID) domain.getId());
		assertNotNull(persistedDomain);
		assertDomainEquals(domain, persistedDomain);
	}
	
	@Test
	public void testFindAll() {
		List<ID> persistedId = CollectionUtil.arrayList();
		
		T domain1 = createDomain();
		repository.save(domain1);
		persistedId.add((ID) domain1.getId());
		
		T domain2 = createDomain();
		repository.save(domain2);
		persistedId.add((ID) domain2.getId());
		
		T domain3 = createDomain();
		repository.save(domain3);
		persistedId.add((ID) domain3.getId());
		
		Iterable<T> persistedDomains = repository.findAll((Iterable<ID>) persistedId);
		assertListIDsAreContainedInPersistedItems(persistedId, persistedDomains);
	}

	protected void assertListIDsAreContainedInPersistedItems(List<ID> persistedId, Iterable<T> persistedDomains) {
		for (T persistedDomain : persistedDomains) {
			for (Iterator<ID> iterator = persistedId.iterator(); iterator.hasNext();) {
				ID sampleDomainID = (ID) iterator.next();
				if (persistedDomain.getId().equals(sampleDomainID.toString())
						|| persistedDomain.getId().equals(sampleDomainID)) {
					break;
				}
				if (!iterator.hasNext()) {
					fail("No matching items in persisted list");
				}
			}
		}
	}

	@Test
	public void testCount() {
		T domainAAA = createDomainUniqueId();
		repository.save(domainAAA);
		
		T domainBBB = createDomainUniqueId();
		repository.save(domainBBB);
		
		T domainCCC = createDomainUniqueId();
		repository.save(domainCCC);
		
		Assert.assertTrue(repository.count() == 3);
	}
	
	@Test
	public void testInsert() {
		T domain = createDomain();
		repository.save(domain);
		
		T persistedDomain = repository.findOne((ID) domain.getId());
		assertNotNull(persistedDomain);
		assertDomainEquals(domain, persistedDomain);
	}
	
	@Test
	public void testInsertAll() {
		List<T> domainBatch = CollectionUtil.arrayList();
		T domain1 = createDomain();
		setDomainId(domain1);
		domainBatch.add(domain1);
		
		T domain2 = createDomain();
		setDomainId(domain2);
		domainBatch.add(domain2);
		
		T domain3 = createDomain();
		setDomainId(domain3);
		domainBatch.add(domain3);
		
		Iterable<T> persistedList = repository.save(domainBatch);
		Assert.assertEquals(((List<T>)persistedList).size(), 3);
		
		assertListsHasSimilarElements(domainBatch, persistedList);
	}

	@Test
	public void testModify() {
		T domain = createDomain();
		repository.save(domain);
		
		T persistedDomain = repository.findOne((ID) domain.getId());
		modifyDomain(persistedDomain);
		repository.save(persistedDomain);
		
		T modifiedDomain = repository.findOne((ID) domain.getId());
		assertDomainEquals(persistedDomain, modifiedDomain);
	}
	
	@Test
	public void testDelete() {
		T domain = createDomain();
		repository.save(domain);
		
		T persistedDomain = repository.findOne((ID) domain.getId());
		assertNotNull(persistedDomain);
		
		repository.delete((ID) persistedDomain.getId());
		
		T deletedDomain = repository.findOne((ID) persistedDomain.getId());
		assertNull(deletedDomain);
	}
	
	@Test
	public void testDeleteAll() {
		List<ID> domainIds = CollectionUtil.arrayList();
		List<T> domainBatch = CollectionUtil.arrayList();
		
		T domain1 = createDomain();
		ID id = setDomainId(domain1);
		domainIds.add(id);
		domainBatch.add(domain1);
		
		T domain2 = createDomain();
		id = setDomainId(domain2);
		domainIds.add(id);
		domainBatch.add(domain2);
		
		T domain3 = createDomain();
		id = setDomainId(domain3);
		domainIds.add(id);
		domainBatch.add(domain3);
		
		repository.delete(domainBatch);
		Iterable<T> deletedIds = repository.findAll(domainIds);
		assertListSizeCount(deletedIds, 0);
	}
	
	@SuppressWarnings("unchecked")
	private ID setDomainId(T domain) {
		if (Index.class.isInstance(domain)) {
			domain.setId((ID) ObjectId.get().toString());
		} else {
			domain.setId((ID) ObjectId.get());	
		}
		return domain.getId();
	}
	
	private void assertListsHasSimilarElements(List<T> domainBatch, Iterable<T> persistedList) {
		for (T domain : domainBatch) {
			for (T persistedDomain : persistedList) {
				if (domain.getId().equals(persistedDomain.getId())) {
					assertDomainEquals(domain, persistedDomain);
				}
			}
		}
	}
	
	private void assertListSizeCount(Iterable<T> deletedIds, int size) {
		if (deletedIds instanceof PageImpl) {
			Assert.assertEquals(((PageImpl<T>)deletedIds).getTotalElements(), size);	
		} else {
			int itCount = 0;
			for (@SuppressWarnings("unused") T t : deletedIds) { itCount ++; }
			Assert.assertEquals(itCount, size);
		}
	}
	
	/**
     * Create a collection if the collection does not already exists
     */
    private void createCollection() {
        if (!mongoTemplate.collectionExists(domainClazz)) {
            mongoTemplate.createCollection(domainClazz);
        }
    }
    
    @SuppressWarnings("unchecked")
	private T createDomainUniqueId() {
		T domain = createDomain();
		ID id = domain.getId();
		if (id instanceof String) {
			domain.setId((ID) ObjectId.get().toString());
		} else {
			domain.setId((ID) ObjectId.get());
		}
		return domain;
	}

    /**
     * Drops the collection if the collection does already exists
     */
    private void dropCollection() {
    	RepositoryCleanupHelper.dropCollection(mongoTemplate);
    }
    
    protected void deleteAllSolrData() {
        try {
          if (domainClazz.newInstance() instanceof Index) {
        	  RepositoryCleanupHelper.deleteAllSolrData(solrUtility);
          }
        } catch (Exception e) {
          e.printStackTrace();
        }	
    }
}

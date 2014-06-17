package com.phroogal.core.service.impl;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import java.io.Serializable;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.util.ReflectionTestUtils;

import com.phroogal.core.domain.Persistent;
import com.phroogal.core.domain.SearchableIndex;
import com.phroogal.core.search.index.Index;
import com.phroogal.core.search.index.SearchIndexRegistry;
import com.phroogal.core.service.Service;

/**
 * Base class for service tests that covers basic CRUD operations of Repository classes
 * @author Christopher Mariano
 *
 */
public abstract class BaseServiceTest<T extends Persistent<ID>, ID extends Serializable, REPO extends CrudRepository<T,ID>> {
	
	protected abstract Service<T, ID> returnServiceImpl();
	
	protected abstract REPO returnMongoRepository();
	
	protected abstract T returnDomainInstance();
	
	private Service<T, ID> serviceImpl;
	
	@SuppressWarnings("rawtypes")
	private SearchIndexRegistry indexRegistry = Mockito.mock(SearchIndexRegistry.class);
	
	private REPO repository;
	
	private T domain;
	
	private ID id;

	@Before
	public void setUp() throws Exception {
		serviceImpl = returnServiceImpl();
		repository = returnMongoRepository();
		domain = returnDomainInstance();
		ReflectionTestUtils.setField(serviceImpl, "repository", repository);
		ReflectionTestUtils.setField(serviceImpl, "indexRegistry", indexRegistry);
	}

	@Test
	public void testSaveOrUpdate() {
		serviceImpl.saveOrUpdate(domain);
		verify(repository, atLeastOnce()).save(domain);
		verifyAddToIndex(domain);
	}

	@Test
	public void testDelete() {
		serviceImpl.delete(domain);
		verify(repository, atLeastOnce()).delete(domain);
		verifyDeleteFromIndex(domain);
	}

	@Test
	public void testFindById() {
		serviceImpl.findById(id);
		verify(repository, atLeastOnce()).findOne(id);
	}

	@Test
	public void testFindAll() {
		serviceImpl.findAll();
		verify(repository, atLeastOnce()).findAll();
	}
	
	@Test
	public void testCount() {
		serviceImpl.count();
		verify(repository, atLeastOnce()).count();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void verifyAddToIndex(T argObject) {
		if (argObject instanceof SearchableIndex) {
			Index index = ((SearchableIndex) domain).getIndex();
			verify(indexRegistry, atLeastOnce()).addIndex(Mockito.any(index.getClass()));
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void verifyDeleteFromIndex(T argObject) {
		if (argObject instanceof SearchableIndex) {
			Index index = ((SearchableIndex) domain).getIndex();
			verify(indexRegistry, atLeastOnce()).deleteIndex(index.getId(), index.getClass());
		}
	}

	protected REPO getRepository() {
		return repository;
	}
}

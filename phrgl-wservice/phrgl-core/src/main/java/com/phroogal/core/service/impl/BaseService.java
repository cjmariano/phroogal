/**
 * 
 */
package com.phroogal.core.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.phroogal.core.domain.Persistent;
import com.phroogal.core.domain.SearchableIndex;
import com.phroogal.core.search.index.Index;
import com.phroogal.core.search.index.SearchIndexRegistry;
import com.phroogal.core.service.Service;

/**
 * Base service class that provides basic data store functionality to inheriting child classes
 * @author Christopher Mariano
 *
 */
@SuppressWarnings({"unchecked","rawtypes"})
public abstract class BaseService<T extends Persistent<ID>, ID extends Serializable, REPO extends MongoRepository<T, ID>> implements Service<T, ID> {

	private static final int DEFAULT_PAGE_REQUEST_SIZE = 100;
	
	@Autowired
	private SearchIndexRegistry indexRegistry;
	
	protected abstract REPO getRepository();
	
	private REPO repository;
	
	/**
	 * Provides a hook for child classes to place logic before the object gets saved or updated
	 * @param argObject - domain to be persisted
	 * @return true to continue save or update operation, false otherwise
	 */
	protected boolean onBeforeSaveOrUpdate(T argObject) {
		//To be overridden by child classes
		return true;
	}
	
	/**
	 * Provides a hook for child classes to place logic after the object gets saved or updated
	 * @param argObject - domain to be persisted
	 */
	protected void onAfterSaveOrUpdate(T argObject) {
		//To be overridden by child classes
	}
	
	/**
	 * Provides a hook for child classes to place logic after the object gets retrieved from id reference
	 * @param argObject - domain to be persisted
	 */
	protected void onAfterFindById(T argObject) {
		//To be overridden by child classes
	}
	
	/**
	 * Provides a hook for child classes to place logic before the object gets deleted
	 * @param argObject - domain to be deleted
	 */
	protected void onBeforeDelete(T argObject) {
		//To be overridden by child classes
	}
	
	/**
	 * Provides a hook for child classes to place logic after the object gets deleted
	 * @param argObject - domain that was deleted
	 */
	protected void onAfterDelete(T argObject) {
		//To be overridden by child classes
	}
	
	@Override
	public T saveOrUpdate(T argObject) {
		initializeRepository();
		if (onBeforeSaveOrUpdate(argObject)) {
			repository.save(argObject);
			addToIndex(argObject);	
		}
		onAfterSaveOrUpdate(argObject);
		return argObject;
	}
	
	@Override
	public List<T> saveOrUpdate(List<T> argObjects) {
		initializeRepository();
		repository.save(argObjects);
		return argObjects;
	}
	
	@Override
	public void delete(T argObject) {
		initializeRepository();
		onBeforeDelete(argObject);
		repository.delete(argObject);
		onAfterDelete(argObject);
		deleteFromIndex(argObject);
	}
	
	@Override
	public void delete(List<T> argObjects) {
		initializeRepository();
		repository.delete(argObjects);
	}

	@Override
	public T findById(ID argId) {
		initializeRepository();
		T domain = repository.findOne(argId);
		if (domain != null) {
			onAfterFindById(domain);	
		}
		return domain;
	}

	@Override
	public List<T> findAll() {
		initializeRepository();
		return (List<T>) repository.findAll();
	}
	
	@Override
	public Page<T> findAll(int pageAt, int pageSize) {
		initializeRepository();
		Pageable pageRequest = generatePageRequest(pageAt, pageSize);
		return repository.findAll(pageRequest);
	}
	
	@Override
	public Long count() {
		initializeRepository();
		return repository.count();
	}

	/**
	 * Convenience method for generating Page requests
	 * @param pageAt - current page number
	 * @param pageSize - size of elements per page
	 * @return instance of {@link PageRequest}
	 */
	protected PageRequest generatePageRequest(int pageAt, int pageSize) {
		return new PageRequest(pageAt, pageSize > 0 ? pageSize : DEFAULT_PAGE_REQUEST_SIZE );
	}
	
	/**
	 * Convenience method for generating Page requests
	 * @param pageAt - current page number
	 * @param pageSize - size of elements per page
	 * @param sort - sort options
	 * @return instance of {@link PageRequest}
	 */
	protected PageRequest generatePageRequest(int pageAt, int pageSize, Sort sort) {
		return new PageRequest(pageAt, pageSize > 0 ? pageSize : DEFAULT_PAGE_REQUEST_SIZE, sort);
	}
	
	private void addToIndex(T argObject) {
		Index index = getIndexFromDomain(argObject);
		if (index != null) {
			indexRegistry.addIndex(getIndexFromDomain(argObject));	
		}
	}
	
	private void deleteFromIndex(T argObject) {
		Index index = getIndexFromDomain(argObject);
		if (index != null) {
			indexRegistry.deleteIndex(index.getId(), index.getClass());	
		}
	}
	
	private Index getIndexFromDomain(T argObject) {
		Index index = null;
		if (argObject instanceof SearchableIndex) {
			index = ( (SearchableIndex) argObject).getIndex();
		}
		return index;
	}

	private void initializeRepository() {
		if (repository == null) {
			repository = getRepository();
		}
	}
}

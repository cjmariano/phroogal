package com.phroogal.core.service;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;

import com.phroogal.core.domain.Persistent;

/**
 * Base service contracts that exposes data store functionality to child services 
 * @author Christopher Mariano
 */
public interface Service<T extends Persistent<ID>, ID extends Serializable> {

	/**
	 * Saves an object to the data store if not persisted yet. 
	 * Otherwise, update existing data
	 */
	T saveOrUpdate(T argObject);
	
	/**
	 * Saves a list of object to the data store if not persisted yet. 
	 * Otherwise, update existing data
	 */
	List<T> saveOrUpdate(List<T> argObjects);
	
	/**
	 * Removes an object from the data store.
	 */
	void delete(T argObject);
	
	/**
	 * Removes a list of object to the data store . 
	 */
	void delete(List<T> argObjects);

	/**
	 * Retrieves a domain object with the given Id.
	 */
	T findById(ID argId);

	/**
	 * Returns a list of all the domain objects
	 */
	List<T> findAll();
	
	/**
	 * Returns a paginated list of all the domain objects
	 * @param pageAt - states to return results starting from this page (number).
	 * @param pageSize - number of results from this page.
	 * @return a paginated list of results
	 */
	Page<T> findAll(int pageAt, int pageSize);
	
	/**
	 * Returns the number of entities in the given collection
	 */
	Long count();
}

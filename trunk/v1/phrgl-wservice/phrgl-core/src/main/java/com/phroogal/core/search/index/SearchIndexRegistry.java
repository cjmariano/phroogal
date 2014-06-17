/**
 * 
 */
package com.phroogal.core.search.index;

import java.util.Map;

import org.springframework.data.solr.repository.SolrCrudRepository;


/**
 * Service that manages search and index functionalities. Ensure that Index to repository 
 * is properly mapped.
 * @author Christopher Mariano
 *
 */
public class SearchIndexRegistry<T> {
	
	private Map<Class<? extends Index>, ? extends SolrCrudRepository<T, String>> indexRepositoryMap;
	
	/**
	 * Adds the index to the search repository
	 * @param index to be added
	 */
	public void addIndex(T index) {
		SolrCrudRepository<T, String> repository = indexRepositoryMap.get(index.getClass());
		
		if (repository != null) {
			repository.save(index);	
		}
	}
	
	/**
	 * Deletes the index from the search repository
	 * @param index to be deleted
	 */
	public void deleteIndex(String indexId, Class<T> indexClass) {
		SolrCrudRepository<T, String> repository = indexRepositoryMap.get(indexClass);
		
		if (repository != null) {
			repository.delete(indexId);	
		}
	}

	public Map<Class<? extends Index>, ? extends SolrCrudRepository<T, String>> getIndexRepositoryMap() {
		return indexRepositoryMap;
	}

	public void setIndexRepositoryMap(Map<Class<? extends Index>, ? extends SolrCrudRepository<T, String>> indexRepositoryMap) {
		this.indexRepositoryMap = indexRepositoryMap;
	} 
}

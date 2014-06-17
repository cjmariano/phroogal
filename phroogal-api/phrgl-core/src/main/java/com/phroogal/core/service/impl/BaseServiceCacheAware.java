/**
 * 
 */
package com.phroogal.core.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.phroogal.core.domain.Persistent;

/**
 * Base service class that provides caching implementation on top of the basic functions of {@link BaseService}.
 * @author Christopher Mariano
 *
 */
public abstract class BaseServiceCacheAware<T extends Persistent<ID>, ID extends Serializable, REPO extends MongoRepository<T,ID>> extends BaseService<T, ID, REPO> {

	/**
	 * Should return the cache name where caches would be stored. This should match the entries on
	 * <code>ehcache.xml</code>
	 * @return
	 */
	protected abstract String returnCacheName();
	
	@Autowired
	private CacheManager cacheManager;
	
	@Override
	public T findById(ID argId) {
		return findByIdFromDbOrCache(argId);
	}

	@Override
	public T saveOrUpdate(T argObject) {
		return saveOrUpdateAndCachePut(argObject);
	}
	
	@Override
	public List<T> saveOrUpdate(List<T> argObjects) {
		return saveOrUpdateAndCachePut(argObjects);
	}

	@Override
	public void delete(T argObject) {
		deleteAndCacheRemove(argObject);
	}
	
	@Override
	public void delete(List<T> argObjects) {
		deleteAndCacheRemove(argObjects);
	}
	
	@SuppressWarnings("unchecked")
	private T findByIdFromDbOrCache(ID argId) {
		T domain = null;
		Cache cache = cacheManager.getCache(returnCacheName());
		ValueWrapper value = cache.get(argId.toString());
		if (value == null) {
			domain = super.findById(argId);
			return saveOrUpdateAndCachePut(domain);
		}
		return (T) value.get();
	}
	
	private T saveOrUpdateAndCachePut(T argObject) {
		Cache cache = cacheManager.getCache(returnCacheName());
		cache.put(argObject.getId().toString(), argObject);
		return super.saveOrUpdate(argObject);
	}
	
	private List<T> saveOrUpdateAndCachePut(List<T> argObjects) {
		for (T argObject : argObjects) {
			saveOrUpdateAndCachePut(argObject);
		}
		return super.saveOrUpdate(argObjects);
	}
	
	private void deleteAndCacheRemove(T argObject) {
		Cache cache = cacheManager.getCache(returnCacheName());
		cache.evict(argObject.getId().toString());
		super.delete(argObject);
	}
	
	private void deleteAndCacheRemove(List<T> argObjects) {
		for (T argObject : argObjects) {
			deleteAndCacheRemove(argObject);
		}
		super.delete(argObjects);
	}
}

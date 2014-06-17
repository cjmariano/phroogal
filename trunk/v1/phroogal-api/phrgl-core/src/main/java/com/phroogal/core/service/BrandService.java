/**
 * 
 */
package com.phroogal.core.service;

import java.util.List;

import org.bson.types.ObjectId;

import com.phroogal.core.domain.Brand;

/**
 * Service for Brand functions
 
 *
 */
public interface BrandService extends Service<Brand, ObjectId> {
	
	public Brand getBrandByName(String name);
	
	public Brand getBrandByUrl(String url);
	
	
	/**
	 * Returns  Brands containing  name with search keyword
	 * @param keyword - used to match a brand's name
	 * @param pageAt - states to return results starting from this page (number).
	 * @param pageSize - number of results from this page.
	 * @return an list of {@link Brand} matching the keyword.
	 */
	public List<Brand> searchBrandByName(String keyword,int pageAt, int pageSize);
	
	/**
	 * Returns  Brands containing  name with search keyword
	 * @param keyword - used to match a brand's name
	 * @param pageAt - states to return results starting from this page (number).
	 * @param pageSize - number of results from this page.
	 * @return an list of {@link Brand} matching the keyword.
	 */
	public List<Brand> searchBrandByNameKeyword(String keyword, int pageAt, int pageSize);
	
	public List<Brand> searchAutoSuggestBrandByName(String keyword, int pageAt, int pageSize);
	
	
}

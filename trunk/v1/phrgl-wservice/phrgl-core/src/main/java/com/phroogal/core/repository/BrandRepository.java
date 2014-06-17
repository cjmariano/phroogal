/**
 * 
 */
package com.phroogal.core.repository;

import java.util.List;

import org.bson.types.ObjectId;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.domain.Pageable;
import com.phroogal.core.domain.Brand;

import org.springframework.stereotype.Repository;


/**
 * Repository for {@link Brand} data
 * 
 * 
 */
@Repository
public interface BrandRepository extends MongoRepository<Brand, ObjectId>{
	
	/**
	 * Retrieves a Brand given the name
	 * @param name associated with a brand
	 * @return the brand that matches the name
	 */
	
	@Query("{name:{$regex : ?0, $options : 'i'}}")
	public Brand findByName(String name);
	
	/**
	 * Retrieves a Brand given the url
	 * @param url associated with a brand
	 * @return the brand that matches the url
	 */
	
	@Query("{url:{$regex : ?0, $options : 'i'}}")
	public Brand findByUrl(String url);	
	
	/**
	 * Retrieves a Brands given the keyword search
	 * @param regex for keyword associated with a brand name
	 * @return the brands that matches the regex
	 */
	@Query("{name:{$regex : ?0, $options : 'i'}}")
    public List<Brand> searchBrands(String regex,Pageable pageRequest);
		
}

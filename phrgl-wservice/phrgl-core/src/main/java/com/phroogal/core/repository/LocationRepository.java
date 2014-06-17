/**
 * 
 */
package com.phroogal.core.repository;

import com.phroogal.core.domain.Location;
import com.phroogal.core.domain.Question;

/**
 * Repository for {@link Question} data
 * 
 * @author Christopher Mariano
 * 
 */
public interface LocationRepository extends BaseMongoRepository<Location> {
	
	/**
	 * Returns a location based on the location reference
	 * @param locationRef - reference for a location 
	 * @return an instance of {@link Location}
	 */
	public Location findByLocationRef(String locationRef);
}

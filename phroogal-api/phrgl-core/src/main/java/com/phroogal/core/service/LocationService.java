/**
 * 
 */
package com.phroogal.core.service;

import java.util.List;

import org.bson.types.ObjectId;

import com.phroogal.core.domain.Location;
import com.phroogal.core.domain.LocationIndex;

/**
 * Service for {@link Location} functions
 * @author Christopher Mariano
 *
 */
public interface LocationService extends Service<Location, ObjectId> {

	/**
	 * Gets a list of instances of {@link LocationIndex}, that matches the keyword specified.
	 * @param types of location to be returned (i.e. (cities) would return lists of cities)
	 * refer to list of google supported types <a href=https://developers.google.com/places/documentation/supported_types?hl=fr>here</a>
	 * @param keyword used to filter locations returned 
	 * @return list of {@link LocationIndex}
	 */
	public List<LocationIndex> queryCitiesByKeyword(String keyword);
	
	/**
	 * Returns the {@link Location} object given the location reference
	 * @param locationRef location reference to be used as filter
	 * @return the {@link Location}
	 */
	public Location getLocationByReference(String locationRef);
}

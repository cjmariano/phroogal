package com.phroogal.core.service.impl;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.phroogal.core.domain.Location;
import com.phroogal.core.domain.LocationIndex;
import com.phroogal.core.repository.LocationRepository;
import com.phroogal.core.service.LocationService;
import com.phroogal.core.utility.GooglePlacesClient;

/**
 * Default implementation of the {@link Location} interface
 * @author Christopher Mariano
 *
 */
@Service
public class LocationServiceImpl extends BaseService<Location, ObjectId, LocationRepository> implements LocationService{
	
	private static final String QUERY_TYPE_CITIES = "(cities)";
	
	@Autowired
	LocationRepository locationRepository;
	
	@Autowired
	@Qualifier("googlePlacesClient")
	private GooglePlacesClient googlePlacesClient;
	
	@Override
	protected LocationRepository getRepository() {
		return locationRepository;
	}

	@Override
	public List<LocationIndex> queryCitiesByKeyword(String keyword) {
		return googlePlacesClient.queryLocationsByKeyword(QUERY_TYPE_CITIES, keyword);
	}

	@Override
	public Location getLocationByReference(String locationRef) {
		Location location = locationRepository.findByLocationRef(locationRef);
		if (location == null && locationRef != null) {
			location = googlePlacesClient.getLocationByRef(locationRef);
			locationRepository.save(location);
		}
		return location;
	}
}

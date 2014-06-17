package com.phroogal.core.service.impl;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.phroogal.core.domain.Location;
import com.phroogal.core.repository.LocationRepository;
import com.phroogal.core.repository.TestEntityGenerator;
import com.phroogal.core.service.LocationService;
import com.phroogal.core.service.Service;
import com.phroogal.core.utility.GooglePlacesClient;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/spring/app-context.xml" })
public class LocationServiceImplTest extends BaseServiceTest<Location, ObjectId, LocationRepository> {

	@Autowired
	private LocationService serviceImpl;
	
	private LocationRepository locationRepository = Mockito.mock(LocationRepository.class);
	
	private GooglePlacesClient googlePlacesClient = Mockito.mock(GooglePlacesClient.class);
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		ReflectionTestUtils.setField(serviceImpl, "locationRepository", locationRepository);
		ReflectionTestUtils.setField(serviceImpl, "googlePlacesClient", googlePlacesClient);
	}

	@Override
	protected Service<Location, ObjectId> returnServiceImpl() {
		return serviceImpl;
	}

	@Override
	protected LocationRepository returnMongoRepository() {
		return Mockito.mock(LocationRepository.class);
	}

	@Override
	protected Location returnDomainInstance() {
		TestEntityGenerator generator = new TestEntityGenerator();
		return generator.generateTestLocation();
	}
	
	@Test
	public void testQueryCitiesByKeyword() {
		serviceImpl.queryCitiesByKeyword("keyword");
		verify(googlePlacesClient, atLeastOnce()).queryLocationsByKeyword("(cities)", "keyword");
	}
	
	@Test
	public void testGetLocationByReferenceFromDB() {
		String locationRef = "abcde12345";
		Location location = Mockito.mock(Location.class);
		when(locationRepository.findByLocationRef(locationRef)).thenReturn(location);
		Location result = serviceImpl.getLocationByReference(locationRef);
		Assert.assertNotNull(result);
	}
	
	@Test
	public void testGetLocationByReferenceFromWS() {
		String locationRef = "abcde12345";
		Location location = Mockito.mock(Location.class);
		when(locationRepository.findByLocationRef(locationRef)).thenReturn(null);
		when(googlePlacesClient.getLocationByRef(locationRef)).thenReturn(location);
		Location result = serviceImpl.getLocationByReference(locationRef);
		Assert.assertNotNull(result);
	}
}

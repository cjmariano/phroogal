package com.phroogal.core.repository;

import junit.framework.AssertionFailedError;

import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.phroogal.core.domain.Location;

public class LocationRepositoryTest  extends BaseRepositoryTest<Location, ObjectId, LocationRepository> {

	@Autowired
	private LocationRepository repository;
	
	@Override
	protected LocationRepository returnRepository() {
		return repository;
	}

	@Override
	protected Class<Location> returnDomainClass() {
		return Location.class;
	}

	@Override
	protected Location createDomain() {
		TestEntityGenerator generator = new TestEntityGenerator();
		return generator.generateTestLocation();
	}

	@Override
	protected void modifyDomain(Location domain) {
		domain.setCity("Other City");
	}

	@Override
	protected boolean assertDomainEquals(Location domain, Location other) {
		try {
			Assert.assertEquals(domain.getId(), other.getId());
			Assert.assertEquals(domain.getLocationRef(), other.getLocationRef());
			Assert.assertEquals(domain.getDisplayName(), other.getDisplayName());
			Assert.assertEquals(domain.getCity(), other.getCity());
			Assert.assertEquals(domain.getState(), other.getState());
			Assert.assertEquals(domain.getCountry(), other.getCountry());
			Assert.assertEquals(domain.getLongtitude(), other.getLongtitude());
			Assert.assertEquals(domain.getLatitude(), other.getLatitude());
			return true;
		} catch (AssertionFailedError e) {
			return false;
		}
	}
	
	@Test
	public void testFindByLocationRef() {
		Location expectedDomain = createDomain();
		String locationRef = expectedDomain.getLocationRef();
		repository.save(expectedDomain);
		
		Location notMatchedDomain = createDomain();
		notMatchedDomain.setLocationRef(locationRef + "-modified");
		repository.save(notMatchedDomain);
		
		Location queryResult = repository.findByLocationRef(locationRef);
		assertDomainEquals(expectedDomain, queryResult);
	}
}

package com.phroogal.core.utility;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.phroogal.core.domain.Location;
import com.phroogal.core.domain.LocationIndex;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/spring/app-context.xml"})
public class GooglePlacesClientTest {
	
	@Autowired
	@Qualifier("googlePlacesClient")
	private GooglePlacesClient googlePlacesClient;

	@Test
	@Ignore("Skip for now to avoid quota charges") //TODO : cjm- create mock for this
	public void testQueryLocationsByKeyword() {
		List<LocationIndex> locations = googlePlacesClient.queryLocationsByKeyword("(cities)", "Elizabeth");
		Assert.assertFalse(locations.isEmpty());
	}
	
	@Test
	@Ignore("Skip for now to avoid quota charges") //TODO : cjm- create mock for this
	public void tesLocationByRef() {
		Location location = googlePlacesClient.getLocationByRef("CkQ0AAAA3dhObIeWI5VPtDZaDr8UVwfpjf79h9V9GlHE3nD7tnlFTeyIoCvlCKKdHXjY9epAFWxu1MQrthwDUv7sl7XqaxIQPbU8-_UB1EFNVdGLsV9aNxoUmHFfdbP3RhTXLK0GfBoD0S8FGEI");
		assertNotNull(location);
	}
}

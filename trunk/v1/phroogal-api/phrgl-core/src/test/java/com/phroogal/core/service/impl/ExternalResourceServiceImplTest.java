package com.phroogal.core.service.impl;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.phroogal.core.domain.ExternalResource;
import com.phroogal.core.exception.ExternalResourcesNotFoundException;
import com.phroogal.core.service.ExternalResourceService;
import com.phroogal.core.service.mock.MockExternalResourceServiceImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(profiles="dev")
@ContextConfiguration(locations={"classpath:/spring/app-context.xml"})
public class ExternalResourceServiceImplTest {

	@Autowired
	private ExternalResourceService extResourceServiceImpl;
	
	@Test
	public void testAllGetResourceByKeyword() {
		List<ExternalResource>  results = extResourceServiceImpl.getResourceByKeyword("Credit", 0L);
		assertExternalResourcesProperlyDefined(results);
	}

	@Test
	public void testGetResourceByKeyword() {
		List<ExternalResource>  results = extResourceServiceImpl.getResourceByKeyword("government_resources","Credit", 0L);
		assertExternalResourcesProperlyDefined(results);
	}
	
	@Test(expected=ExternalResourcesNotFoundException.class)
	public void testGetResourceByKeywordNoResults() {
		extResourceServiceImpl.getResourceByKeyword("government_resources", MockExternalResourceServiceImpl.NO_RESULTS_KEYWORD, 0L);
	}
	
	private void assertExternalResourcesProperlyDefined(List<ExternalResource> results) {
		Assert.assertNotNull(results);
		Assert.assertTrue(!results.isEmpty());
	}
}

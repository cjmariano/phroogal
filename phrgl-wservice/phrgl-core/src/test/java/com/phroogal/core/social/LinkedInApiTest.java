package com.phroogal.core.social;

import static com.phroogal.core.social.SocialNetworkType.LINKEDIN;

import org.junit.Ignore;
import org.junit.Test;

public class LinkedInApiTest extends BaseSocialNetworkApiTest {

	@Override
	protected String getProviderId() {
		return LINKEDIN.getId();
	}
	
	@Test
	@Ignore //TODO: Continue this
	public void testProfilePictureUrl() {
	}
}

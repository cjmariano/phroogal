package com.phroogal.core.social;

import org.junit.Ignore;
import org.junit.Test;




public class FacebookApiTest extends BaseSocialNetworkApiTest {

	@Override
	protected String getProviderId() {
		return SocialNetworkType.FACEBOOK.getId();
	}
	
	@Test
	@Ignore //TODO: Continue this
	public void testProfilePictureUrl() {
	}
}

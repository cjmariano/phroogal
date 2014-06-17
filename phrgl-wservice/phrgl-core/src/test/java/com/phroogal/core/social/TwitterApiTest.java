package com.phroogal.core.social;

import org.junit.Ignore;
import org.junit.Test;




public class TwitterApiTest extends BaseSocialNetworkApiTest {

	@Override
	protected String getProviderId() {
		return SocialNetworkType.TWITTER.getId();
	}
	
	@Test
	@Ignore //TODO: Continue this
	public void testProfilePictureUrl() {
	}
}

package com.phroogal.core.social;

import static com.phroogal.core.social.SocialNetworkType.GOOGLE;



public class GoogleApiTest extends BaseSocialNetworkApiTest {

	@Override
	protected String getProviderId() {
		return GOOGLE.getId();
	}
}

package com.phroogal.core.domain;



import static com.phroogal.core.social.TestSocialNetworkApiGenerator.TEST_PROFILE_URL;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;

import com.phroogal.core.social.SocialNetworkType;
import com.phroogal.core.social.TestSocialNetworkApiGenerator;

public class SocialProfileTest {

	@Test
	public void testCreateSocialProfile() throws Exception {
		TestSocialNetworkApiGenerator generator = new TestSocialNetworkApiGenerator(SocialNetworkType.FACEBOOK.getId()); 
		SocialProfile socialProfile = SocialProfile.createSocialProfile(generator.getTestSocialNetworkApi());
		Assert.assertNotNull(socialProfile);
		Assert.assertTrue(socialProfile.getSite().equals(TestSocialNetworkApiGenerator.getTestSocialNetworkType()));
		Assert.assertTrue(socialProfile.getEmail().equals(TestSocialNetworkApiGenerator.TEST_EMAIL));
		Assert.assertTrue(socialProfile.getProfileUrl().equals(TEST_PROFILE_URL));
		Assert.assertTrue(socialProfile.getUserId().equals(TestSocialNetworkApiGenerator.TEST_PROVIDER_USERID));
	}

	@Test
	public void testCreateUniqueHandle() throws Exception {
		TestSocialNetworkApiGenerator generator = new TestSocialNetworkApiGenerator(SocialNetworkType.FACEBOOK.getId());
		Connection<Object> connection = generator.getConnection();
		String expectedHandle = getExpectedUniqueHandleFromConnection(connection);
		Assert.assertEquals(expectedHandle, SocialProfile.createUniqueHandle(connection));
	}

	private String getExpectedUniqueHandleFromConnection(Connection<Object> connection) {
		ConnectionData connectionData = connection.createData();
		StringBuffer uniqueHandle = new StringBuffer();
		uniqueHandle.append(connectionData.getProviderUserId());
		uniqueHandle.append("@");
		uniqueHandle.append(connectionData.getProviderId());
		return uniqueHandle.toString();
	}
}

package com.phroogal.core.social;

import java.util.Arrays;

import org.mockito.Mockito;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.FacebookProfile;
import org.springframework.social.facebook.api.FqlOperations;
import org.springframework.social.facebook.api.FqlResultMapper;
import org.springframework.social.facebook.api.UserOperations;
import org.springframework.social.google.api.Google;
import org.springframework.social.linkedin.api.LinkedIn;
import org.springframework.social.linkedin.api.LinkedInProfile;
import org.springframework.social.linkedin.api.ProfileOperations;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.test.util.ReflectionTestUtils;

import com.phroogal.core.domain.Location;
import com.phroogal.core.exception.SocialNetworkNotSupportedException;

public final class TestSocialNetworkApiGenerator {
	
	public static final String TEST_EMAIL = "a@b.com";
	
	public static final String TEST_PROVIDER_USERID = "id12345";
	
	public static final String TEST_FIRSTNAME = "John";
	
	public static final String TEST_LASTNAME = "Smith";
	
	public static final String TEST_BIO = "Software Developer";
	
	public static final String TEST_PROFILEPIC_URL = "www.social.com/12345/pic.jpg";
	
	public static final String TEST_PROFILE_URL = "www.social.com/id12345";
	
	private static SocialNetworkType testSocialNetworkType;
	
	@SuppressWarnings("unchecked")
	private Connection<Object> connection = Mockito.mock(Connection.class);
	
	private ConnectionData data = Mockito.mock(ConnectionData.class);
	
	private UserProfile profile = Mockito.mock(UserProfile.class);
	
	private Object socialNetworkApi;
	
	private String providerId;
	
	public TestSocialNetworkApiGenerator(String providerId) {
		this.providerId = providerId;
		testSocialNetworkType = SocialNetworkType.get(providerId);
		setupMocks();		
	}
	
	public SocialNetwork getTestSocialNetworkApi() {
		SocialNetworkType networkType = SocialNetworkType.get(providerId);
		SocialNetwork api = null;
		switch (networkType) {
		case FACEBOOK:
			api = new FacebookApi();
			break;
		case GOOGLE:
			api = new GoogleApi();
			break;
		case LINKEDIN:
			api = new LinkedInApi();
			break;
		case TWITTER:
			api = new TwitterApi();
			break;
		}
		
		ReflectionTestUtils.setField(api, "connection", getConnection());
		Mockito.when(api.getProfileUrl()).thenReturn(TEST_PROFILE_URL);
		return api;
	}

	@SuppressWarnings("unchecked")
	public Object getTestApi() {
		SocialNetworkType networkType = SocialNetworkType.get(providerId);
		switch (networkType) {
		case FACEBOOK:
			Facebook facebook = Mockito.mock(Facebook.class);
			UserOperations userOperations = Mockito.mock(UserOperations.class);
			FacebookProfile fbProfile = Mockito.mock(FacebookProfile.class);
			FqlOperations fqlOperations = Mockito.mock(FqlOperations.class);
			Mockito.when(facebook.userOperations()).thenReturn(userOperations);
			Mockito.when(facebook.userOperations().getUserProfile()).thenReturn(fbProfile);
			Mockito.when(facebook.userOperations().getUserProfile().getBio()).thenReturn(TEST_BIO);
			Mockito.when(facebook.fqlOperations()).thenReturn(fqlOperations);
			Mockito.when(fqlOperations.query(Mockito.matches("SELECT current_location FROM user WHERE uid=me()"), Mockito.any(FqlResultMapper.class))).thenReturn(Arrays.asList(new Location()));
			return facebook;
		case GOOGLE:
			return Mockito.mock(Google.class);
		case LINKEDIN:
			LinkedIn linkedin = Mockito.mock(LinkedIn.class);
			String userId = "12345";
			ProfileOperations profileOperations = Mockito.mock(ProfileOperations.class);
			LinkedInProfile profile = Mockito.mock(LinkedInProfile.class);
			Mockito.when(profile.getId()).thenReturn(userId);
			Mockito.when(linkedin.profileOperations()).thenReturn(profileOperations);
			Mockito.when(linkedin.profileOperations().getUserProfile()).thenReturn(profile);
			return linkedin;
		case TWITTER:
			return Mockito.mock(Twitter.class);
		}
		throw new SocialNetworkNotSupportedException();
	}

	private void setupMocks() {
		socialNetworkApi = getTestApi();
		Mockito.when(connection.getApi()).thenReturn(socialNetworkApi);
		Mockito.when(connection.fetchUserProfile()).thenReturn(profile);
		Mockito.when(connection.createData()).thenReturn(data);
		Mockito.when(connection.createData().getProviderId()).thenReturn(providerId);
		Mockito.when(connection.createData().getProviderUserId()).thenReturn(TEST_PROVIDER_USERID);
		Mockito.when(connection.createData().getProfileUrl()).thenReturn(TEST_PROVIDER_USERID);
		Mockito.when(connection.createData().getImageUrl()).thenReturn(TEST_PROFILEPIC_URL);
		Mockito.when(connection.fetchUserProfile().getEmail()).thenReturn(TEST_EMAIL);
		Mockito.when(connection.fetchUserProfile().getFirstName()).thenReturn(TEST_FIRSTNAME);
		Mockito.when(connection.fetchUserProfile().getLastName()).thenReturn(TEST_LASTNAME);
	}

	public Connection<Object> getConnection() {
		return connection;
	}

	public static SocialNetworkType getTestSocialNetworkType() {
		return testSocialNetworkType;
	}
}


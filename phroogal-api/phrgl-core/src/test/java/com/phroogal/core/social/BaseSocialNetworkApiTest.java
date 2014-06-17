package com.phroogal.core.social;

import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.phroogal.core.domain.SocialContact;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/spring/app-context.xml"})
public abstract class BaseSocialNetworkApiTest extends TestCase {
	
	protected abstract String getProviderId();
	
	@Autowired
	private SocialNetworkResolver resolver;
	
	private TestSocialNetworkApiGenerator generator;
	
	private SocialNetwork socialNetwork;
	
	private List<SocialNetworkType> emailCheckExclusion = Arrays.asList(SocialNetworkType.TWITTER);
	
	@Before
	public void setUp() {
		generator = new TestSocialNetworkApiGenerator(getProviderId()); 
		socialNetwork = resolver.getApi(generator.getConnection());
	}
	
	@Test
	public void testGetSocialNetworkType() {
		SocialNetworkType type = SocialNetworkType.get(getProviderId());
		Assert.assertEquals(type, socialNetwork.getSocialNetworkType());
	}

	@Test
	public void testGetFirstName() {
		String firstName = socialNetwork.getFirstName();
		Assert.assertFalse(firstName.isEmpty());
		Assert.assertEquals(TestSocialNetworkApiGenerator.TEST_FIRSTNAME, firstName);
	}

	@Test
	public void testGetLastName() {
		String lastName = socialNetwork.getLastName();
		Assert.assertFalse(lastName.isEmpty());
		Assert.assertEquals(TestSocialNetworkApiGenerator.TEST_LASTNAME, lastName);
	}
	
	@Test
	public void testProfilePictureUrl() {
		String profilePictureUrl = socialNetwork.getProfilePictureUrl();
		Assert.assertFalse(profilePictureUrl.isEmpty());
		Assert.assertEquals(TestSocialNetworkApiGenerator.TEST_PROFILEPIC_URL, profilePictureUrl);
	}

	@Test
	public void testGetPrimaryEmail() {
		if ( ! emailCheckExclusion.contains(socialNetwork.getSocialNetworkType())) {
			String email = socialNetwork.getPrimaryEmail();
			Assert.assertFalse(email.isEmpty());
			Assert.assertEquals(TestSocialNetworkApiGenerator.TEST_EMAIL, email);			
		}
	}

	@Test
	@Ignore
	public void testReturnContactUserId() {
		List<SocialContact> contacts = socialNetwork.getSocialContacts(null);
		Assert.assertNotNull(contacts);
	}

	public SocialNetwork getSocialNetwork() {
		return socialNetwork;
	}
}

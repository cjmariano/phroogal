package com.phroogal.core.social;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/spring/app-context.xml"})
public class SocialNetworkResolverTest {

	@Autowired
	private SocialNetworkResolver resolver;
	
	private TestSocialNetworkApiGenerator generator;
	
	@Test
	public void testMapInitialized() {
		Assert.assertNotNull(resolver.getSocialNetworkMap());
	}
	
	@Test
	public void testGetApi() throws Exception {
		generator = new TestSocialNetworkApiGenerator("facebook"); 
		SocialNetwork facebook = resolver.getApi(generator.getConnection());
		Assert.assertTrue(facebook instanceof FacebookApi);
		
		generator = new TestSocialNetworkApiGenerator("linkedin"); 
		SocialNetwork linkedIn = resolver.getApi(generator.getConnection());
		Assert.assertTrue(linkedIn instanceof LinkedInApi);
		
		generator = new TestSocialNetworkApiGenerator("google");
		SocialNetwork google = resolver.getApi(generator.getConnection());
		Assert.assertTrue(google instanceof GoogleApi);
	}
}

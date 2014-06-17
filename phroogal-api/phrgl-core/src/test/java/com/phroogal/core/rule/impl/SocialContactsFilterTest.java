package com.phroogal.core.rule.impl;

import java.util.Collection;
import java.util.List;

import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.phroogal.core.domain.SocialContact;
import com.phroogal.core.domain.SocialProfile;
import com.phroogal.core.domain.User;
import com.phroogal.core.rule.Rule;
import com.phroogal.core.rule.RuleExecutionContext;
import com.phroogal.core.social.SocialNetworkType;
import com.phroogal.core.utility.CollectionUtil;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/spring/app-context.xml"})
public class SocialContactsFilterTest {

	@Autowired
	@Qualifier(value="socialContactsFilter")
	private Rule socialContactsFilter;
	
	private User user = new User();
	
	private List<SocialContact> socialContacts = CollectionUtil.arrayList();
	
	private ObjectId matchingContact;
	
	@Before
	public void setUp() {
		user.setSocialProfiles(generateTestSocialProfiles());
		socialContacts.addAll(generateSocialContacts());
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testSocialContactsFilterRule() throws Exception {
		List facts = CollectionUtil.arrayList();
		facts.add(user);
		facts.addAll(socialContacts);
		socialContactsFilter.setFacts(facts);
		RuleExecutionContext<ObjectId> executionContext = socialContactsFilter.execute();
		List<ObjectId> results = executionContext.getResults(); 
				
		Assert.assertTrue(results != null && results.size() == 1);
		Assert.assertNotNull(results.get(0).equals(matchingContact));
	}
	
	private List<SocialProfile> generateTestSocialProfiles() {
		List<SocialProfile> socialProfiles = CollectionUtil.arrayList();
		
		SocialProfile socialProfileFB = new SocialProfile();
		socialProfileFB.setEmail("user@FB.com");
		socialProfileFB.setSite(SocialNetworkType.FACEBOOK);
		socialProfileFB.setUserId("fbuser");
		socialProfiles.add(socialProfileFB);
		
		SocialProfile socialProfileTW = new SocialProfile();
		socialProfileTW.setEmail("user@TW.com");
		socialProfileTW.setSite(SocialNetworkType.TWITTER);
		socialProfileTW.setUserId("twuser");
		socialProfiles.add(socialProfileTW);
		
		return socialProfiles;
	}
	
	private Collection<? extends SocialContact> generateSocialContacts() {
		List<SocialContact> socialContacts = CollectionUtil.arrayList();
		matchingContact = ObjectId.get();
		
		SocialContact socialContactFB = new SocialContact();
		socialContactFB.setUserId(matchingContact);
		socialContactFB.setConnectedThru(SocialNetworkType.FACEBOOK);
		socialContactFB.setContactUserId("fbuser");
		socialContacts.add(socialContactFB);
		
		return socialContacts;
	}
}

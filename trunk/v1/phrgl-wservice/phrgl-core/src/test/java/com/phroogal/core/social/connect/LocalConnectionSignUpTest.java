package com.phroogal.core.social.connect;


import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.phroogal.core.domain.User;
import com.phroogal.core.repository.UserRepository;
import com.phroogal.core.service.UserService;
import com.phroogal.core.social.SocialNetworkType;
import com.phroogal.core.social.TestSocialNetworkApiGenerator;
import com.phroogal.core.test.helper.RepositoryCleanupHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/spring/app-context.xml" })
public class LocalConnectionSignUpTest {

	@Autowired
	private LocalConnectionSignUp connectionSignup;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@After
	public void tearDown() {
		RepositoryCleanupHelper.dropCollection(mongoTemplate);
	}
	
	@Test
	public void testExecute() throws Exception {
		ReflectionTestUtils.setField(userService, "repository", userRepository);
		ReflectionTestUtils.setField(connectionSignup, "userService", userService);
		
		TestSocialNetworkApiGenerator apiGenerator = new TestSocialNetworkApiGenerator(SocialNetworkType.FACEBOOK.getId());
		String username = getExpectedUsername(SocialNetworkType.FACEBOOK);
		String userId = connectionSignup.execute(apiGenerator.getConnection());
		
		User user = userService.findById(new ObjectId(userId));
		Assert.assertNotNull(user);
		Assert.assertTrue(user.getUsername().equals(username));
		Assert.assertTrue(user.getPrimarySocialNetworkConnection().equals(TestSocialNetworkApiGenerator.getTestSocialNetworkType()));
	}

	private String getExpectedUsername(SocialNetworkType socialNetwork) {
		StringBuffer sb = new StringBuffer(TestSocialNetworkApiGenerator.TEST_PROVIDER_USERID);
		sb.append("@").append(socialNetwork.getId());
		return sb.toString();
	}

}

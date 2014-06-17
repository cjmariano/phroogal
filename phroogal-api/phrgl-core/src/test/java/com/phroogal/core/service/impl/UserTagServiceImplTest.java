package com.phroogal.core.service.impl;


import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;

import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.phroogal.core.domain.User;
import com.phroogal.core.domain.UserTag;
import com.phroogal.core.repository.TestEntityGenerator;
import com.phroogal.core.repository.UserTagRepository;
import com.phroogal.core.service.Service;
import com.phroogal.core.service.UserTagService;
import com.phroogal.core.test.helper.RepositoryCleanupHelper;
import com.phroogal.core.test.helper.UnwrapProxyHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/spring/app-context.xml"})
public class UserTagServiceImplTest extends BaseServiceTest<UserTag, ObjectId, UserTagRepository> {

	@Autowired
	private UserTagService serviceImpl;
	
	@Autowired
	private UserTagRepository userTagRepository;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Value(value="${usertags.default.values}")
	public String[] defaultUserTags;
	
	@Override
	@SuppressWarnings("unchecked")
	protected Service<UserTag, ObjectId> returnServiceImpl() {
		return (Service<UserTag, ObjectId>) UnwrapProxyHelper.unwrapProxy(UserTagServiceImpl.class, serviceImpl);
	}
	
	@Override
	protected UserTagRepository returnMongoRepository() {
		return Mockito.mock(UserTagRepository.class);
	}

	@Override
	protected UserTag returnDomainInstance() {
		TestEntityGenerator generator = new TestEntityGenerator(); 
		return generator.generateTestUserTag();
	}
	
	@After
	public void tearDown() {
		RepositoryCleanupHelper.dropCollection(mongoTemplate);
	}

	@Test
	public void testGetByUserId() throws Exception {
		ObjectId testUserId = ObjectId.get();
		UserTagRepository repository = Mockito.mock(UserTagRepository.class);
		UserTagService service = (UserTagService) returnServiceImpl();
		ReflectionTestUtils.setField(service, "userTagRepository", repository);
		
		service.getByUserId(testUserId);
		verify(repository, atLeastOnce()).findByUserId(testUserId);
	}
	
	@Test
		public void testCreateDefaultUserTagsFor() throws Exception {
			TestEntityGenerator generator = new TestEntityGenerator();
			ReflectionTestUtils.setField(serviceImpl, "repository", userTagRepository);
			User user = generator.generateTestUser();
			user.setCreatedOn(DateTime.now());
			user.setModifiedOn(user.getCreatedOn());
			serviceImpl.createDefaultUserTagsFor(user);
			
			List<UserTag> userTags = serviceImpl.getByUserId(user.getId());
			List<String> lstDefaultUserTags = Arrays.asList(defaultUserTags); 
			for (UserTag userTag : userTags) {
				Assert.assertTrue(lstDefaultUserTags.contains(userTag.getName()));
			}	
		}
	
	@Test
	public void testSaveOrUpdate() {
		ObjectId userId = ObjectId.get();
		UserTagService service = (UserTagService) returnServiceImpl();
		ReflectionTestUtils.setField(service, "userTagRepository", userTagRepository);
		ReflectionTestUtils.setField(service, "repository", userTagRepository);
		
		UserTag testUserTag1 = returnDomainInstance();
		testUserTag1.setName("Test Tag");
		testUserTag1.setUserId(userId);
		service.saveOrUpdate(testUserTag1);
		
		List<UserTag> userTags = service.getByUserId(userId);
		Assert.assertTrue(userTags.size() == 1);
		
		UserTag testUserTag2 = returnDomainInstance();
		testUserTag2.setName("test tag");
		testUserTag2.setUserId(userId);
		service.saveOrUpdate(testUserTag2);
		
		userTags = service.getByUserId(userId);
		Assert.assertTrue(userTags.size() == 1);
		
		UserTag testUserTag3 = returnDomainInstance();
		testUserTag3.setName("another tag");
		testUserTag3.setUserId(userId);
		service.saveOrUpdate(testUserTag3);
		
		userTags = service.getByUserId(userId);
		Assert.assertTrue(userTags.size() == 2);
	}

}

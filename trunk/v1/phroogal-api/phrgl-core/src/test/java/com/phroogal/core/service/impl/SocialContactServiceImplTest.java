package com.phroogal.core.service.impl;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.social.connect.Connection;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.phroogal.core.domain.SocialContact;
import com.phroogal.core.domain.User;
import com.phroogal.core.repository.SocialContactRepository;
import com.phroogal.core.repository.TestEntityGenerator;
import com.phroogal.core.service.Service;
import com.phroogal.core.service.SocialContactService;
import com.phroogal.core.social.SocialNetwork;
import com.phroogal.core.social.SocialNetworkResolver;
import com.phroogal.core.social.SocialNetworkType;
import com.phroogal.core.social.TestSocialNetworkApiGenerator;
import com.phroogal.core.utility.CollectionUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/spring/app-context.xml"})
public class SocialContactServiceImplTest extends BaseServiceTest<SocialContact, ObjectId, SocialContactRepository> {

	private SocialContactService serviceImpl = new SocialContactServiceImpl();
	
	private ObjectId userId;
	
	private SocialNetworkResolver socialNetworkResolver;
	
	private SocialContactRepository socialcontactRepository;
	
	private TestSocialNetworkApiGenerator generator;
	
	private TestEntityGenerator entityGenerator = new TestEntityGenerator();
	
	private Connection<?> mockConnection;
	
	private SocialNetwork api;
	
	private List<SocialContact> socialContactsList;
	
	@Override
	protected Service<SocialContact, ObjectId> returnServiceImpl() {
		return serviceImpl;
	}
	
	@Override
	protected SocialContactRepository returnMongoRepository() {
		return Mockito.mock(SocialContactRepository.class);
	}

	@Override
	protected SocialContact returnDomainInstance() {
		TestEntityGenerator generator = new TestEntityGenerator(); 
		return generator.generateTestSocialContact();
	}
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		userId = ObjectId.get();
		generator = new TestSocialNetworkApiGenerator("facebook");
		socialNetworkResolver = Mockito.mock(SocialNetworkResolver.class);
		socialcontactRepository= returnMongoRepository();
		api = Mockito.mock(SocialNetwork.class);
		mockConnection = generator.getConnection();
		socialContactsList = CollectionUtil.arrayList();
		ReflectionTestUtils.setField(serviceImpl, "socialNetworkResolver", socialNetworkResolver);
		ReflectionTestUtils.setField(serviceImpl, "socialcontactRepository", socialcontactRepository);
		
		Mockito.when(socialNetworkResolver.getApi(mockConnection)).thenReturn(api);
		Mockito.when(socialcontactRepository.findByUserIdAndConnectedThru(userId, SocialNetworkType.FACEBOOK)).thenReturn(null);
		Mockito.when(api.getSocialContacts(userId)).thenReturn(socialContactsList);
		Mockito.when(api.getSocialNetworkType()).thenReturn(SocialNetworkType.FACEBOOK);
	}
	
	@Test
	public void testGetByUserId() {
		ObjectId userId = ObjectId.get();
		SocialContactRepository socialcontactRepository = Mockito.mock(SocialContactRepository.class);
		ReflectionTestUtils.setField(serviceImpl, "socialcontactRepository", socialcontactRepository);
		serviceImpl.getByUserId(userId);
		verify(socialcontactRepository, atLeastOnce()).findByUserId(userId);
	}
	
	@Test
	public void testGetByUserIdAndConnectedThru() {
		ObjectId userId = ObjectId.get();
		SocialNetworkType connectedThru = SocialNetworkType.GOOGLE;
		SocialContactRepository socialcontactRepository = Mockito.mock(SocialContactRepository.class);
		ReflectionTestUtils.setField(serviceImpl, "socialcontactRepository", socialcontactRepository);
		serviceImpl.getByUserIdAndConnectedThru(userId, connectedThru);
		verify(socialcontactRepository, atLeastOnce()).findByUserIdAndConnectedThru(userId, connectedThru);
	}
	
	@Test
	public void testSynchSocialNetworkContacts() {
		User user = entityGenerator.generateTestUser();
		ObjectId userId = user.getId();
		serviceImpl = Mockito.spy(serviceImpl);
		serviceImpl.synchSocialNetworkContacts(user, mockConnection);
		verify(serviceImpl, Mockito.atLeast(1)).removeSocialNetworkContacts(userId, SocialNetworkType.FACEBOOK);
		verify(serviceImpl, Mockito.atLeast(1)).saveOrUpdate(socialContactsList);
	}
	
	@Test
	public void testRemoveSocialNetworkContacts() {
		User user = entityGenerator.generateTestUser();
		ObjectId userId = user.getId();
		serviceImpl = Mockito.spy(serviceImpl);
		serviceImpl.synchSocialNetworkContacts(user, mockConnection);
		verify(serviceImpl, Mockito.atLeast(1)).removeSocialNetworkContacts(userId, SocialNetworkType.FACEBOOK);
		verify(serviceImpl, Mockito.never()).saveOrUpdate((SocialContact) Mockito.any());
	}
}

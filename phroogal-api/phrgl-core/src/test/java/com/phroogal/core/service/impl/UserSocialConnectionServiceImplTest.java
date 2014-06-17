package com.phroogal.core.service.impl;



import static org.mockito.Mockito.*;

import java.util.Arrays;

import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.phroogal.core.domain.UserSocialConnection;
import com.phroogal.core.repository.UserSocialConnectionRepository;
import com.phroogal.core.service.UserSocialConnectionService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/spring/app-context.xml"})
public class UserSocialConnectionServiceImplTest extends BaseServiceTest<UserSocialConnection, ObjectId, UserSocialConnectionRepository> {

	@Autowired
	private UserSocialConnectionService serviceImpl;
	
	@Autowired
	private UserSocialConnectionRepository repository;
	
	@Override
	protected UserSocialConnectionService returnServiceImpl() {
		return serviceImpl;
	}
	
	@Override
	protected UserSocialConnectionRepository returnMongoRepository() {
		repository = Mockito.mock(UserSocialConnectionRepository.class);
		return repository;
	}
	
	@Override
	protected UserSocialConnection returnDomainInstance() {
		return new UserSocialConnection();
	}

	@Test
	public void testRetrieveUserSocialConnection() {
		UserSocialConnectionRepository userSocialConnectionRepository = mock(UserSocialConnectionRepository.class);
		UserSocialConnection userConnection = mock(UserSocialConnection.class);
		ReflectionTestUtils.setField(serviceImpl, "userSocialConnectionRepository", userSocialConnectionRepository);
		ObjectId userId = new ObjectId("51f1ca124d7ad8e29cf2698b");
		Connection<?> connection = Mockito.mock(Connection.class);
		ConnectionData data = new ConnectionData("facebook", userId.toString(), "John", "url", "imageUrl", "accessToken", "secret", "refreshToken", 10L);
		Mockito.when(connection.createData()).thenReturn(data);
		Mockito.when(userSocialConnectionRepository.findByProviderIdAndProviderUserId(anyString(), anyString())).thenReturn(Arrays.asList(new UserSocialConnection[] {userConnection}));
		
		UserSocialConnection result = serviceImpl.retrieveUserSocialConnection(connection);
		Assert.assertNotNull(result);
		verify(userSocialConnectionRepository, atLeastOnce()).findByProviderIdAndProviderUserId(anyString(), anyString());
	}
}

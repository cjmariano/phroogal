package com.phroogal.core.service.impl;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.phroogal.core.domain.User;

@RunWith(MockitoJUnitRunner.class)
public class DocumentAuditorServiceImplTest {

	@Mock
	private AuthenticationDetailsServiceImpl authenticationService;
	
	@Mock
	private User authenticatedUser;
	
	private DocumentAuditorServiceImpl documentAuditorServiceImpl = new DocumentAuditorServiceImpl();

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		ReflectionTestUtils.setField(documentAuditorServiceImpl, "authenticationService", authenticationService);
		when(authenticationService.getAuthenticatedUser()).thenReturn(authenticatedUser);
	}
	
	@Test
	public void testGetCurrentAuditor() {
		User user = documentAuditorServiceImpl.getCurrentAuditor();
		verify(authenticationService, atLeastOnce()).getAuthenticatedUser();
		Assert.assertNotNull(user);
	}

}

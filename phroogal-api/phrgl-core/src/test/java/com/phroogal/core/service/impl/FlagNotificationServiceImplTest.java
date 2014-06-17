package com.phroogal.core.service.impl;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.phroogal.core.domain.FlagNotificationRequest;
import com.phroogal.core.domain.FlagNotificationStatusType;
import com.phroogal.core.domain.Post;
import com.phroogal.core.domain.PostType;
import com.phroogal.core.repository.FlagNotificationRequestRepository;
import com.phroogal.core.repository.TestEntityGenerator;
import com.phroogal.core.service.FlagNotificationService;
import com.phroogal.core.service.Service;
import com.phroogal.core.utility.CollectionUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/spring/app-context.xml"})
public class FlagNotificationServiceImplTest extends BaseServiceTest<FlagNotificationRequest, ObjectId, FlagNotificationRequestRepository> {

	@Autowired
	private FlagNotificationService serviceImpl;
	
	private TestEntityGenerator generator = new TestEntityGenerator();
	
	@Override
	protected Service<FlagNotificationRequest, ObjectId> returnServiceImpl() {
		return serviceImpl;
	}
	
	@Override
	protected FlagNotificationRequestRepository returnMongoRepository() {
		return Mockito.mock(FlagNotificationRequestRepository.class);
	}

	@Override
	protected FlagNotificationRequest returnDomainInstance() {
		TestEntityGenerator generator = new TestEntityGenerator(); 
		return generator.generateTestFlagNotificationRequest();
	}
	
	@Test
	public void testCreateRequest() {
		TestEntityGenerator generator = new TestEntityGenerator();
		Post post = generator.generateTestQuestion();
		String content = "This is a flagged content";
		FlagNotificationService mockedService = Mockito.spy(serviceImpl);
		FlagNotificationRequest request = mockedService.createRequest(post, content);
		Assert.assertEquals(request.getRefId(), post.getId());
		Assert.assertEquals(request.getType(), PostType.QUESTION);
		Assert.assertEquals(request.getContent(), content);
		Assert.assertEquals(request.getPostBy().getId(), post.getPostBy().getId());
		Mockito.verify(mockedService, Mockito.atLeastOnce()).saveOrUpdate(request);
	}
	
	@Test
	public void testUpdateStatus() {
		List<FlagNotificationRequest> requests = generateTestFlagNotificationRequests();
		Post post = generator.generateTestQuestion();
		FlagNotificationRequestRepository repository = returnMongoRepository();
		ReflectionTestUtils.setField(serviceImpl, "flagNotificationRequestRepository", repository);
		when(repository.findByRefIdAndType(post.getId(), post.getPostType())).thenReturn(requests);
		serviceImpl.updateStatus(post, FlagNotificationStatusType.DELETED);
		Assert.assertEquals(FlagNotificationStatusType.DELETED, requests.get(0).getStatus());
	}
	
	

	@Test
	public void testGetByStatusAndType() {
		FlagNotificationRequestRepository repository = returnMongoRepository();
		ReflectionTestUtils.setField(serviceImpl, "flagNotificationRequestRepository", repository);
		serviceImpl.getByStatusAndType(FlagNotificationStatusType.ACTIVE, PostType.QUESTION, 0, 10);
		verify(repository, atLeastOnce()).findByStatusAndType(FlagNotificationStatusType.ACTIVE, PostType.QUESTION, new PageRequest(0, 10));
	}
	
	@Test
	public void testGetRefIdAsStringByStatusAndType() {
		List<FlagNotificationRequest> requests = generateTestFlagNotificationRequests();
		FlagNotificationRequestRepository repository = returnMongoRepository();
		ReflectionTestUtils.setField(serviceImpl, "flagNotificationRequestRepository", repository);
		when(repository.findByStatusAndType(FlagNotificationStatusType.ACTIVE, PostType.QUESTION, new PageRequest(0, 10))).thenReturn(requests);
		
		List<String> results = serviceImpl.getRefIdAsStringByStatusAndType(FlagNotificationStatusType.ACTIVE, PostType.QUESTION, 0, 10);
		Assert.assertEquals(requests.get(0).getRefId().toString(), results.get(0));
	}
	
	private List<FlagNotificationRequest> generateTestFlagNotificationRequests() {
		List<FlagNotificationRequest> requests = CollectionUtil.arrayList();
		FlagNotificationRequest flagNotificationRequest = generator.generateTestFlagNotificationRequest();
		requests.add(flagNotificationRequest);
		return requests;
	}
}

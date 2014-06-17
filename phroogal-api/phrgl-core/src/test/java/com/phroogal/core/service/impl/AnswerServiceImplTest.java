package com.phroogal.core.service.impl;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.phroogal.core.domain.Answer;
import com.phroogal.core.domain.User;
import com.phroogal.core.domain.security.UserRoleType;
import com.phroogal.core.repository.AnswerRepository;
import com.phroogal.core.repository.TestEntityGenerator;
import com.phroogal.core.service.AnswerService;
import com.phroogal.core.service.FlagNotificationService;
import com.phroogal.core.service.Service;
import com.phroogal.core.test.helper.UnwrapProxyHelper;
import com.phroogal.core.utility.CollectionUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/spring/app-context.xml"})
public class AnswerServiceImplTest extends BaseServiceTest<Answer, ObjectId, AnswerRepository> {

	@Autowired
	private AnswerService serviceImpl;
	
	private AnswerRepository answerRepository = Mockito.mock(AnswerRepository.class);
	
	@Override
	protected Service<Answer, ObjectId> returnServiceImpl() {
		return (AnswerService) UnwrapProxyHelper.unwrapProxy(AnswerServiceImpl.class, serviceImpl);
	}
	
	@Override
	protected AnswerRepository returnMongoRepository() {
		return Mockito.mock(AnswerRepository.class);
	}

	@Override
	protected Answer returnDomainInstance() {
		TestEntityGenerator generator = new TestEntityGenerator(); 
		return generator.generateTestAnswer();
	}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		serviceImpl = (AnswerService) returnServiceImpl();
	}
	
	@Override
	public void testDelete() {
		setAuthenticationPrincipal("ADMIN");
		super.testDelete();
	}
	
	@Test
	public void testGetByQuestionRef() {
		ObjectId testId = ObjectId.get();
		ReflectionTestUtils.setField(serviceImpl, "answerRepository", answerRepository);
		serviceImpl.getByQuestionRef(testId);
		verify(answerRepository, atLeastOnce()).findByQuestionRef(testId);
	}
	
	@Test
	public void testGetByUserId() {
		ObjectId testId = ObjectId.get();
		Pageable pageRequest = new PageRequest(0, 10);
		ReflectionTestUtils.setField(serviceImpl, "answerRepository", answerRepository);
		serviceImpl.getByUserId(testId, 0, 10);
		verify(answerRepository, atLeastOnce()).findByPostById(testId, pageRequest);
	}
	
	@Test
	public void testGetRootQuestionId() {
		TestEntityGenerator generator = new TestEntityGenerator();
		ObjectId testId = ObjectId.get();
		Answer answer = generator.generateTestAnswer();
		answer.setQuestionRef(testId);
		serviceImpl.saveOrUpdate(answer);
		Assert.assertEquals(testId, answer.getRootQuestionId());
	}
	
	@Test
	public void testSortAnswersByHierarchyRulesImplemented() {
		TestEntityGenerator generator = new TestEntityGenerator();
		List<Answer> answers = CollectionUtil.arrayList();

		Answer answer1 = generator.generateAnswerWithVotesTotalOf(100);
		answers.add(answer1);
		
		Answer answer2 = generator.generateAnswerWithVotesTotalOf(200);
		answers.add(answer2);
		
		Answer answer3 = generator.generateAnswerWithVotesTotalOf(300);
		answers.add(answer3);

		serviceImpl.sortByAnswerHierarchyRules(answers);
		
		Assert.assertTrue(answers.get(0).getId().equals(answer3.getId()));
		Assert.assertTrue(answers.get(1).getId().equals(answer2.getId()));
		Assert.assertTrue(answers.get(2).getId().equals(answer1.getId()));
	}
	
	@Test
	public void testFlagAnswer() {
		TestEntityGenerator generator = new TestEntityGenerator();
		FlagNotificationService flagNotificationService =  Mockito.mock(FlagNotificationService.class);
		ReflectionTestUtils.setField(serviceImpl, "flagNotificationService", flagNotificationService);
		Answer flaggedAnswer = generator.generateTestAnswer();
		String content = flaggedAnswer.getContent();
		serviceImpl.flagAnswer(flaggedAnswer);
		verify(flagNotificationService, atLeastOnce()).createRequest(flaggedAnswer, content);
	}
	
	private void setAuthenticationPrincipal(String role) {
		TestEntityGenerator generator = new TestEntityGenerator(); 
		User user = generator.generateTestUser();
		List<UserRoleType> authorities = CollectionUtil.arrayList();
		authorities.add(UserRoleType.get(role));
		user.setGrantedAuthorities(authorities);
		UserCredentials credentials  = new UserCredentials("username", "password");
        Authentication auth = new UsernamePasswordAuthenticationToken(user, credentials, authorities);
        SecurityContextHolder.getContext().setAuthentication(auth);
	}
}

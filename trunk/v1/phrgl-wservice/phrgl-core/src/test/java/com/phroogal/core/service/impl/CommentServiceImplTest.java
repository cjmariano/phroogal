package com.phroogal.core.service.impl;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.phroogal.core.domain.Answer;
import com.phroogal.core.domain.Comment;
import com.phroogal.core.repository.CommentRepository;
import com.phroogal.core.repository.TestEntityGenerator;
import com.phroogal.core.service.AnswerService;
import com.phroogal.core.service.CommentService;
import com.phroogal.core.service.Service;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/spring/app-context.xml"})
public class CommentServiceImplTest extends BaseServiceTest<Comment, ObjectId, CommentRepository> {

	@Autowired
	private CommentService serviceImpl;
	
	private CommentRepository commentRepository = Mockito.mock(CommentRepository.class);
	
	private AnswerService answerService = Mockito.mock(AnswerService.class);
	
	@Override
	protected Service<Comment, ObjectId> returnServiceImpl() {
		return serviceImpl;
	}
	
	@Override
	protected CommentRepository returnMongoRepository() {
		return Mockito.mock(CommentRepository.class);
	}

	@Override
	protected Comment returnDomainInstance() {
		TestEntityGenerator generator = new TestEntityGenerator(); 
		return generator.generateTestComment();
	}
	
	@Test
	public void testGetByAnswerRef() {
		ObjectId testId = ObjectId.get();
		ReflectionTestUtils.setField(serviceImpl, "commentRepository", commentRepository);
		serviceImpl.getByAnswerRef(testId);
		verify(commentRepository, atLeastOnce()).findByAnswerRef(testId);
	}
	
	@Test
	public void testGetRootQuestionId() {
		TestEntityGenerator generator = new TestEntityGenerator();
		ObjectId testId = ObjectId.get();
		Answer answer = generator.generateTestAnswer();
		answer.setQuestionRef(testId);
		ReflectionTestUtils.setField(serviceImpl, "answerService", answerService);
		Mockito.when(answerService.findById((ObjectId) Mockito.any())).thenReturn(answer);
		
		Comment comment = returnDomainInstance();
		serviceImpl.saveOrUpdate(comment);
		Assert.assertEquals(testId, comment.getRootQuestionId());
	}
}

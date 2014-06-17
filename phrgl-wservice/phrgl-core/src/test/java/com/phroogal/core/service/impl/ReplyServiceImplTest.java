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

import com.phroogal.core.domain.Comment;
import com.phroogal.core.domain.Reply;
import com.phroogal.core.repository.ReplyRepository;
import com.phroogal.core.repository.TestEntityGenerator;
import com.phroogal.core.service.CommentService;
import com.phroogal.core.service.ReplyService;
import com.phroogal.core.service.Service;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/spring/app-context.xml"})
public class ReplyServiceImplTest extends BaseServiceTest<Reply, ObjectId, ReplyRepository> {

	@Autowired
	private ReplyService serviceImpl;
	
	private ReplyRepository replyRepository = Mockito.mock(ReplyRepository.class);
	
	private CommentService commentService = Mockito.mock(CommentService.class);
	
	@Override
	protected Service<Reply, ObjectId> returnServiceImpl() {
		return serviceImpl;
	}
	
	@Override
	protected ReplyRepository returnMongoRepository() {
		return Mockito.mock(ReplyRepository.class);
	}

	@Override
	protected Reply returnDomainInstance() {
		TestEntityGenerator generator = new TestEntityGenerator(); 
		return generator.generateTestReply();
	}
	
	@Test
	public void testGetByAnswerRef() {
		ObjectId testId = ObjectId.get();
		ReflectionTestUtils.setField(serviceImpl, "replyRepository", replyRepository);
		serviceImpl.getByCommentRef(testId);
		verify(replyRepository, atLeastOnce()).findByCommentRef(testId);
	}
	
	@Test
	public void testGetRootQuestionId() {
		TestEntityGenerator generator = new TestEntityGenerator();
		ObjectId testId = ObjectId.get();
		Comment comment  = generator.generateTestComment();
		comment.setRootQuestionId(testId);
		ReflectionTestUtils.setField(serviceImpl, "commentService", commentService);
		Mockito.when(commentService.findById((ObjectId) Mockito.any())).thenReturn(comment);
		
		Reply reply = returnDomainInstance();
		serviceImpl.saveOrUpdate(reply);
		Assert.assertEquals(testId, reply.getRootQuestionId());
	}
}

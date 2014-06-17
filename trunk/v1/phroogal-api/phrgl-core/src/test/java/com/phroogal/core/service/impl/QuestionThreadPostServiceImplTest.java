package com.phroogal.core.service.impl;


import java.util.Arrays;

import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.phroogal.core.domain.Answer;
import com.phroogal.core.domain.Comment;
import com.phroogal.core.domain.Question;
import com.phroogal.core.domain.Reply;
import com.phroogal.core.repository.QuestionRepository;
import com.phroogal.core.repository.TestEntityGenerator;
import com.phroogal.core.service.AnswerService;
import com.phroogal.core.service.CommentService;
import com.phroogal.core.service.QuestionService;
import com.phroogal.core.service.QuestionThreadPostService;
import com.phroogal.core.service.ReplyService;
import com.phroogal.core.test.helper.RepositoryCleanupHelper;
import com.phroogal.core.test.helper.UnwrapProxyHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/spring/app-context.xml"})
public class QuestionThreadPostServiceImplTest {
	
	@Autowired
	private QuestionThreadPostService questionThreadPostService;
	
	@Autowired
	private QuestionRepository questionRepository;
	
	@Autowired
	private QuestionService questionService;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	private Question question;
	
	private Answer answer;
	
	private Comment comment;
	
	private Reply reply; 
	
	@Before
	public void setUp() {
		QuestionRepository questionRepository = Mockito.mock(QuestionRepository.class);
		AnswerService answerService = Mockito.mock(AnswerService.class);
		CommentService commentService = Mockito.mock(CommentService.class);
		ReplyService replyService = Mockito.mock(ReplyService.class);
		TestEntityGenerator generator = new TestEntityGenerator();
		questionService = (QuestionService) UnwrapProxyHelper.unwrapProxy(QuestionServiceImpl.class, questionService);
		question = generator.generateTestQuestion();
		answer =  generator.generateTestAnswer();
		answer.setQuestionRef(question.getId());
		comment = generator.generateTestComment();
		comment.setAnswerRef(answer.getId());
		reply = generator.generateTestReply();
		reply.setCommentRef(comment.getId());
		
		ReflectionTestUtils.setField(questionService, "repository", this.questionRepository);
		ReflectionTestUtils.setField(questionThreadPostService, "questionRepository", questionRepository);
		ReflectionTestUtils.setField(questionThreadPostService, "answerService", answerService);
		ReflectionTestUtils.setField(questionThreadPostService, "commentService", commentService);
		ReflectionTestUtils.setField(questionThreadPostService, "replyService", replyService);
		
		Mockito.when(questionRepository.findOne(Mockito.any(ObjectId.class))).thenReturn(question);
		Mockito.when(answerService.getByQuestionRef(Mockito.any(ObjectId.class))).thenReturn(Arrays.asList(answer));
		Mockito.when(commentService.getByAnswerRef(Mockito.any(ObjectId.class))).thenReturn(Arrays.asList(comment));
		Mockito.when(replyService.getByCommentRef(Mockito.any(ObjectId.class))).thenReturn(Arrays.asList(reply));
	}
	
	@After
	public void tearDown() {
		RepositoryCleanupHelper.dropCollection(mongoTemplate);
	}

	@Test
	public void testRefreshQuestionAnswerThread() throws Exception {
		questionThreadPostService.refreshQuestionAnswerThread(answer);
		Question result = questionService.findById(question.getId());
		Assert.assertEquals(result.getAnswers().get(0).getId(), answer.getId());
		Assert.assertEquals(result.getAnswers().get(0).getComments().get(0).getId(), comment.getId());
		Assert.assertEquals(result.getAnswers().get(0).getComments().get(0).getReplies().get(0).getId(), reply.getId());
	}

}

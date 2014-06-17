package com.phroogal.core.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import junit.framework.AssertionFailedError;

import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.phroogal.core.domain.Answer;
import com.phroogal.core.domain.Comment;
import com.phroogal.core.domain.Question;
import com.phroogal.core.domain.Reply;
import com.phroogal.core.domain.User;
import com.phroogal.core.utility.CollectionUtil;

public class AnswerRepositoryTest extends BaseRepositoryTest<Answer, ObjectId, AnswerRepository> {

	@Autowired
	private AnswerRepository repository;
	
	@Autowired
	private QuestionRepository questionRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private ReplyRepository replyRepository;
	
	private Question questionRef;
	
	private User userRef;
	
	private TestEntityGenerator generator = new TestEntityGenerator(); 
	
	@Before
	public void setUp() {
		super.setUp();
		persistUserReference();
		persistQuestionReference();
	}
	
	private void persistUserReference() {
		TestEntityGenerator generator = new TestEntityGenerator();
		userRef = generator.generateTestUser();
		userRepository.save(userRef);		
	}

	private void persistQuestionReference() {
		TestEntityGenerator generator = new TestEntityGenerator();
		questionRef = generator.generateTestQuestion();
		questionRef.getPostBy().setId(userRef.getId());
		questionRepository.save(questionRef);
	}

	@Override
	protected AnswerRepository returnRepository() {
		return repository;
	}

	@Override
	protected Class<Answer> returnDomainClass() {
		return Answer.class;
	}

	@Override
	protected Answer createDomain() {
		TestEntityGenerator generator = new TestEntityGenerator();
		Answer answer = generator.generateTestAnswer();
		answer.setQuestionRef(questionRef.getId());
		answer.setComments(generateTestComments(generator));
		return answer;
	}

	@Override
	protected void modifyDomain(Answer answer) {
		answer.setContent("This is a modified content");
	}

	@Override
	protected boolean assertDomainEquals(Answer domain, Answer other) {
		try {
			Assert.assertEquals(domain.getId(), other.getId());
			Assert.assertEquals(domain.getQuestionRef(), other.getQuestionRef());
			Assert.assertEquals(domain.getContent(), other.getContent());
			return true;
		} catch (AssertionFailedError e) {
			return false;
		}
	}
	
	@Test
	public void testFindAllByIds() {
		List<ObjectId> answersId = CollectionUtil.arrayList();
		Answer answer1 = createDomain();
		answersId.add(answer1.getId());
		repository.save(answer1);
		
		Answer answer2 = createDomain();
		answersId.add(answer2.getId());
		repository.save(answer2);
		
		Answer answer3 = createDomain();
		answersId.add(answer3.getId());
		repository.save(answer3);
		
		Answer answer4 = createDomain();
		repository.save(answer4);
		
		List<Answer> results = repository.findAll(answersId, new PageRequest(0, 10)).getContent();
		Assert.assertTrue(results.size() == 3);
		assertListIDsAreContainedInPersistedItems(answersId, results);
	}
	
	@Test
	public void testFindByCreatedOnDateRanges() {
		List<ObjectId> answersId = CollectionUtil.arrayList();
		Answer answer1 = createDomain();
		answer1.setCreatedOn(DateTime.now().minusDays(30));
		answersId.add(answer1.getId());
		repository.save(answer1);
		
		Answer answer2 = createDomain();
		answer2.setCreatedOn(DateTime.now().minusDays(20));
		repository.save(answer2);
		
		Answer answer3 = createDomain();
		answer3.setCreatedOn(DateTime.now().minusDays(10));
		repository.save(answer3);
		
		Answer answer4 = createDomain();
		answer4.setCreatedOn(DateTime.now());
		repository.save(answer4);
		
		List<Answer> results = repository.findByCreatedOnBetween(DateTime.now().minusDays(11), DateTime.now(), 
				new PageRequest(0, 10, new Sort(Sort.Direction.DESC, "createdOn"))).getContent();
		assertEquals(results.get(0).getId(), answer4.getId());
		assertEquals(results.get(1).getId(), answer3.getId());
	}
	
	@Test
	public void testFindByQuestionRef() {
		Answer answer = createDomain();
		repository.save(answer);
		
		List<Answer> persistedAnswer = repository.findByQuestionRef(questionRef.getId());
		assertNotNull(persistedAnswer);
		assertNotNull(persistedAnswer.get(0));
		assertDomainEquals(answer, persistedAnswer.get(0));
	}
	
	@Test
	public void testFindByPostById() {
		Answer answer = createDomain();
		ObjectId userId = answer.getPostBy().getId();
		repository.save(answer);
		
		Pageable pageRequest = new PageRequest(0, 10);
		List<Answer> results = repository.findByPostById(userId, pageRequest).getContent();
		assertNotNull(results);
		assertTrue(results.size() == 1);
		assertDomainEquals(answer, results.get(0));
	}
	
	@Test
	public void testAnswerMetaDataIsUpdated() {
		Answer answer = new Answer();
		repository.save(answer);
		Assert.assertTrue(answer.getTotalCommentCount() == 0);
		
		
		Comment comment = new Comment();
		comment.setAnswerRef(answer.getId());
		commentRepository.save(comment);
		
		Answer persistedAnswer = repository.findOne(answer.getId());
		Assert.assertTrue(persistedAnswer.getTotalCommentCount() == 1);
		
		Comment anotherComment = new Comment();
		anotherComment.setAnswerRef(answer.getId());
		commentRepository.save(anotherComment);
		
		persistedAnswer = repository.findOne(answer.getId());
		Assert.assertTrue(persistedAnswer.getTotalCommentCount() == 2);
	}
	
	@Test
	public void testAnswerDeleteCascade() {
		Answer answer = createDomain();
		repository.save(answer);
		
		Comment comment = generator.generateTestComment();
		comment.setAnswerRef(answer.getId());
		answer.setComments(Arrays.asList(comment));
		commentRepository.save(comment);
		
		Reply reply = generator.generateTestReply();
		reply.setCommentRef(comment.getId());
		comment.setReplies(Arrays.asList(reply));
		replyRepository.save(reply);
		
		repository.save(answer);
		
		repository.delete(answer);
		Assert.assertNull(repository.findOne(answer.getId()));
		Assert.assertNull(commentRepository.findOne(comment.getId()));
		Assert.assertNull(replyRepository.findOne(reply.getId()));
	}

	private List<Comment> generateTestComments(TestEntityGenerator generator) {
		List<Comment> comments = CollectionUtil.arrayList();
		Comment comment = generator.generateTestComment();
		comments.add(comment);
		return comments;
	}
}

package com.phroogal.core.repository;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import junit.framework.AssertionFailedError;

import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.phroogal.core.domain.Answer;
import com.phroogal.core.domain.Comment;
import com.phroogal.core.domain.Question;
import com.phroogal.core.domain.Reply;
import com.phroogal.core.utility.CollectionUtil;

public class QuestionRepositoryTest extends BaseRepositoryTest<Question, ObjectId, QuestionRepository> {

	@Autowired
	private QuestionRepository repository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AnswerRepository answerRepository;
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private ReplyRepository replyRepository;
	
	private TestEntityGenerator generator = new TestEntityGenerator();
	
	@Before
	public void setUp() {
		super.setUp();
	}
	
	@Override
	protected QuestionRepository returnRepository() {
		return repository;
	}

	@Override
	protected Class<Question> returnDomainClass() {
		return Question.class;
	}

	@Override
	protected Question createDomain() {
		Question question = generator.generateTestQuestion();
		question.setAnswers(generateTestAnswers(generator));
		return question;
	}

	@Override
	protected void modifyDomain(Question question) {
		question.setTitle("This is a modified title");
		question.setContent("This is a modified content");
	}

	@Override
	protected boolean assertDomainEquals(Question domain, Question other) {
		try {
			Assert.assertEquals(domain.getId(), other.getId());
			Assert.assertEquals(domain.getTitle(), other.getTitle());
			Assert.assertEquals(domain.getContent(), other.getContent());
			return true;
		} catch (AssertionFailedError e) {
			return false;
		}
	}
	
	private List<Answer> generateTestAnswers(TestEntityGenerator generator) {
		List<Answer> answers = CollectionUtil.arrayList();
		Answer answer = generator.generateTestAnswer();
		answers.add(answer);
		return answers;
	}
	
	@Test
	public void testFindByDocId() {
		Question question = createDomain();
		repository.save(question);
		
		Question persistedDomain = repository.findByDocId(question.getDocId());
		assertNotNull(persistedDomain);
		assertDomainEquals(question, persistedDomain);
	}
	
	@Test
	public void testFindByPostById() {
		Question question = createDomain();
		ObjectId userId = question.getPostBy().getId();
		repository.save(question);
		
		Pageable pageRequest = new PageRequest(0, 10);
		List<Question> results = repository.findByPostById(userId, pageRequest).getContent();
		assertNotNull(results);
		assertTrue(results.size() == 1);
		assertDomainEquals(question, results.get(0));
	}
	
	@Test
	public void testFindByPostByIds() {
		List<ObjectId> userIds = CollectionUtil.arrayList();
		List<ObjectId> expectedResults = CollectionUtil.arrayList();
		Question question1 = createDomain();
		ObjectId userId1 = question1.getPostBy().getId();
		repository.save(question1);
		userIds.add(userId1);
		expectedResults.add(question1.getId());
		
		Question question2 = createDomain();
		ObjectId userId2 = question2.getPostBy().getId();
		repository.save(question2);
		userIds.add(userId2);
		expectedResults.add(question2.getId());
		
		Question question3 = createDomain();
		repository.save(question3);
		
		List<Question> results = repository.findByPostById(userIds, new PageRequest(0, 10)).getContent();
		assertNotNull(results);
		assertTrue(results.size() == 2);
		assertListIDsAreContainedInPersistedItems(expectedResults, results);
	}
	
	@Test
	public void testDocIdNotNull() {
		Question question = createDomain();
		repository.save(question);
		Assert.assertTrue(question.getDocId() != 0);
	}
	
	@Test
	public void testQuestionMetaDataUdated() {
		Question question = createDomain();
		question.setAnswers(null);
		repository.save(question);
		assertFalse(question.isAnswered());
		
		question.setAnswers(Arrays.asList(generator.generateTestAnswer()));
		question.getAnswers().get(0).setComments(Arrays.asList(generator.generateTestComment()));
		repository.save(question);
		assertTrue(question.isAnswered());
		assertTrue(question.getTotalAnswerCount() == 1);
		assertTrue(question.getTotalCommentCount() == 1);
	}
	
	@Test
	public void testQuestionDeleteCascade() {
		Question question = createDomain();
		ObjectId questionId = question.getId();
		
		Answer answer = generator.generateTestAnswer();
		answer.setQuestionRef(questionId);
		List<Answer> answers = Arrays.asList(answer);
		question.setAnswers(answers);
		answerRepository.save(answer);
		
		Comment comment = generator.generateTestComment();
		comment.setAnswerRef(answer.getId());
		comment.setRootQuestionId(questionId);
		answer.setComments(Arrays.asList(comment));
		commentRepository.save(comment);
		
		Reply reply = generator.generateTestReply();
		reply.setCommentRef(comment.getId());
		reply.setRootQuestionId(questionId);
		comment.setReplies(Arrays.asList(reply));
		replyRepository.save(reply);
		
		repository.save(question);
		
		repository.delete(question);
		Assert.assertNull(repository.findOne(questionId));
		Assert.assertNull(answerRepository.findOne(answer.getId()));
		Assert.assertNull(commentRepository.findOne(comment.getId()));
		Assert.assertNull(replyRepository.findOne(reply.getId()));
	}
}


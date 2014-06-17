package com.phroogal.core.repository;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import junit.framework.AssertionFailedError;

import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.phroogal.core.domain.Answer;
import com.phroogal.core.domain.Comment;
import com.phroogal.core.domain.Reply;
import com.phroogal.core.domain.User;
import com.phroogal.core.utility.CollectionUtil;

public class CommentRepositoryTest extends BaseRepositoryTest<Comment, ObjectId, CommentRepository> {

	@Autowired
	private CommentRepository repository;
	
	@Autowired
	private AnswerRepository answerRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	private Answer answerRef;
	
	private User userRef;
	
	@Before
	public void setUp() {
		super.setUp();
		persistAnswerReference();
		persistUserReference();
	}
	
	private void persistUserReference() {
		TestEntityGenerator generator = new TestEntityGenerator();
		userRef = generator.generateTestUser();
		userRepository.save(userRef);		
	}

	private void persistAnswerReference() {
		TestEntityGenerator generator = new TestEntityGenerator();
		answerRef = generator.generateTestAnswer();
		answerRepository.save(answerRef);
	}

	@Override
	protected CommentRepository returnRepository() {
		return repository;
	}

	@Override
	protected Class<Comment> returnDomainClass() {
		return Comment.class;
	}

	@Override
	protected Comment createDomain() {
		TestEntityGenerator generator = new TestEntityGenerator();
		Comment comment = generator.generateTestComment();
		comment.setAnswerRef(answerRef.getId());
		comment.setPostBy(userRef);
		comment.setReplies(generateTestReplies(generator));
		return comment;
	}

	@Override
	protected void modifyDomain(Comment comment) {
		comment.setContent("This is a modified content");
	}

	@Override
	protected boolean assertDomainEquals(Comment domain, Comment other) {
		try {
			Assert.assertEquals(domain.getId(), other.getId());
			Assert.assertEquals(domain.getAnswerRef(), other.getAnswerRef());
			Assert.assertEquals(domain.getContent(), other.getContent());
			return true;
		} catch (AssertionFailedError e) {
			return false;
		}
	}
	
	@Test
	public void testFindByAnswerRef() {
		Comment comment = createDomain();
		repository.save(comment);
		
		List<Comment> persistedComment = repository.findByAnswerRef(answerRef.getId());
		assertNotNull(persistedComment);
		assertNotNull(persistedComment.get(0));
		assertDomainEquals(comment, persistedComment.get(0));
	}

	private List<Reply> generateTestReplies(TestEntityGenerator generator) {
		List<Reply> replies = CollectionUtil.arrayList();
		Reply reply = generator.generateTestReply();
		replies.add(reply);
		return replies;
	}
}

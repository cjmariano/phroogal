package com.phroogal.core.repository;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import junit.framework.AssertionFailedError;

import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.phroogal.core.domain.Comment;
import com.phroogal.core.domain.Reply;
import com.phroogal.core.domain.User;

public class ReplyRepositoryTest extends BaseRepositoryTest<Reply, ObjectId, ReplyRepository> {

	@Autowired
	private ReplyRepository repository;
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	private Comment commentRef;
	
	private User userRef;
	
	@Before
	public void setUp() {
		super.setUp();
		persistCommentReference();
		persistUserReference();
	}
	
	private void persistUserReference() {
		TestEntityGenerator generator = new TestEntityGenerator();
		userRef = generator.generateTestUser();
		userRepository.save(userRef);		
	}

	private void persistCommentReference() {
		TestEntityGenerator generator = new TestEntityGenerator();
		commentRef = generator.generateTestComment();
		commentRepository.save(commentRef);
	}

	@Override
	protected ReplyRepository returnRepository() {
		return repository;
	}

	@Override
	protected Class<Reply> returnDomainClass() {
		return Reply.class;
	}

	@Override
	protected Reply createDomain() {
		TestEntityGenerator generator = new TestEntityGenerator();
		Reply reply = generator.generateTestReply();
		reply.setCommentRef(commentRef.getId());
		return reply;
	}

	@Override
	protected void modifyDomain(Reply reply) {
		reply.setContent("This is a modified content");
	}

	@Override
	protected boolean assertDomainEquals(Reply domain, Reply other) {
		try {
			Assert.assertEquals(domain.getId(), other.getId());
			Assert.assertEquals(domain.getCommentRef(), other.getCommentRef());
			Assert.assertEquals(domain.getContent(), other.getContent());
			return true;
		} catch (AssertionFailedError e) {
			return false;
		}
	}
	
	@Test
	public void testFindByCommentRef() {
		Reply reply = createDomain();
		repository.save(reply);
		
		List<Reply> persistedReply = repository.findByCommentRef(commentRef.getId());
		assertNotNull(persistedReply);
		assertNotNull(persistedReply.get(0));
		assertDomainEquals(reply, persistedReply.get(0));
	}
}

package com.phroogal.core.utility;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrServerException;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.phroogal.core.domain.Question;
import com.phroogal.core.domain.User;
import com.phroogal.core.repository.QuestionRepository;
import com.phroogal.core.repository.UserRepository;
import com.phroogal.core.repository.index.QuestionIndexRepository;
import com.phroogal.core.service.AnswerService;
import com.phroogal.core.service.CommentService;
import com.phroogal.core.service.QuestionService;
import com.phroogal.core.service.ReplyService;
import com.phroogal.core.service.TagService;
import com.phroogal.core.service.UserService;
import com.phroogal.core.service.UserSocialContactService;
import com.phroogal.core.service.UserTagService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/spring/app-context.xml"})
public class DatabasePatchTest {
	
	@Autowired
	private QuestionService  questionService;
	
	@Autowired
	private QuestionRepository  questionRepository;
	
	@Autowired
	private QuestionIndexRepository  questionIndexRepository;
	
	@Autowired
	private AnswerService  answerService;
	
	@Autowired
	private CommentService  commentService;
	
	@Autowired
	private ReplyService  replyService;
	
	@Autowired
	private TagService  tagService;
	
	@Autowired
	private UserTagService  userTagService;
	
	@Autowired
	private UserSocialContactService userSocialContactService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserService userService;
	
	@Test
	@Ignore
	public void testPatch() throws SolrServerException, IOException {
		System.out.println("====================================");
		System.out.println("Refreshing metadata");
		System.out.println("====================================");
		updateQuestion();
	}
	

	private void updateQuestion() {
		EntityIteratorHelper<Question, QuestionRepository, QuestionService> iterator = new EntityIteratorHelper<Question, QuestionRepository, QuestionService>(questionRepository, questionService) {
			@Override
			protected boolean processEach(Question question) {
				if (question.getPostBy() == null) {
					if (question.getPostById() != null) {
						User user = userService.findById(question.getPostById());
						question.setPostBy(user);
						questionService.saveOrUpdate(question);
						return true;
					}
				}
				return false;
			}
		};
		iterator.startAndContinueOnErrors();
	}
}

package com.phroogal.core.utility;


import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.phroogal.core.domain.Question;
import com.phroogal.core.repository.TestEntityGenerator;
import com.phroogal.core.repository.index.QuestionIndexRepository;
import com.phroogal.core.search.index.QuestionIndex;
import com.phroogal.core.service.QuestionService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/spring/app-context.xml"})
public class SolrUtilityTest {
	
	@Autowired
	private QuestionService questionService;
	
	@Autowired
	private QuestionIndexRepository questionIndexRepo;
	
	@Autowired
	@Qualifier("solrUtility")
	private SolrUtility solrUtility;
	
	@Before
	public void setup() {
		List<Question> questions = questionService.findAll();
		questionService.delete(questions);
		for (QuestionIndex idx : questionIndexRepo.findAll()) {
			questionIndexRepo.delete(idx);
		}
		
	}
	
	@Test
	@Ignore
	public void testDeleteAllIndex() throws Exception {
		String testKeyword = setupTestQuestionAndReturnKeyword();
		List<QuestionIndex> questionIndex = questionService.searchIndexedQuestionByTitle(testKeyword, 0, 100).getContent();
		Assert.assertTrue(questionIndex != null && questionIndex.size() > 0);
		
		solrUtility.deleteAllIndex();
		questionIndex = questionService.searchIndexedQuestionByTitle(testKeyword, 0, 100).getContent();
		Assert.assertTrue(questionIndex != null && questionIndex.size() == 0);
	}

	@Test
	@Ignore
	public void testResetQuestionIndex() throws Exception {
		String testKeyword = setupTestQuestionAndReturnKeyword();
		List<QuestionIndex> questionIndex = questionService.searchIndexedQuestionByTitle(testKeyword, 0, 100).getContent();
		Assert.assertTrue(questionIndex != null && questionIndex.size() > 0);
		
		solrUtility.resetQuestionIndex();
		questionIndex = questionService.searchIndexedQuestionByTitle(testKeyword, 0, 100).getContent();
		Assert.assertTrue(questionIndex != null && questionIndex.size() > 0);
	}
	
	private String setupTestQuestionAndReturnKeyword() {
		String testKeyword = "test";
		TestEntityGenerator generator = new TestEntityGenerator();
		Question question = generator.generateTestQuestion();
		question.setTitle(testKeyword);
		questionService.saveOrUpdate(question);
		return testKeyword;
	}
}

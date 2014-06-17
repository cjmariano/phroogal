package com.phroogal.core.service.impl;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.solr.client.solrj.SolrServerException;
import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.phroogal.core.domain.Answer;
import com.phroogal.core.domain.Question;
import com.phroogal.core.domain.UserSocialContact;
import com.phroogal.core.repository.QuestionRepository;
import com.phroogal.core.repository.TestEntityGenerator;
import com.phroogal.core.repository.index.QuestionIndexRepository;
import com.phroogal.core.search.index.QuestionIndex;
import com.phroogal.core.service.AnswerService;
import com.phroogal.core.service.CommentService;
import com.phroogal.core.service.FlagNotificationService;
import com.phroogal.core.service.QuestionService;
import com.phroogal.core.service.Service;
import com.phroogal.core.service.UserSocialContactService;
import com.phroogal.core.test.helper.RepositoryCleanupHelper;
import com.phroogal.core.test.helper.UnwrapProxyHelper;
import com.phroogal.core.utility.CollectionUtil;
import com.phroogal.core.utility.SolrUtility;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/spring/app-context.xml"})
public class QuestionServiceImplTest extends BaseServiceTest<Question, ObjectId, QuestionRepository> {

	@Autowired
	private QuestionService serviceImpl;
	
	@Autowired
	private QuestionIndexRepository questionIndexRepository;
	
	private QuestionIndexRepository mockedIndexRepository = Mockito.mock(QuestionIndexRepository.class);
	
	private UserSocialContactService userSocialContactService = Mockito.mock(UserSocialContactService.class);
	
	private QuestionIndex testQuestionIndex;
	
	private Question testQuestion;
	
	private List<Answer> listOfAnswers;
	
	private TestEntityGenerator generator;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	@Qualifier("solrUtility")
	private SolrUtility solrUtility;
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		serviceImpl = (QuestionService) returnServiceImpl();
		generator = new TestEntityGenerator();
		testQuestionIndex = generator.generateTestQuestion().getIndex();
		testQuestion = generator.generateTestQuestion();
		
		List<QuestionIndex> listOfQuestionIndex = CollectionUtil.arrayList();
		listOfQuestionIndex.add(testQuestionIndex);
		listOfAnswers = CollectionUtil.arrayList();
		Pageable pageRequest = new PageRequest(0, 10);
		Page<QuestionIndex> pageResult = new PageImpl<QuestionIndex>(listOfQuestionIndex);
		
		AnswerService mockedAnswerService = Mockito.mock(AnswerService.class);
		CommentService commentService = Mockito.mock(CommentService.class);
		ReflectionTestUtils.setField(serviceImpl, "questionIndexRepository", mockedIndexRepository);
		
		when(serviceImpl.findById((ObjectId) Mockito.any())).thenReturn(testQuestion);
		when(mockedIndexRepository.searchByTitle("keyword", pageRequest)).thenReturn(pageResult);
		when(mockedIndexRepository.searchByTags("tag", new PageRequest(0, 100))).thenReturn(pageResult);
		when(mockedAnswerService.getByQuestionRef((ObjectId) Mockito.any())).thenReturn(listOfAnswers);
		when(commentService.getByAnswerRef((ObjectId) Mockito.any())).thenReturn(Arrays.asList(generator.generateTestComment()));
	}
	
	@After
	public void tearDown() {
		RepositoryCleanupHelper.dropCollection(mongoTemplate);
		RepositoryCleanupHelper.deleteAllSolrData(solrUtility);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected Service<Question, ObjectId> returnServiceImpl() {
		return (Service<Question, ObjectId>) UnwrapProxyHelper.unwrapProxy(QuestionServiceImpl.class, serviceImpl);
	}
	
	@Override
	protected QuestionRepository returnMongoRepository() {
		return Mockito.mock(QuestionRepository.class);
	}

	@Override
	protected Question returnDomainInstance() {
		TestEntityGenerator generator = new TestEntityGenerator(); 
		return generator.generateTestQuestion();
	}
	
	@Test
	public void testGetByDocId() {
		QuestionRepository mockedRepository =  Mockito.mock(QuestionRepository.class);
		ReflectionTestUtils.setField(serviceImpl, "questionRepository", mockedRepository);
		when(mockedRepository.findByDocId(1000L)).thenReturn(testQuestion);
		serviceImpl.getByDocId(1000L);
		verify(mockedRepository, atLeastOnce()).findByDocId(1000L);
	}
	
	@Test
	public void testGetRecentQuestions() {
		serviceImpl.getRecentQuestions(0, 10);
		verify(mockedIndexRepository, atLeastOnce()).findByCreatedOnBetween(Mockito.any(DateTime.class), Mockito.any(DateTime.class), Mockito.any(PageRequest.class));
	}
	
	@Test
	public void testGetByUserId() {
		QuestionRepository mockedRepository =  Mockito.mock(QuestionRepository.class);
		ReflectionTestUtils.setField(serviceImpl, "questionRepository", mockedRepository);
		ObjectId userId = ObjectId.get();
		
		Pageable pageRequest = new PageRequest(0, 10);
		serviceImpl.getByUserId(userId, 0, 10);
		verify(mockedRepository, atLeastOnce()).findByPostById(userId, pageRequest);
	}
	
	@Test
	public void testGetUnansweredQuestions() {
		serviceImpl.getUnansweredQuestions(0, 10);
		verify(mockedIndexRepository, atLeastOnce()).findByIsAnswered(Mockito.eq(new Boolean(false)), Mockito.any(PageRequest.class));
	}
	
	@Test
	public void testGetRecentUnansweredQuestions() {
		serviceImpl.getRecentUnansweredQuestions(0, 10);
		Sort sort = new Sort(Sort.Direction.DESC, "createdOn");
		Pageable pageRequest =new PageRequest(0, 10, sort);
		verify(mockedIndexRepository, atLeastOnce()).findByIsAnswered(Mockito.eq(new Boolean(false)), Mockito.eq(pageRequest));
	}
	
	@Test
	public void testGetSocialQuestions() {
		ObjectId userId = ObjectId.get();
		QuestionRepository mockedRepository =  Mockito.mock(QuestionRepository.class);
		ReflectionTestUtils.setField(serviceImpl, "questionRepository", mockedRepository);
		ReflectionTestUtils.setField(serviceImpl, "userSocialContactService", userSocialContactService);
		UserSocialContact userSocialContact = generator.generateTestUserSocialContact();
		when(userSocialContactService.getByUserId(userId)).thenReturn(userSocialContact);
		
		serviceImpl.getSocialQuestions(userId, 0, 10);
		Pageable pageRequest =new PageRequest(0, 10);
		verify(mockedRepository, atLeastOnce()).findByPostById(userSocialContact.getSocialContactIds(), pageRequest);
	}
	
	@Test
	public void testGetQuestionsByDateRange() {
		DateTime from = DateTime.now().minusDays(5);
		DateTime to = DateTime.now();
		serviceImpl.getQuestionsByDateRange(from, to, 0, 10);
		Sort sort = new Sort(Sort.Direction.DESC, "createdOn");
		Pageable pageRequest =new PageRequest(0, 10, sort);
		verify(mockedIndexRepository, atLeastOnce()).findByCreatedOnBetween(Mockito.eq(from), Mockito.eq(to), Mockito.eq(pageRequest));
	}
	
	@Test
	public void testSearchIndexedQuestionByTitle() {
		Pageable pageRequest = new PageRequest(0, 10);
		serviceImpl.searchIndexedQuestionByTitle("keyword", 0, 10);
		verify(mockedIndexRepository, atLeastOnce()).searchByTitle("keyword", pageRequest);
	}
	
	@Test
	public void testSearchSimilarIndexedQuestion() {
		Pageable pageRequest = new PageRequest(0, 5, new Sort(Sort.Direction.DESC, "rank"));		
		serviceImpl.searchSimilarIndexedQuestion("keyword",0, 5);
		verify(mockedIndexRepository, atLeastOnce()).searchBySimilarTitle("keyword",pageRequest);
	}
	
	@Test
	public void testSearchSimilarIndexedQuestionAndNotDocId() {
		Pageable pageRequest = new PageRequest(0, 5, new Sort(Sort.Direction.DESC, "rank"));		
		serviceImpl.searchSimilarIndexedQuestion("keyword",1000l, 0, 5);
		verify(mockedIndexRepository, atLeastOnce()).searchBySimilarTitleAndNotDocId("keyword", 1000l, pageRequest);
	}
	
	@Test
	public void testFlagQuestion() {
		FlagNotificationService flagNotificationService =  Mockito.mock(FlagNotificationService.class);
		ReflectionTestUtils.setField(serviceImpl, "flagNotificationService", flagNotificationService);
		Question flaggedQuestion = generator.generateTestQuestion();
		String content = flaggedQuestion.getTitle().concat(flaggedQuestion.getContent());
		serviceImpl.flagQuestion(flaggedQuestion);
		verify(flagNotificationService, atLeastOnce()).createRequest(flaggedQuestion, content);
	}
	
	@Test
	public void testSearchQuestion() {
		Answer answer1 = generator.generateAnswerWithVotesTotalOf(100);
		listOfAnswers.add(answer1);
		
		Answer answer2 = generator.generateAnswerWithVotesTotalOf(200);
		listOfAnswers.add(answer2);
		
		Answer answer3 = generator.generateAnswerWithVotesTotalOf(300);
		listOfAnswers.add(answer3);
		testQuestion.setAnswers(listOfAnswers);
		
		QuestionIndex result = serviceImpl.searchQuestion("keyword", 0, 10).getContent().get(0);
		
		Assert.assertEquals(result.getId(), testQuestionIndex.getId());
		Assert.assertEquals(result.getTitle(), testQuestionIndex.getTitle());
		Assert.assertEquals(result.getContent(), testQuestionIndex.getContent());
		Assert.assertEquals(result.getTags().size(), testQuestionIndex.getTags().size());
	}
	
	@Test
	public void testSearchQuestionPreviewByTag() {
		Answer answer1 = generator.generateAnswerWithVotesTotalOf(100);
		listOfAnswers.add(answer1);
		
		Answer answer2 = generator.generateAnswerWithVotesTotalOf(200);
		listOfAnswers.add(answer2);
		
		Answer answer3 = generator.generateAnswerWithVotesTotalOf(300);
		listOfAnswers.add(answer3);
		testQuestion.setAnswers(listOfAnswers);
		
		QuestionIndex result = serviceImpl.searchQuestionByTag("tag", 0, 100).getContent().get(0);
		
		Assert.assertEquals(result.getId(), testQuestionIndex.getId());
		Assert.assertEquals(result.getTitle(), testQuestionIndex.getTitle());
		Assert.assertEquals(result.getContent(), testQuestionIndex.getContent());
		Assert.assertEquals(result.getTags().size(), testQuestionIndex.getTags().size());
	}
	
	@Test
	public void testReIndex() throws SolrServerException, IOException {
		SolrUtility solrUtility = Mockito.mock(SolrUtility.class);
		ReflectionTestUtils.setField(serviceImpl, "solrUtility", solrUtility);
		serviceImpl.reIndex();
		verify(solrUtility, atLeastOnce()).resetQuestionIndex();
	}
	
	@Test
	public void testIncrementTotalViewsCount() {
		testQuestion = generator.generateTestQuestion();
		QuestionRepository mockedRepository =  Mockito.mock(QuestionRepository.class);
		ReflectionTestUtils.setField(serviceImpl, "questionRepository", mockedRepository);
		when(mockedRepository.findByDocId(testQuestion.getDocId())).thenReturn(testQuestion);
		
		testQuestion.setTotalViewCount(100L);
		serviceImpl.incrementTotalViewsCount(testQuestion.getDocId());
		Assert.assertTrue(testQuestion.getTotalViewCount() == 101L);
		
		serviceImpl.incrementTotalViewsCount(testQuestion.getDocId());
		Assert.assertTrue(testQuestion.getTotalViewCount() == 102L);
	}
	
	@Test
	public void testGetTrendingQuestions() {
		List<QuestionIndex> listOfQuestionIndex = CollectionUtil.arrayList();
		QuestionIndex testQuestion1 = generator.generateTestQuestionIndex();
		testQuestion1.setId(ObjectId.get().toString());
		testQuestion1.setTotalAnswerCount(5);
		testQuestion1.setTotalViewCount(5);
		testQuestion1.setRank(10);
		testQuestion1.setAnswered(false);
		listOfQuestionIndex.add(testQuestion1);
		
		QuestionIndex testQuestion2 = generator.generateTestQuestionIndex();
		testQuestion2.setId(ObjectId.get().toString());
		testQuestion2.setTotalAnswerCount(10);
		testQuestion2.setTotalViewCount(10);
		testQuestion2.setRank(20);
		testQuestion2.setAnswered(true);
		listOfQuestionIndex.add(testQuestion2);
		
		QuestionIndex testQuestion3 = generator.generateTestQuestionIndex();
		testQuestion3.setId(ObjectId.get().toString());
		testQuestion3.setTotalAnswerCount(15);
		testQuestion3.setTotalViewCount(15);
		testQuestion3.setRank(30);
		testQuestion3.setAnswered(true);
		listOfQuestionIndex.add(testQuestion3);
		
		questionIndexRepository.save(listOfQuestionIndex);
		ReflectionTestUtils.setField(serviceImpl, "questionIndexRepository", questionIndexRepository);
		List<QuestionIndex> results = serviceImpl.getTrendingQuestions(0, 5).getContent();
		
		Assert.assertTrue(results.size() ==  2);
		Assert.assertTrue(results.get(0).getId().equals(testQuestion3.getId()));
		Assert.assertTrue(results.get(1).getId().equals(testQuestion2.getId()));
	}
	
	@Test
	public void testGetMostViewedQuestions() {
		List<QuestionIndex> listOfQuestionIndex = CollectionUtil.arrayList();
		QuestionIndex testQuestion1 = generator.generateTestQuestionIndex();
		testQuestion1.setId(ObjectId.get().toString());
		testQuestion1.setTotalViewCount(25);
		testQuestion1.setCreatedOn(DateTime.now());
		listOfQuestionIndex.add(testQuestion1);
		
		QuestionIndex testQuestion2 = generator.generateTestQuestionIndex();
		testQuestion2.setId(ObjectId.get().toString());
		testQuestion2.setTotalViewCount(50);
		testQuestion2.setCreatedOn(DateTime.now().minusDays(100));
		listOfQuestionIndex.add(testQuestion2);
		
		QuestionIndex testQuestion3 = generator.generateTestQuestionIndex();
		testQuestion3.setId(ObjectId.get().toString());
		testQuestion3.setTotalViewCount(100);
		testQuestion3.setCreatedOn(DateTime.now().minusDays(1));
		listOfQuestionIndex.add(testQuestion3);
		
		questionIndexRepository.save(listOfQuestionIndex);
		ReflectionTestUtils.setField(serviceImpl, "questionIndexRepository", questionIndexRepository);
		List<QuestionIndex> results = serviceImpl.getMostViewedQuestions(0, 10, 5).getContent();
		
		Assert.assertTrue(results.size() ==  2);
		Assert.assertTrue(results.get(0).getId().equals(testQuestion3.getId()));
		Assert.assertTrue(results.get(1).getId().equals(testQuestion1.getId()));
	}
	
}

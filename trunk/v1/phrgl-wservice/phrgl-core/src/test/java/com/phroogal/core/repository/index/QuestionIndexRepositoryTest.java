package com.phroogal.core.repository.index;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import junit.framework.AssertionFailedError;

import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.phroogal.core.repository.BaseRepositoryTest;
import com.phroogal.core.repository.TestEntityGenerator;
import com.phroogal.core.search.index.QuestionIndex;
import com.phroogal.core.utility.CollectionUtil;

public class QuestionIndexRepositoryTest extends BaseRepositoryTest<QuestionIndex, String, QuestionIndexRepository> {

	@Autowired
	private QuestionIndexRepository repository;
	
	@Override
	protected QuestionIndexRepository returnRepository() {
		return repository;
	}

	@Override
	protected Class<QuestionIndex> returnDomainClass() {
		return QuestionIndex.class;
	}

	@Override
	protected QuestionIndex createDomain() {
		TestEntityGenerator generator = new TestEntityGenerator();
		return generator.generateTestQuestionIndex();
	}

	@Override
	protected void modifyDomain(QuestionIndex questionIndex) {
		questionIndex.setTitle("This is a modified title");
	}

	@Override
	protected boolean assertDomainEquals(QuestionIndex domain, QuestionIndex other) {
		try {
			Assert.assertEquals(domain.getId(), other.getId());
			Assert.assertEquals(domain.getTitle(), other.getTitle());
			return true;
		} catch (AssertionFailedError e) {
			return false;
		}
	}
	
	@Test
	public void testFindAllByIds() {
		List<QuestionIndex> expectedResultsCase1 = CollectionUtil.arrayList();
		List<QuestionIndex> expectedResultsCase2 = CollectionUtil.arrayList();
		QuestionIndex idx1 = createAndPersistQuestion("What is Phroogal?", 1000L, expectedResultsCase1); 
		QuestionIndex idx2 = createAndPersistQuestion("How do I join Phroogal?", 1001L, expectedResultsCase1);
		QuestionIndex idx3 = createAndPersistQuestion("What is a Phroogie?", 1002L, expectedResultsCase1);
		createAndPersistQuestion("What is a credit union?", 1003L, expectedResultsCase2);
		
		List<QuestionIndex> results = repository.findAll(Arrays.asList(idx1.getId(),idx2.getId(),idx3.getId()), new PageRequest(0, 10)).getContent();
		List<String> resultIds = createResultsIdsList(results);
		Assert.assertEquals(3, results.size());
		assertListIDsAreContainedInPersistedItems(resultIds, expectedResultsCase1);
	}
	
	@Test
	public void testFindByTitle() {
		Pageable pageRequest = new PageRequest(0, 10);
		List<QuestionIndex> expectedResultsCase1 = CollectionUtil.arrayList();
		List<QuestionIndex> expectedResultsCase2 = CollectionUtil.arrayList();
		List<QuestionIndex> expectedResultsCase3 = CollectionUtil.arrayList();
		createAndPersistQuestion("What is Phroogal?", 1000L, expectedResultsCase1); 
		createAndPersistQuestion("How do I join Phroogal?", 1001L, expectedResultsCase1);
		createAndPersistQuestion("What is a Phroogie?", 1002L, expectedResultsCase1);
		createAndPersistQuestion("What is a credit union?", 1003L, expectedResultsCase2);
		createAndPersistQuestion("How can I improve my credit card score?", 1004L, expectedResultsCase3);
		
		List<QuestionIndex> results = repository.searchByTitle("phro", pageRequest).getContent();
		List<String> resultIds = createResultsIdsList(results);
		Assert.assertEquals(3, results.size());
		assertListIDsAreContainedInPersistedItems(resultIds, expectedResultsCase1);
		
		results = repository.searchByTitle("credit", pageRequest).getContent();
		resultIds = createResultsIdsList(results);
		Assert.assertEquals(2, results.size());
		assertListIDsAreContainedInPersistedItems(resultIds, expectedResultsCase2);
		assertListIDsAreContainedInPersistedItems(resultIds, expectedResultsCase3);
		
		results = repository.searchByTitle("credit card", pageRequest).getContent();
		resultIds = createResultsIdsList(results);
		Assert.assertEquals(1, results.size());
		assertListIDsAreContainedInPersistedItems(resultIds, expectedResultsCase3);
		
	}
	
	@Test
	@Ignore //TODO: How shoud solr handle this?
	public void testFindByTitleWithSpecialCharacters() {
		Pageable pageRequest = new PageRequest(0, 10);
		List<QuestionIndex> expectedResultsCase1 = CollectionUtil.arrayList();
		List<QuestionIndex> expectedResultsCase2 = CollectionUtil.arrayList();
		createAndPersistQuestion("I can't pay my mortgage", 1000L, expectedResultsCase1);
		createAndPersistQuestion("I can't pay my credit card", 1001L, expectedResultsCase2);
		
		List<QuestionIndex> results = repository.searchByTitle("I can't pay my credit card", pageRequest).getContent();
		List<String> resultIds = createResultsIdsList(results);
		Assert.assertEquals(1, results.size());
		assertListIDsAreContainedInPersistedItems(resultIds, expectedResultsCase2);
	}
	
	@Test
	public void testFindBySimilarTitle() {
		Pageable pageRequest = new PageRequest(0, 5);
		
		List<QuestionIndex> expectedResultsCase1 = CollectionUtil.arrayList();
		List<QuestionIndex> expectedResultsCase2 = CollectionUtil.arrayList();
		List<QuestionIndex> expectedResultsCase3 = CollectionUtil.arrayList();
		List<QuestionIndex> expectedResultsCase4 = CollectionUtil.arrayList();
		
		createAndPersistQuestion("What is Phroogal?", 1000L, expectedResultsCase1); 
		createAndPersistQuestion("How do I join Phroogal?", 1001L, expectedResultsCase1);
		createAndPersistQuestion("What is a Phroogie?", 1002L, expectedResultsCase1);
		createAndPersistQuestion("What is a credit union?", 1003L, expectedResultsCase2);
		createAndPersistQuestion("How can I improve my credit card score?", 1004L, expectedResultsCase3);
		
		createAndPersistQuestion("financial tool kit", 1005L, expectedResultsCase4);
		createAndPersistQuestion("synthetic refunding a financial tool", 1006L, expectedResultsCase4);
		createAndPersistQuestion("financial ranking financial tool", 1007L, expectedResultsCase4);
		createAndPersistQuestion("financial tool programs", 1008L, expectedResultsCase4);
		createAndPersistQuestion("Any financial websites?", 1009L, expectedResultsCase4);
		
		List<QuestionIndex> results = repository.searchBySimilarTitle("phro",pageRequest).getContent();
		List<String> resultIds = createResultsIdsList(results);
		Assert.assertEquals(3, results.size());
		assertListIDsAreContainedInPersistedItems(resultIds, expectedResultsCase1);
		
		results = repository.searchBySimilarTitle("credit",pageRequest).getContent();
		resultIds = createResultsIdsList(results);
		Assert.assertEquals(2, results.size());
		assertListIDsAreContainedInPersistedItems(resultIds, expectedResultsCase2);
		assertListIDsAreContainedInPersistedItems(resultIds, expectedResultsCase3);
		
		results = repository.searchBySimilarTitle("credit card",pageRequest).getContent();
		resultIds = createResultsIdsList(results);
		Assert.assertEquals(2, results.size());
		assertListIDsAreContainedInPersistedItems(resultIds, expectedResultsCase3);
		
		results = repository.searchBySimilarTitle("financial tool",pageRequest).getContent();
		resultIds = createResultsIdsList(results);
		Assert.assertEquals(5, results.size());
		assertListIDsAreContainedInPersistedItems(resultIds, expectedResultsCase4);
	}
	
	@Test
	public void testFindBySimilarTitleAndNotDocId() {
		Pageable pageRequest = new PageRequest(0, 5);
		
		List<QuestionIndex> expectedResultsCase1 = CollectionUtil.arrayList();
		List<QuestionIndex> expectedResultsCase2 = CollectionUtil.arrayList();
		
		createAndPersistQuestion("What is Phroogal?", 1000L, expectedResultsCase1); 
		createAndPersistQuestion("How do I join Phroogal?", 1001L, expectedResultsCase1);
		createAndPersistQuestion("What is a Phroogie?", 1002L, expectedResultsCase2);
		createAndPersistQuestion("What is a credit union?", 1003L, expectedResultsCase2);
		
		List<QuestionIndex> results = repository.searchBySimilarTitleAndNotDocId("phro", 1002L, pageRequest).getContent();
		List<String> resultIds = createResultsIdsList(results);
		Assert.assertEquals(2, results.size());
		assertListIDsAreContainedInPersistedItems(resultIds, expectedResultsCase1);
	}
	
	@Test
	public void testFindPluralStemmingTitles() {
		Pageable pageRequest = new PageRequest(0, 10);
		List<QuestionIndex> expectedResultsCase1 = CollectionUtil.arrayList();
		List<QuestionIndex> expectedResultsCase2 = CollectionUtil.arrayList();
		
		createAndPersistQuestion("What are international accounts?", 1000L, expectedResultsCase1); 
		createAndPersistQuestion("Are there support for account internationalization?", 1001L, expectedResultsCase1);
		createAndPersistQuestion("Should credit union be good for internationals", 1002L, expectedResultsCase1);
		createAndPersistQuestion("What is a credit union?", 1003L, expectedResultsCase2);
		
		List<QuestionIndex> results = repository.searchByTitle("international", pageRequest).getContent();
		List<String> resultIds = createResultsIdsList(results);
		Assert.assertEquals(3, results.size());
		assertListIDsAreContainedInPersistedItems(resultIds, expectedResultsCase1);
	}
	
	@Test
	public void testSearchByTagsFound() {
		TestEntityGenerator generator = new TestEntityGenerator();
		String tagName = "Accounting";
		QuestionIndex questionIdx = generator.generateTestQuestionIndex();
		questionIdx.setTags(generateTagsData());
		repository.save(questionIdx);
		List<QuestionIndex> results = repository.searchByTags(tagName, new PageRequest(0, 100)).getContent();
		Assert.assertTrue(results != null && results.size() == 1);
		Assert.assertTrue(results.get(0).getTags() != null);
		Assert.assertTrue(results.get(0).getTags().contains(tagName));
	}
	
	@Test
	public void testSearchByMultipleTagsFound() {
		TestEntityGenerator generator = new TestEntityGenerator();
		List<String> tags = Arrays.asList("Accounting", "Investment");
		QuestionIndex questionIdx = generator.generateTestQuestionIndex();
		questionIdx.setTags(generateTagsData());
		repository.save(questionIdx);
		List<QuestionIndex> results = repository.searchByTags(tags, new PageRequest(0, 100)).getContent();
		Assert.assertTrue(results != null && results.size() == 1);
		Assert.assertTrue(results.get(0).getTags().containsAll(tags));
	}
	
	@Test
	public void testSearchByTagsFullMatchOnly() {
		TestEntityGenerator generator = new TestEntityGenerator();
		List<String> tags = generateTagsData();
		String tagName = tags.get(0) + " Etc.";
		QuestionIndex questionIdx = generator.generateTestQuestionIndex();
		questionIdx.setTags(tags);
		repository.save(questionIdx);
		List<QuestionIndex> results = repository.searchByTags(tagName, new PageRequest(0, 100)).getContent();
		Assert.assertTrue(results.size() == 0);
	}
	
	@Test
	public void testSearchByTagsNotFound() {
		TestEntityGenerator generator = new TestEntityGenerator();
		String tagName = "TagNotMatched";
		QuestionIndex question = generator.generateTestQuestionIndex();
		question.setTags(generateTagsData());
		repository.save(question);
		List<QuestionIndex> result = repository.searchByTags(tagName, new PageRequest(0, 100)).getContent();
		Assert.assertTrue(result.size() == 0);
	}
	
	@Test
	public void testPaginationFindByTitle() {
		List<QuestionIndex> expectedResultsCase1 = CollectionUtil.arrayList();
		List<QuestionIndex> expectedResultsCase2 = CollectionUtil.arrayList();
		createAndPersistQuestion("Page1, Question1", 1000L, expectedResultsCase1); 
		createAndPersistQuestion("Page1, Question2", 1001L, expectedResultsCase1);
		createAndPersistQuestion("Page1, Question3", 1002L, expectedResultsCase1);
		
		createAndPersistQuestion("Page2, Question1", 1003L, expectedResultsCase2);
		createAndPersistQuestion("Page2, Question2", 1004L, expectedResultsCase2);
		createAndPersistQuestion("Page2, Question3", 1005L, expectedResultsCase2);

		Pageable pageRequest = new PageRequest(0, 3);
		List<QuestionIndex> results = repository.searchByTitle("Question", pageRequest).getContent();
		List<String> resultIds = createResultsIdsList(results);
		Assert.assertEquals(3, results.size());
		assertListIDsAreContainedInPersistedItems(resultIds, expectedResultsCase1);
		
		pageRequest = new PageRequest(1, 3);
		results = repository.searchByTitle("Question", pageRequest).getContent();
		resultIds = createResultsIdsList(results);
		Assert.assertEquals(3, results.size());
		assertListIDsAreContainedInPersistedItems(resultIds, expectedResultsCase2);
	}
	
	@Test
	public void testFindAllSortByCreatedOn() {
		QuestionIndex question1 = createDomain();
		question1.setCreatedOn(DateTime.now().minusDays(30));
		repository.save(question1);
		
		QuestionIndex question2 = createDomain();
		question2.setCreatedOn(DateTime.now().minusDays(20));
		repository.save(question2);
		
		QuestionIndex question3 = createDomain();
		question3.setCreatedOn(DateTime.now().minusDays(10));
		repository.save(question3);
		
		QuestionIndex question4 = createDomain();
		question4.setCreatedOn(DateTime.now());
		repository.save(question4);
		
		Page<QuestionIndex> questionPages =  repository.findAll(new PageRequest(0,10,new Sort(Sort.Direction.DESC, "createdOn")));
		List<QuestionIndex> questions = questionPages.getContent();
		
		assertEquals(questions.get(0).getId(), question4.getId());
		assertEquals(questions.get(1).getId(), question3.getId());
		assertEquals(questions.get(2).getId(), question2.getId());
		assertEquals(questions.get(3).getId(), question1.getId());
	}
	
	@Test
	public void testFindUnansweredQuestionsAndSortByCreatedOn() {
		List<ObjectId> unansweredQuestionsId = CollectionUtil.arrayList();
		QuestionIndex question1 = createDomain();
		question1.setCreatedOn(DateTime.now().minusDays(30));
		unansweredQuestionsId.add(new ObjectId(question1.getId()));
		repository.save(question1);
		
		QuestionIndex question2 = createDomain();
		question2.setCreatedOn(DateTime.now().minusDays(20));
		question2.setAnswered(true);
		repository.save(question2);
		
		QuestionIndex question3 = createDomain();
		question3.setCreatedOn(DateTime.now().minusDays(10));
		question3.setAnswered(true);
		repository.save(question3);
		
		QuestionIndex question4 = createDomain();
		question4.setCreatedOn(DateTime.now());
		unansweredQuestionsId.add(new ObjectId(question4.getId()));
		repository.save(question4);
		
		List<QuestionIndex> unansweredQuestions = repository.findByIsAnswered(false, new PageRequest(0, 10)).getContent();
		
		assertTrue(unansweredQuestions.size() == 2);
		
		unansweredQuestions = repository.findByIsAnswered(false, new PageRequest(0, 10, new Sort(Sort.Direction.DESC, "createdOn"))).getContent();
		assertEquals(unansweredQuestions.get(0).getId(), question4.getId());
		assertEquals(unansweredQuestions.get(1).getId(), question1.getId());
	}
	
	@Test
	public void testFindByCreatedOnDateRanges() {
		List<ObjectId> unansweredQuestionsId = CollectionUtil.arrayList();
		QuestionIndex question1 = createDomain();
		question1.setCreatedOn(DateTime.now().minusDays(30));
		unansweredQuestionsId.add(new ObjectId(question1.getId()));
		repository.save(question1);
		
		QuestionIndex question2 = createDomain();
		question2.setCreatedOn(DateTime.now().minusDays(20));
		question2.setAnswered(true);
		repository.save(question2);
		
		QuestionIndex question3 = createDomain();
		question3.setCreatedOn(DateTime.now().minusDays(10));
		question3.setAnswered(true);
		repository.save(question3);
		
		QuestionIndex question4 = createDomain();
		question4.setCreatedOn(DateTime.now());
		unansweredQuestionsId.add(new ObjectId(question4.getId()));
		repository.save(question4);
		
		List<QuestionIndex> unansweredQuestions = repository.findByCreatedOnBetween(DateTime.now().minusDays(11), DateTime.now(), new PageRequest(0, 10, new Sort(Sort.Direction.DESC, "createdOn"))).getContent();
		assertEquals(unansweredQuestions.get(0).getId(), question4.getId());
		assertEquals(unansweredQuestions.get(1).getId(), question3.getId());
	}
	
	@Test
	public void testFindByCreatedOnBetweenAndIsAnswered() {
		QuestionIndex question1 = createDomain();
		question1.setCreatedOn(DateTime.now().minusDays(30));
		question1.setAnswered(false);
		repository.save(question1);
		
		QuestionIndex question2 = createDomain();
		question2.setCreatedOn(DateTime.now().minusDays(10));
		question2.setAnswered(false);
		repository.save(question2);
		
		QuestionIndex question3 = createDomain();
		question3.setCreatedOn(DateTime.now().minusDays(10));
		question3.setAnswered(true);
		repository.save(question3);
		
		PageRequest pageable = new PageRequest(0, 10, new Sort(Sort.Direction.DESC, "createdOn"));
		List<QuestionIndex> results = repository.findByCreatedOnBetweenAndIsAnswered(DateTime.now().minusDays(11), DateTime.now(), true, pageable).getContent();
		assertTrue(results.size() == 1);
		assertEquals(results.get(0).getId(), question3.getId());
	}
	
	@Test
	public void testFindByCreatedOnBetweenAndIsAnsweredAndNotDocId() {
		long excludeDocId = 1000L;
		QuestionIndex question1 = createDomain();
		question1.setCreatedOn(DateTime.now().minusDays(30));
		question1.setAnswered(false);
		question1.setDocId(1L);
		repository.save(question1);
		
		QuestionIndex question2 = createDomain();
		question2.setCreatedOn(DateTime.now().minusDays(10));
		question2.setAnswered(false);
		question2.setDocId(2L);
		repository.save(question2);
		
		QuestionIndex question3 = createDomain();
		question3.setCreatedOn(DateTime.now().minusDays(10));
		question3.setAnswered(true);
		question3.setDocId(3L);
		repository.save(question3);
		
		QuestionIndex question4 = createDomain();
		question4.setCreatedOn(DateTime.now().minusDays(10));
		question4.setAnswered(true);
		question4.setDocId(excludeDocId);
		repository.save(question4);
		
		PageRequest pageable = new PageRequest(0, 10, new Sort(Sort.Direction.DESC, "createdOn"));
		List<QuestionIndex> results = repository.findByCreatedOnBetweenAndIsAnsweredAndNotDocId(DateTime.now().minusDays(11), DateTime.now(), true, excludeDocId, pageable).getContent();
		assertTrue(results.size() == 1);
		assertEquals(results.get(0).getId(), question3.getId());
	}

	private QuestionIndex createAndPersistQuestion(String title, long docId, List<QuestionIndex> dataStore) {
		QuestionIndex questionIndex = createDomain();
		questionIndex.setTitle(title);
		questionIndex.setDocId(docId);
		repository.save(questionIndex);
		dataStore.add(questionIndex);
		return questionIndex;
	}
	
	private List<String> createResultsIdsList(List<QuestionIndex> results) {
		List<String> resultIds = CollectionUtil.arrayList();
		for (QuestionIndex question : results) {
			resultIds.add(question.getId());
		}
		return resultIds;
	}
	
	private List<String> generateTagsData() {
		List<String> tags = CollectionUtil.arrayList();
		tags.add("Accounting");
		tags.add("Investment");
		tags.add("Credit Card");
		return tags;
	}
}
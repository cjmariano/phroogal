package com.phroogal.core.service.impl;


import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.phroogal.core.domain.Tag;
import com.phroogal.core.repository.TagRepository;
import com.phroogal.core.repository.TestEntityGenerator;
import com.phroogal.core.service.QuestionService;
import com.phroogal.core.service.Service;
import com.phroogal.core.service.TagService;
import com.phroogal.core.test.helper.RepositoryCleanupHelper;
import com.phroogal.core.test.helper.UnwrapProxyHelper;
import com.phroogal.core.utility.SolrUtility;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/spring/app-context.xml"})
public class TagServiceImplTest extends BaseServiceTest<Tag, ObjectId, TagRepository> {

	@Autowired
	private TagService serviceImpl;
	
	@Autowired
	private TagRepository tagRepository;
	
	@Autowired
	private QuestionService questionService;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	@Qualifier("solrUtility")
	private SolrUtility solrUtility;
	
	@After
	public void tearDown() {
		RepositoryCleanupHelper.dropCollection(mongoTemplate);
		RepositoryCleanupHelper.deleteAllSolrData(solrUtility);
	}
	
	
	@Override
	@SuppressWarnings("unchecked")
	protected Service<Tag, ObjectId> returnServiceImpl() {
		return (Service<Tag, ObjectId>) UnwrapProxyHelper.unwrapProxy(TagServiceImpl.class, serviceImpl);
	}
	
	@Override
	protected TagRepository returnMongoRepository() {
		return Mockito.mock(TagRepository.class);
	}

	@Override
	protected Tag returnDomainInstance() {
		TestEntityGenerator generator = new TestEntityGenerator(); 
		return generator.generateTestTag();
	}

	@Test
	public void testFindByNameStartingWith() throws Exception {
		TagRepository repository = Mockito.mock(TagRepository.class);
		TagService service = (TagService) returnServiceImpl();
		ReflectionTestUtils.setField(service, "tagRepository", repository);
		service.findByNameStartingWith("keyword");
		verify(repository, atLeastOnce()).findByNameStartingWith("keyword");
	}
	
	@Test
	public void testGetTrendingTags() throws Exception {
		TestEntityGenerator generator = new TestEntityGenerator();
		String testTagR3 = "Accounting";
		Tag tag1 = generator.generateTestTag(testTagR3);
		tag1.setTotalNumQuestionsTagged(10);
		tag1.setTotalNumViewsForQuestionsTagged(1000);
		tag1.setRank(703);
		tagRepository.save(tag1);
		
		String testTagR2 = "Investment";
		Tag tag2 = generator.generateTestTag(testTagR2);
		tag2.setTotalNumQuestionsTagged(20);
		tag2.setTotalNumViewsForQuestionsTagged(2000);
		tag2.setRank(6 + 1406);
		tagRepository.save(tag2);
		
		String testTagR1 = "401k(s)";
		Tag tag3 = generator.generateTestTag(testTagR1);
		tag3.setTotalNumQuestionsTagged(30);
		tag3.setTotalNumViewsForQuestionsTagged(3000);
		tag3.setRank(2109);
		tagRepository.save(tag3);
		
		TagService serviceImpl = (TagService) returnServiceImpl();
		ReflectionTestUtils.setField(serviceImpl, "tagRepository", tagRepository);
		List<Tag> results = serviceImpl.getTrendingTags(0, 10).getContent();
		
		Assert.assertTrue(results.get(0).getName().equals(testTagR1));
		Assert.assertTrue(results.get(1).getName().equals(testTagR2));
		Assert.assertTrue(results.get(2).getName().equals(testTagR3));
	}
	
	@Test
	public void testGetTagsSortByAscName() throws Exception {
		TestEntityGenerator generator = new TestEntityGenerator();
		String testTagR3 = "Investment";
		Tag tag1 = generator.generateTestTag(testTagR3);
		tag1.setName(testTagR3);
		tagRepository.save(tag1);
		
		String testTagR2 = "Debit";
		Tag tag2 = generator.generateTestTag(testTagR2);
		tag2.setName(testTagR2);
		tagRepository.save(tag2);
		
		String testTagR1 = "Accounting";
		Tag tag3 = generator.generateTestTag(testTagR1);
		tag3.setName(testTagR1);
		tagRepository.save(tag3);
		
		TagService serviceImpl = (TagService) returnServiceImpl();
		ReflectionTestUtils.setField(serviceImpl, "tagRepository", tagRepository);
		List<Tag> results = serviceImpl.getTagsSortByAscName(0, 10).getContent();
		
		Assert.assertTrue(results.get(0).getName().equals(testTagR1));
		Assert.assertTrue(results.get(1).getName().equals(testTagR2));
		Assert.assertTrue(results.get(2).getName().equals(testTagR3));
	}
	
	@Test
	public void testGetTagsSortByDescTotalNumQuestionsTagged() throws Exception {
		TestEntityGenerator generator = new TestEntityGenerator();
		String testTagR3 = "Accounting";
		Tag tag1 = generator.generateTestTag(testTagR3);
		tag1.setName(testTagR3);
		tag1.setTotalNumQuestionsTagged(100);
		tagRepository.save(tag1);
		
		String testTagR2 = "Debit";
		Tag tag2 = generator.generateTestTag(testTagR2);
		tag2.setName(testTagR2);
		tag2.setTotalNumQuestionsTagged(200);
		tagRepository.save(tag2);
		
		String testTagR1 = "Investment";
		Tag tag3 = generator.generateTestTag(testTagR1);
		tag3.setName(testTagR1);
		tag3.setTotalNumQuestionsTagged(300);
		tagRepository.save(tag3);
		
		TagService serviceImpl = (TagService) returnServiceImpl();
		ReflectionTestUtils.setField(serviceImpl, "tagRepository", tagRepository);
		List<Tag> results = serviceImpl.getTagsSortByDescTotalNumQuestionsTagged(0, 10).getContent();
		
		Assert.assertTrue(results.get(0).getName().equals(testTagR1));
		Assert.assertTrue(results.get(1).getName().equals(testTagR2));
		Assert.assertTrue(results.get(2).getName().equals(testTagR3));
	}
}

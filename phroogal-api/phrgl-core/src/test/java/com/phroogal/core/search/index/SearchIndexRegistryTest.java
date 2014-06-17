package com.phroogal.core.search.index;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.phroogal.core.repository.TestEntityGenerator;
import com.phroogal.core.repository.index.QuestionIndexRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/spring/app-context.xml"})
public class SearchIndexRegistryTest {
	
	@Autowired
	private SearchIndexRegistry<QuestionIndex> indexRegistry;
	
	@Autowired
	private QuestionIndexRepository repository;
	
	@Value("${solr.server.url}")
	private String solrTestServer;
	
	@Before
	public void setUp() {
		deleteAllSolrData();
	}
	
	@After
	public void tearDown() {
		deleteAllSolrData();
	}
	
	@Test
	public void testAddIndex() {
		TestEntityGenerator generator = new TestEntityGenerator();
		QuestionIndex index = generator.generateTestQuestionIndex();
		indexRegistry.addIndex(index);
		
		QuestionIndex persistedIndex = repository.findOne(index.getId());
		assertNotNull(persistedIndex);
		assertEquals(persistedIndex.getId(), index.getId());
		
	}

	@Test
	public void testDeleteIndex() {
		TestEntityGenerator generator = new TestEntityGenerator();
		QuestionIndex index = generator.generateTestQuestionIndex();
		indexRegistry.addIndex(index);
		
		QuestionIndex persistedIndex = repository.findOne(index.getId());
		assertNotNull(persistedIndex);
		assertEquals(persistedIndex.getId(), index.getId());
		
		indexRegistry.deleteIndex(index.getId(), QuestionIndex.class);
		persistedIndex = repository.findOne(index.getId());
		assertNull(persistedIndex);
	}
	
	private void deleteAllSolrData() {
        HttpSolrServer solr = new HttpSolrServer(solrTestServer);
        try {
          solr.deleteByQuery("*:*");
          solr.commit();
        } catch (Exception e) {
          e.printStackTrace();
        }
    }
}

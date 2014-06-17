/**
 * 
 */
package com.phroogal.core.utility;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.phroogal.core.domain.Question;
import com.phroogal.core.repository.QuestionRepository;
import com.phroogal.core.repository.index.QuestionIndexRepository;
import com.phroogal.core.search.index.QuestionIndex;
import com.phroogal.core.service.QuestionService;

/**
 * Utility for executing commands to the Solr server
 * @author Christopher Mariano
 *
 */
@Component("solrUtility")
public class SolrUtility {
	
	private static final Logger log = Logger.getLogger(SolrUtility.class);
	
	@Value("${solr.server.url}")
	private String solrTestServer;
	
	@Value("${solr.server.timeout}")
	private String timeout;
	
	@Autowired
	private QuestionService questionService;
	
	@Autowired
	private QuestionRepository questionRepository;
	
	@Autowired
	private QuestionIndexRepository questionIndexRepository;
	
	/**
	 * Deletes all indexed data on the Solr server
	 * @throws IOException 
	 * @throws SolrServerException 
	 */
	public void deleteAllIndex() throws SolrServerException, IOException {
		HttpSolrServer solr = new HttpSolrServer(solrTestServer);
		int intTimeout = Integer.parseInt(timeout);
		solr.setConnectionTimeout(intTimeout);
		solr.setSoTimeout(intTimeout);
  	  	solr.deleteByQuery("*:*");
        solr.commit();
	}

	/**
	 * Performs a reindex of the Solr data. This is done by deleting existing questions index on Solr, and repopulating
	 * it again based on the existing questions. 
	 * @throws IOException 
	 * @throws SolrServerException 
	 */
	public void resetQuestionIndex() throws SolrServerException, IOException {
		EntityIteratorHelper<Question, QuestionRepository, QuestionService> iterator = new EntityIteratorHelper<Question, QuestionRepository, QuestionService>(questionRepository, questionService) {
			@Override
			protected boolean processEach(Question question) {
				log.info("Processing question with id ["+ question.getId().toString() + "]");
				QuestionIndex index = question.getIndex();
				questionIndexRepository.delete(index);
				questionIndexRepository.save(index);
				return true;
			}
		};
		iterator.startAndContinueOnErrors();
	}
}

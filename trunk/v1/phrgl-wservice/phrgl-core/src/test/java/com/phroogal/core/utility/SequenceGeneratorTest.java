package com.phroogal.core.utility;




import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.phroogal.core.domain.ApplicationConstants;
import com.phroogal.core.domain.Sequence;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/spring/app-context.xml"})
public class SequenceGeneratorTest {

	@Autowired
	private SequenceGenerator sequenceGenerator;
	
	@Autowired
	private MongoOperations mongo;
	
	@Before
	public void setup() {
		mongo.dropCollection(ApplicationConstants.COLLECTION_SEQUENCES);
	}
	
	@Test
	public void testGetNextSequence() throws Exception {
		Long sequence = sequenceGenerator.getNextSequence(ApplicationConstants.COLLECTION_QUESTIONS);
		Assert.assertTrue(sequence == Sequence.INITIAL_SEQ_VALUE);
		
		sequence = sequenceGenerator.getNextSequence(ApplicationConstants.COLLECTION_QUESTIONS);
		Assert.assertTrue(sequence == Sequence.INITIAL_SEQ_VALUE + 1);
		
		sequence = sequenceGenerator.getNextSequence(ApplicationConstants.COLLECTION_QUESTIONS);
		Assert.assertTrue(sequence == Sequence.INITIAL_SEQ_VALUE + 2);
		
		sequence = sequenceGenerator.getNextSequence(ApplicationConstants.COLLECTION_QUESTIONS);
		Assert.assertTrue(sequence == Sequence.INITIAL_SEQ_VALUE + 3);
	}
}

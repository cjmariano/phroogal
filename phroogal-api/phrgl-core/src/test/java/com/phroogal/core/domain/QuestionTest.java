package com.phroogal.core.domain;



import org.junit.Assert;
import org.junit.Test;

import com.phroogal.core.repository.TestEntityGenerator;
import com.phroogal.core.search.index.QuestionIndex;

public class QuestionTest {

	@Test
	public void testGetIndex() throws Exception {
		TestEntityGenerator generator = new TestEntityGenerator();
		Question question = generator.generateTestQuestion();
		QuestionIndex index = question.getIndex();
		
		Assert.assertEquals(question.getId(), index.getId());
		Assert.assertEquals(question.getTitle(), index.getTitle());
	}

}

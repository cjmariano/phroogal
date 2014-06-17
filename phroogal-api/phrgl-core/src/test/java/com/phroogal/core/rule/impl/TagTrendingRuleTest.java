package com.phroogal.core.rule.impl;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.phroogal.core.domain.Tag;
import com.phroogal.core.repository.TestEntityGenerator;
import com.phroogal.core.rule.Fact;
import com.phroogal.core.rule.Rule;
import com.phroogal.core.rule.RuleExecutionContext;
import com.phroogal.core.search.index.QuestionIndex;
import com.phroogal.core.utility.CollectionUtil;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/spring/app-context.xml"})
public class TagTrendingRuleTest {

	@Autowired
	@Qualifier(value="tagTrendingRule")
	private Rule tagTrendingRule;
	
	private TestEntityGenerator generator = new TestEntityGenerator();
	
	@Test
	public void testFireTagTrendingRule() throws Exception {
		String TEST_TAG_R1 = "Investment";
		Long TEST_TOTALNUM_VIEWS_R1 = 1000L;
		String TEST_TAG_R2 = "Credit Card";
		Long TEST_TOTALNUM_VIEWS_R2 = 500L;
		String TEST_TAG_R3 = "401K";
		
		List<Tag> tags = CollectionUtil.arrayList();
		Tag tag1 = generator.generateTestTag(TEST_TAG_R2);
		tags.add(tag1);
		Tag tag2 = generator.generateTestTag(TEST_TAG_R1);
		tags.add(tag2);
		Tag tag3 = generator.generateTestTag(TEST_TAG_R3);
		tags.add(tag3);
		
		List<QuestionIndex> questions = CollectionUtil.arrayList();
		QuestionIndex question = generator.generateTestQuestionIndex();
		question.setTags(Arrays.asList(TEST_TAG_R1));
		question.setTotalViewCount(TEST_TOTALNUM_VIEWS_R1);
		questions.add(question);
		
		QuestionIndex question1 = generator.generateTestQuestionIndex();
		question1.setTags(Arrays.asList(TEST_TAG_R1));
		question1.setTotalViewCount(TEST_TOTALNUM_VIEWS_R1);
		questions.add(question1);
		
		QuestionIndex question2 = generator.generateTestQuestionIndex();
		question2.setTags(Arrays.asList(TEST_TAG_R2));
		question2.setTotalViewCount(TEST_TOTALNUM_VIEWS_R2);
		questions.add(question2);
		
		List<Fact> facts = CollectionUtil.arrayList();
		facts.addAll(tags);
		facts.addAll(questions);
		tagTrendingRule.setFacts(facts);
		RuleExecutionContext<Tag> context = tagTrendingRule.execute();
		List<Tag> results = context.getResults();
		Assert.assertNotNull(results);
		
		CollectionUtil.sortElementsByDescRank(results);
		Assert.assertTrue(results.size() == 3);
		Tag tagRank1 = results.get(0);
		Tag tagRank2 = results.get(1);
		Tag tagRank3 = results.get(2);
		
		Assert.assertTrue(tagRank1.getName().equals(TEST_TAG_R1));
		Assert.assertTrue(tagRank1.getTotalNumQuestionsTagged() == 2);
		Assert.assertTrue(tagRank1.getTotalNumViewsForQuestionsTagged() == TEST_TOTALNUM_VIEWS_R1 * 2);
		Assert.assertTrue(tagRank1.getRank() == computeForRank(tagRank1.getTotalNumQuestionsTagged(), tagRank1.getTotalNumViewsForQuestionsTagged()));
		
		Assert.assertTrue(tagRank2.getName().equals(TEST_TAG_R2));
		Assert.assertTrue(tagRank2.getTotalNumQuestionsTagged() == 1);
		Assert.assertTrue(tagRank2.getTotalNumViewsForQuestionsTagged() == TEST_TOTALNUM_VIEWS_R2 );
		Assert.assertTrue(tagRank2.getRank() == computeForRank(tagRank2.getTotalNumQuestionsTagged(), tagRank2.getTotalNumViewsForQuestionsTagged()));
		
		Assert.assertTrue(tagRank3.getName().equals(TEST_TAG_R3));
		Assert.assertTrue(tagRank3.getTotalNumQuestionsTagged() == 0);
		Assert.assertTrue(tagRank3.getTotalNumViewsForQuestionsTagged() == 0 );
		Assert.assertTrue(tagRank3.getRank() == computeForRank(tagRank3.getTotalNumQuestionsTagged(), tagRank3.getTotalNumViewsForQuestionsTagged()));
	}

	private double computeForRank(long totalNumQuestionsTagged, long totalNumViewsForQuestionsTagged) {
		return (totalNumQuestionsTagged * .3) + (totalNumViewsForQuestionsTagged * .7);
	}
}

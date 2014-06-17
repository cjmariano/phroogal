package com.phroogal.core.utility;

import org.junit.Assert;
import org.junit.Test;

public class SearchKeywordUtilityTest {

	@Test
	public void testDistill() throws Exception {
		String testKeyword = "What is a credit card?";
		Assert.assertTrue(SearchKeywordUtility.distill(testKeyword).equals("What is a credit card"));
		
		testKeyword = "What is an over-the-counter transaction";
		Assert.assertTrue(SearchKeywordUtility.distill(testKeyword).equals("What is an over the counter transaction"));
		
		testKeyword = "What is a credit'";
		Assert.assertTrue(SearchKeywordUtility.distill(testKeyword).equals("What is a credit"));
		
		testKeyword = "What is a credit#";
		Assert.assertTrue(SearchKeywordUtility.distill(testKeyword).equals("What is a credit"));
		
		testKeyword = "What is a credit/";
		Assert.assertTrue(SearchKeywordUtility.distill(testKeyword).equals("What is a credit"));
	}
}

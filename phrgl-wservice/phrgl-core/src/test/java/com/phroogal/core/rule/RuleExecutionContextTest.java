package com.phroogal.core.rule;


import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class RuleExecutionContextTest {

	@Test
	public void testGetResults() throws Exception {
		RuleExecutionContext<String> context = new RuleExecutionContext<String>(); 
		context.addResult("A");
		context.addResult("B");
		context.addResult("C");
		final List<String> results = context.getResults();
		Assert.assertTrue(results != null && results.size() == 3);
		Assert.assertTrue(results.contains("A"));
		Assert.assertTrue(results.contains("B"));
		Assert.assertTrue(results.contains("C"));
	}

	@Test
	public void testGetDistinctResults() throws Exception {
		RuleExecutionContext<String> context = new RuleExecutionContext<String>(); 
		context.addResult("A");
		context.addResult("B");
		context.addResult("C");
		context.addResult("A");
		context.addResult("A");
		context.addResult("A");
		final List<String> results = context.getDistinctResults();
		Assert.assertTrue(results != null && results.size() == 3);
		Assert.assertTrue(results.contains("A"));
		Assert.assertTrue(results.contains("B"));
		Assert.assertTrue(results.contains("C"));
	}
}

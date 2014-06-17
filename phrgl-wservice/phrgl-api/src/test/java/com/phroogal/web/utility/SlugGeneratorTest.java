package com.phroogal.web.utility;


import org.junit.Assert;
import org.junit.Test;

public class SlugGeneratorTest {

	@Test
	public void testToSlugSimple() throws Exception {
		String result = "what-is-a-credit-card";
		String slug = SlugGenerator.toSlug("What is a credit card");
		Assert.assertEquals(result, slug);
	}
	
	@Test
	public void testToSlugNoQuestionMark() throws Exception {
		String result = "what-is-a-credit-card";
		String slug = SlugGenerator.toSlug("What is a credit card?");
		Assert.assertEquals(result, slug);
	}
	
	@Test
	public void testToSlugDotCom() throws Exception {
		String result = "what-is-mint-com";
		String slug = SlugGenerator.toSlug("What is mint.com?");
		Assert.assertEquals(result, slug);
		
		result = "what-is-groupon-com-ph";
		slug = SlugGenerator.toSlug("What is groupon.com.ph?");
		Assert.assertEquals(result, slug);
	}
	
	@Test
	public void testToSlugReplaceDoubleQuotes() throws Exception {
		String result = "what-is-a-good-investment";
		String slug = SlugGenerator.toSlug("What is a \"good\" investment?");
		Assert.assertEquals(result, slug);
	}
	
	@Test
	public void testToSlugReplaceSingleQuotes() throws Exception {
		String result = "what-is-a-good-investment";
		String slug = SlugGenerator.toSlug("What is a 'good' investment?");
		Assert.assertEquals(result, slug);
	}

}

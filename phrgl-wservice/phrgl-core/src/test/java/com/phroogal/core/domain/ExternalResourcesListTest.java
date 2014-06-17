package com.phroogal.core.domain;


import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.google.api.services.customsearch.model.Result;
import com.google.api.services.customsearch.model.Search;
import com.phroogal.core.utility.CollectionUtil;

public class ExternalResourcesListTest {

	private static final String TEST_LINK = "http://test.com";
	private static final String TEST_SNIPPET = "This is a result";
	private static final String TEST_SOURCE = "Search Result";
	private static final String TEST_TITLE = "Title";

	@Test
	public void testGet() throws Exception {
		Search searchResults = new Search();
		searchResults.setItems(generateTestSearchItems());
		
		ExternalResourcesList externalResources = new ExternalResourcesList(searchResults);
		List<ExternalResource> resultsList = externalResources.get();
		
		Assert.assertEquals(TEST_LINK, resultsList.get(0).getExternalLink());
		Assert.assertEquals(TEST_SNIPPET, resultsList.get(0).getSnippet());
		Assert.assertEquals(TEST_SOURCE, resultsList.get(0).getSource());
		Assert.assertEquals(TEST_TITLE, resultsList.get(0).getTitle());
	}

	private List<Result> generateTestSearchItems() {
		List<Result> searchResultItems = CollectionUtil.arrayList();
		Result result = new Result();
		result.setTitle(TEST_TITLE);
		result.setDisplayLink(TEST_SOURCE);
		result.setHtmlSnippet(TEST_SNIPPET);
		result.setLink(TEST_LINK);
		searchResultItems.add(result);
		return searchResultItems;
	}

}

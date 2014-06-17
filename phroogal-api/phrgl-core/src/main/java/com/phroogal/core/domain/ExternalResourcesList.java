package com.phroogal.core.domain;

import java.util.List;

import com.google.api.services.customsearch.model.Result;
import com.google.api.services.customsearch.model.Search;
import com.phroogal.core.utility.CollectionUtil;


/**
 * Wrapper class that contains the lists of External resources
 * @author Christopher Mariano
 *
 */
public class ExternalResourcesList {

	private List<ExternalResource> indexlist = CollectionUtil.arrayList();
	
	public ExternalResourcesList(Search searchResults) {
		if (thereAreSearchResults(searchResults)) {
			List<Result> results = searchResults.getItems();
			for (Result result : results) {
				ExternalResource resourceIndex = new ExternalResource(); 
				resourceIndex.setTitle(result.getTitle());
				resourceIndex.setSource(result.getDisplayLink());
				resourceIndex.setSnippet(result.getHtmlSnippet());
				resourceIndex.setExternalLink(result.getLink());
				indexlist.add(resourceIndex);
			}	
		}
	}
	
	/**
	 * Gets list of external Resource index
	 * @return list of indexed external resource
	 */
	public List<ExternalResource> get() {
		return indexlist;
	}
	
	private boolean thereAreSearchResults(Search searchResults) {
		return searchResults != null && ! searchResults.getItems().isEmpty();
	}
}

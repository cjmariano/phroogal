/**
 * 
 */
package com.phroogal.core.service.mock;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.test.util.ReflectionTestUtils;

import com.phroogal.core.domain.ExternalResource;
import com.phroogal.core.domain.ExternalResourcesList;
import com.phroogal.core.exception.ExternalResourcesNotFoundException;
import com.phroogal.core.service.ExternalResourceService;
import com.phroogal.core.utility.CollectionUtil;

public class MockExternalResourceServiceImpl implements ExternalResourceService {
	
	public static final String NO_RESULTS_KEYWORD = "noresultsforthiskeywordshouldbefetched";
	
	@Override
	@SuppressWarnings("unchecked")
	public List<ExternalResource> getResourceByKeyword(String keyword, Long start) throws ExternalResourcesNotFoundException {
		if (keyword.equals(StringUtils.EMPTY)) {
			return Collections.EMPTY_LIST;
		}
		ExternalResourcesList resourceList = new ExternalResourcesList(null);
		String [] parameters = keyword.split("\\|");
		String category = parameters.length == 2 ? parameters[1] : "all_categories"; 
		throwNoResultsFoundIfExpected(parameters[0]);
		ReflectionTestUtils.setField(resourceList, "indexlist", populateMockResults(category));
		return resourceList.get();
	}

	@Override
	public List<ExternalResource> getResourceByKeyword(String category, String keyword, Long start) throws ExternalResourcesNotFoundException {
		return this.getResourceByKeyword(keyword + "|" + category, 0L); 
	}
	
	private void throwNoResultsFoundIfExpected(String keyword) {
		if (keyword.equals(NO_RESULTS_KEYWORD)) {
			throw new ExternalResourcesNotFoundException();
		}
	}
	
	private List<ExternalResource> populateMockResults(String category) {
		List<ExternalResource> indexlist = CollectionUtil.arrayList();
		for (int i = 1; i < 11; i++) {
			ExternalResource resource = new ExternalResource();
			resource.setTitle("External Resource link 1 | " + category + " | " + i);
			resource.setSource("www.irs.gov");
			resource.setExternalLink("http://www.google.com/?q=Title " + i);
			resource.setSnippet("May 16, 2013 <b>...</b> Pay your Taxes by Debit or <b>Credit Card</b>. Choose your payment processor and <br>  pay now. Español. You can pay by debit or <b>credit card</b> whether&nbsp;<b>...</b>");
			indexlist.add(resource);
		}
		return indexlist;
	}
}

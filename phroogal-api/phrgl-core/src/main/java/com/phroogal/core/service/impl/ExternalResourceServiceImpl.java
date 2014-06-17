/**
 * 
 */
package com.phroogal.core.service.impl;

import java.io.IOException;
import java.util.Collections;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.customsearch.Customsearch;
import com.google.api.services.customsearch.Customsearch.Cse.List;
import com.phroogal.core.domain.ExternalResource;
import com.phroogal.core.domain.ExternalResourcesList;
import com.phroogal.core.exception.ExternalResourcesNotFoundException;
import com.phroogal.core.service.ExternalResourceService;

public class ExternalResourceServiceImpl implements ExternalResourceService {
	
	private static final String PREFIX_CATEGORY_SEARCH = " more:";

	@Value("${google.cse.apiKey}")
	private String googleSearchApiKey;
	
	@Value("${google.cse.id}")
	private String googleSearchEngineId;
	
	private HttpRequestInitializer httpRequestInitializer = new HttpRequestInitializer() {
		@Override
		public void initialize(HttpRequest arg0) throws IOException {
		}
	};
	
	@Override
	@SuppressWarnings("unchecked")
	public java.util.List<ExternalResource> getResourceByKeyword(String keyword, Long start) throws ExternalResourcesNotFoundException {
		if (keyword.equals(StringUtils.EMPTY)) {
			return Collections.EMPTY_LIST;
		}
		try {
			Customsearch customsearch = new Customsearch(new NetHttpTransport(), new JacksonFactory(), httpRequestInitializer);
			List list = customsearch.cse().list(keyword);
			list.setKey(googleSearchApiKey);
			list.setCx(googleSearchEngineId);
			list.setStart(start);
			ExternalResourcesList resourceList = new ExternalResourcesList(list.execute());
			return resourceList.get();
		} catch (Exception e) {
			throw new ExternalResourcesNotFoundException(e);
		} 
	}
	
	@Override
	public java.util.List<ExternalResource> getResourceByKeyword(String category, String keyword, Long start) throws ExternalResourcesNotFoundException {
		return this.getResourceByKeyword(addCategoryToKeyword(category, keyword), start); 
	}
	
	private String addCategoryToKeyword(String category, String keyword) {
		StringBuffer sb = new StringBuffer(keyword);
		sb.append(PREFIX_CATEGORY_SEARCH);
		sb.append(category);
		return sb.toString();
	}
}

package com.phroogal.web.bean;

import java.util.List;

public class ExternalResourceBean {
	
	private String previousPageResoure;
	
	private String nextPageResoure;
	
	private List<ExternalResourceIndexBean> results;


	public String getPreviousPageResoure() {
		return previousPageResoure;
	}

	public void setPreviousPageResoure(String previousPageResoure) {
		this.previousPageResoure = previousPageResoure;
	}

	public String getNextPageResoure() {
		return nextPageResoure;
	}

	public void setNextPageResoure(String nextPageResoure) {
		this.nextPageResoure = nextPageResoure;
	}

	public List<ExternalResourceIndexBean> getResults() {
		return results;
	}

	public void setResults(List<ExternalResourceIndexBean> results) {
		this.results = results;
	}

}

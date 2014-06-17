package com.phroogal.web.bean;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

/**
 * Bean to hold documents of type {@link Page}
 * @author Christopher Mariano
 *
 */
public class PageBean<T> {

	private List<T> content = new ArrayList<T>();
	
	private long totalElements;
	
	private int totalPages;
	
	private int size;
	
	private int number;
	
	private boolean isFirstPage;
	
	private boolean isLastPage;
	
	public List<T> getContent() {
		return content;
	}

	public void setContent(List<T> content) {
		this.content = content;
	}

	public long getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(long totalElements) {
		this.totalElements = totalElements;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public boolean isFirstPage() {
		return isFirstPage;
	}

	public void setFirstPage(boolean isFirstPage) {
		this.isFirstPage = isFirstPage;
	}

	public boolean isLastPage() {
		return isLastPage;
	}

	public void setLastPage(boolean isLastPage) {
		this.isLastPage = isLastPage;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

}

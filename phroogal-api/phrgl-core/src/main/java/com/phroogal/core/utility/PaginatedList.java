package com.phroogal.core.utility;

import java.util.List;

/**
 * Helper class that implements paging over a collection.
 * 
 * @author Christopher Mariano
 */
public class PaginatedList<T> {

	/** the default page size */
	public static final int DEFAULT_PAGE_SIZE = 10;

	/** the list over which this class is paging */
	private List<T> list;

	/** the page size */
	private int pageSize = DEFAULT_PAGE_SIZE;

	/** the current page */
	private int page;

	/** the starting index */
	private int startingIndex;

	/** the ending index */
	private int endingIndex;

	/** the maximum number of pages */
	private int maxPages;
	
	/**
	 * Creates a new instance with the specified list, and initialises the page, and page size
	 * 
	 * @param list
	 *            a List
	 */
	public PaginatedList(List<T> list, int pageAt, int pageSize) {
		this.list = list;
		this.page = pageAt;
		this.maxPages = 1;
		this.pageSize = pageSize;
		calculatePages();
		setPageIndex();
	}
	
	private void calculatePages() {
		if (pageSize > 0) {
			// calculate how many pages there are
			if (list.size() % pageSize == 0) {
				maxPages = list.size() / pageSize;
			} else {
				maxPages = (list.size() / pageSize) + 1;
			}
		}
	}
	
	private void setPageIndex() {
		startingIndex = pageSize * (page);
		if (startingIndex < 0) {
			startingIndex = 0;
		}
		endingIndex = startingIndex + pageSize;
		if (endingIndex > list.size()) {
			endingIndex = list.size();
		}
	}

	/**
	 * Gets the list that this instance is paging over.
	 * 
	 * @return a List
	 */
	public List<T> getList() {
		return this.list;
	}

	/**
	 * Gets the subset of the list for the current page.
	 * 
	 * @return a List
	 */
	public List<T> getListForPage() {
		return list.subList(startingIndex, endingIndex);
	}

	/**
	 * Gets the page size.
	 * 
	 * @return the page size as an int
	 */
	public int getPageSize() {
		return this.pageSize;
	}

	/**
	 * Sets the page size.
	 * 
	 * @param pageSize
	 *            the page size as an int
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * Gets the page.
	 * 
	 * @return the page as an int
	 */
	public int getPage() {
		return this.page;
	}

	/**
	 * Sets the page size.
	 * 
	 * @param p
	 *            the page as an int
	 */
	public void setPage(int p) {
		this.page = p;
	}

	/**
	 * Gets the maximum number of pages.
	 * 
	 * @return the maximum number of pages as an int
	 */
	public int getMaxPages() {
		return this.maxPages;
	}
}

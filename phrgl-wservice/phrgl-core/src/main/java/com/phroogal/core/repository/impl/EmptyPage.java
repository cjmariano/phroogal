/**
 * 
 */
package com.phroogal.core.repository.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * Wraps an empty page to be returned when an instance of {@link Page} is required but its contents are empty
 * @author Christopher Mariano
 *
 */
public class EmptyPage<T> implements Page<T> {

	@Override
	public int getNumber() {
		return 0;
	}

	@Override
	public int getSize() {
		return 0;
	}

	@Override
	public int getTotalPages() {
		return 0;
	}

	@Override
	public int getNumberOfElements() {
		return 0;
	}

	@Override
	public long getTotalElements() {
		return 0;
	}

	@Override
	public boolean hasPreviousPage() {
		return false;
	}

	@Override
	public boolean isFirstPage() {
		return false;
	}

	@Override
	public boolean hasNextPage() {
		return false;
	}

	@Override
	public boolean isLastPage() {
		return false;
	}

	@Override
	public Pageable nextPageable() {
		return null;
	}

	@Override
	public Pageable previousPageable() {
		return null;
	}

	@Override
	public Iterator<T> iterator() {
		return null;
	}

	@Override
	public List<T> getContent() {
		return new ArrayList<T>();
	}

	@Override
	public boolean hasContent() {
		return false;
	}

	@Override
	public Sort getSort() {
		return null;
	}

}

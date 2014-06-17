/**
 * 
 */
package com.phroogal.core.domain;

import com.phroogal.core.search.index.Index;

/**
 * Implemented by domain which has an index in a search engine server 
 * @author Christopher Mariano
 *
 */
public interface SearchableIndex<T extends Index> {

	/**
	 * Derives the index based to be used when searching
	 * @return the index
	 */
	public T getIndex();
}

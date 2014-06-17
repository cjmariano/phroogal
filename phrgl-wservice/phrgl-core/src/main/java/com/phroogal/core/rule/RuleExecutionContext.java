/**
 * 
 */
package com.phroogal.core.rule;

import java.util.List;

import com.phroogal.core.utility.CollectionUtil;

/**
 * Provides a context for holding results from a rule execution.
 * @author Christopher Mariano
 *
 */
public class RuleExecutionContext<T> implements Fact {

	private List<T> results;
	
	/**
	 * Adds a result on this execution context
	 * @param result to be added
	 */
	public void addResult(T result) {
		if (results == null) {
			results = CollectionUtil.arrayList();
		}
		results.add(result);
	}
	
	/**
	 * Get the results that is added to this context
	 * @return the results
	 */
	public List<T> getResults() {
		return results;
	}
	
	/**
	 * Get the distinct results that is added to this context
	 * @return the results that is stripped of duplicates.
	 */
	public List<T> getDistinctResults() {
		return CollectionUtil.removeDuplicates(results);
	}
}

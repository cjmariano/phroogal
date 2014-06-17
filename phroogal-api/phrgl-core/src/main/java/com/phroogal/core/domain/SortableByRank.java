/**
 * 
 */
package com.phroogal.core.domain;

/**
 * Implemented by classes that could be sorted by rank
 * @author Christopher Mariano
 *
 */
public interface SortableByRank {

	/**
	 * returns the rank
	 * @return rank
	 */
	public double getRank();

	/**
	 * sets rank
	 * @param rank 
	 */
	public void setRank(double rank);
}

/**
 * 
 */
package com.phroogal.web.bean;

import java.util.HashSet;
import java.util.Set;

/**
 * Encapsulates data that pertains to votes received by a certain domain
 * @author Reylen Catungal
 *
 */
public class VotesBean {
	
	private int total;
	
	private Set<String> userUpvotes = new HashSet<String>();
	
	private Set<String> userDownvotes  = new HashSet<String>();
	
	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public Set<String> getUserUpvotes() {
		return userUpvotes;
	}

	public void setUserUpvotes(Set<String> userUpvotes) {
		this.userUpvotes = userUpvotes;
	}

	public Set<String> getUserDownvotes() {
		return userDownvotes;
	}

	public void setUserDownvotes(Set<String> userDownvotes) {
		this.userDownvotes = userDownvotes;
	}

}

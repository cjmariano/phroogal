/**
 * 
 */
package com.phroogal.core.domain;

import java.util.HashSet;
import java.util.Set;

import org.bson.types.ObjectId;

/**
 * Encapsulates data that pertains to votes received by a certain domain
 * @author Reylen Catungal
 *
 */
public class Votes {
	
	private int total;
	
	private Set<ObjectId> userUpvotes = new HashSet<ObjectId>();
	
	private Set<ObjectId> userDownvotes  = new HashSet<ObjectId>();
	
	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public Set<ObjectId> getUserUpvotes() {
		return userUpvotes;
	}

	public void setUserUpvotes(Set<ObjectId> userUpvotes) {
		this.userUpvotes = userUpvotes;
	}

	public Set<ObjectId> getUserDownvotes() {
		return userDownvotes;
	}

	public void setUserDownvotes(Set<ObjectId> userDownvotes) {
		this.userDownvotes = userDownvotes;
	}

}

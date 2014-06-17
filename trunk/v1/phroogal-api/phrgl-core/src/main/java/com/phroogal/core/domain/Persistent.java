package com.phroogal.core.domain;

import java.io.Serializable;


/**
 * Models an object that is stored in the data store.
 * 
 */
public interface Persistent<ID extends Serializable> {
	/**
	 * Returns the unique identifier of this object.
	 */
	ID getId();
	
	/**
	 * Allows for assigning of IDs.
	 */
	void setId(ID id);
}

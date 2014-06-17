/**
 * 
 */
package com.phroogal.core.service;

import java.util.List;

import org.bson.types.ObjectId;

import com.phroogal.core.domain.CreditUnion;

/**
 * Service for {@link CreditUnion} functions
 * @author Christopher Mariano
 *
 */
public interface CreditUnionService extends Service<CreditUnion, ObjectId> {

	/**
	 * Retrieves a credit union by name using a keyword
	 * @param keyword to be used to search for a credit union
	 * @return a list of credit unions that matches the keyword
	 */
	public List<CreditUnion> searchByName(String keyword);	
}

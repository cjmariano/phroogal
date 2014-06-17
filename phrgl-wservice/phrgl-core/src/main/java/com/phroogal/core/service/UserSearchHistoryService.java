/**
 * 
 */
package com.phroogal.core.service;

import org.bson.types.ObjectId;

import com.phroogal.core.domain.analytics.UserSearchHistory;
/**
 * Service for {@link UserSearchHistoryService} functions
 * @author Christopher Mariano
 *
 */
public interface UserSearchHistoryService extends Service<UserSearchHistory, ObjectId> {
	
	/**
	 * Adds the keyword to the user search history
	 * @param keyword to be added 
	 */
	public void addKeywordToUserSearchHistory (String keyword);
	
}

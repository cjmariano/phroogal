package com.phroogal.core.domain.analytics;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.phroogal.core.domain.ApplicationConstants;
import com.phroogal.core.domain.Persistent;

/**
 * Search keywords or terms being serched by the user
 * @author Christopher Mariano
 *
 */
@Document(collection = ApplicationConstants.COLLECTION_DATA_USER_SEARCH_HISTORY)
public class UserSearchHistory implements Persistent<ObjectId>, Serializable {
	
	private static final long serialVersionUID = 9078684167863829140L;

	@Id
	private ObjectId id;
	
	private ObjectId userId;
	
	private String userPrimaryEmail;
	
	private Set<String> searchTerms;
	
	/**
	 * Adds the given search term that the user used to search the site.
	 * @param searchTerm user used to search the site
	 */
	public void addSearchTerm(String searchTerm) {
		if ( ! StringUtils.isEmpty(searchTerm)) {
			if (searchTerms == null) {
				searchTerms = new HashSet<String>();
			}
			searchTerms.add(searchTerm.toLowerCase());	
		}
	}
	
	public ObjectId getUserId() {
		return userId;
	}

	public void setUserId(ObjectId userId) {
		this.userId = userId;
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public Set<String> getSearchTerms() {
		return searchTerms;
	}

	public void setSearchTerms(Set<String> searchTerms) {
		this.searchTerms = searchTerms;
	}

	public String getUserPrimaryEmail() {
		return userPrimaryEmail;
	}

	public void setUserPrimaryEmail(String userPrimaryEmail) {
		this.userPrimaryEmail = userPrimaryEmail;
	}
}

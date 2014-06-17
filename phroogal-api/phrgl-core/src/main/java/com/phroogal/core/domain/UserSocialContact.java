/**
 * 
 */
package com.phroogal.core.domain;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.phroogal.core.utility.CollectionUtil;

/**
 * User and social contact IDs mapping
 * @author Christopher Mariano
 *
 */
@Document(collection = ApplicationConstants.COLLECTION_USER_SOCIAL_CONTACTS)
public class UserSocialContact implements Persistent<ObjectId>  {
	
	@Id
	private ObjectId id;
	
	/**
	 * Registered id of the user 
	 */
	private ObjectId userId;
	
	/**
	 * List of registered id of the social contact 
	 */
	private List<ObjectId> socialContactIds;
	
	/**
	 * Adds the given list to the social contacts of this user.
	 * Duplicates on the existing is ignored
	 * @param socialContacts
	 */
	public void addSocialContact(List<ObjectId> socialContacts) {
		if (socialContacts != null && socialContacts.size() > 1) {
			getSocialContactIds().addAll(socialContacts);
			setSocialContactIds(CollectionUtil.removeDuplicates(socialContactIds));	
		}
	}
	

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public ObjectId getUserId() {
		return userId;
	}

	public void setUserId(ObjectId userId) {
		this.userId = userId;
	}

	public List<ObjectId> getSocialContactIds() {
		if (socialContactIds == null) {
			socialContactIds = CollectionUtil.arrayList();
		}
		return socialContactIds;
	}

	private void setSocialContactIds(List<ObjectId> socialContactIds) {
		this.socialContactIds = socialContactIds;
	} 
}

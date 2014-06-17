/**
 * 
 */
package com.phroogal.core.domain;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.phroogal.core.social.SocialNetworkType;


/**
 * Holds information about a user's social connections. This could include friends, and contacts from 
 * the given social network site
 * 
 * @author Christopher Mariano
 *
 */
@Document(collection = ApplicationConstants.COLLECTION_SOCIAL_CONTACTS)
public class SocialContact implements Persistent<ObjectId> {
	
	@Id
	private ObjectId id;

	@Indexed
	/**
	 * Id of a user which is registered with the application
	 */
	private ObjectId userId;
	
	/**
	 * Social Network that this contact is registered with.
	 */
	@Indexed
	private SocialNetworkType connectedThru;
	
	/**
	 * Id of a user's contact which is assigned by the provider social network
	 */
	private String contactUserId;
	
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

	public SocialNetworkType getConnectedThru() {
		return connectedThru;
	}

	public void setConnectedThru(SocialNetworkType connectedVia) {
		this.connectedThru = connectedVia;
	}

	public String getContactUserId() {
		return contactUserId;
	}

	public void setContactUserId(String contactUserId) {
		this.contactUserId = contactUserId;
	}
}

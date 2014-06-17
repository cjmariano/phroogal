/**
 * 
 */
package com.phroogal.core.social;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.social.ApiBinding;
import org.springframework.social.connect.Connection;

import com.phroogal.core.domain.SocialContact;
import com.phroogal.core.utility.CollectionUtil;

/**
 * Provides common implementation for social network functions.
 * @author Christopher Mariano 
 *
 */
public abstract class BaseSocialNetworkApi implements SocialNetwork{
	
	/**
	 * Returns the list of contacts(i.e. friends, connections) by their usernames 
	 * as set by their social network connection 
	 * @return list of usernames that is enlisted as friends, contacts etc.
	 */
	protected abstract List<String> returnContactUserId();
	
	private Connection<?> connection;
	
	@Override
	public String getFirstName() {
		return connection.fetchUserProfile().getFirstName();
	}

	@Override
	public String getLastName() {
		return connection.fetchUserProfile().getLastName();
	}

	/**
	 * Defaults to returning a small profile picture if an overriding method is not provided by the implementing class  
	 */
	@Override
	public String getProfilePictureUrl() {
		return getProfileSmallPictureUrl();
	}
	
	@Override
	public String getProfileSmallPictureUrl() {
		return connection.createData().getImageUrl();
	}
	
	@Override
	public String getProfileUrl() {
		return connection.getProfileUrl();
	}
	
	@Override
	public String getPrimaryEmail() {
		return connection.fetchUserProfile().getEmail();
	}
	
	@Override
	public List<SocialContact> getSocialContacts(ObjectId userId) {
		List<SocialContact> socialContacts = CollectionUtil.arrayList();
		SocialNetworkType socialNetworkType = getSocialNetwork();
		
		List<String> contacts = returnContactUserId();
		if (contacts != null) {
			for (String contactUserId : contacts) {
				SocialContact contact = new SocialContact();
				contact.setUserId(userId);
				contact.setConnectedThru(socialNetworkType);
				contact.setContactUserId(contactUserId);
				socialContacts.add(contact);
			}	
		}
		return socialContacts;
	}

	@Override
	public Connection<?> getConnection() {
		return connection;
	}
	
	/**
	 * Returns the {@link ApiBinding} that will be used as access to social network operations
	 * @return the api from the current social connection
	 */
	public Object getApi() {
		return connection.getApi();
	}

	/**
	 * Sets the connection to be used by this class and its child classes
	 * @param connection
	 */
	public void setConnection(Connection<?> connection) {
		this.connection = connection;
	}
	
	private SocialNetworkType getSocialNetwork() {
		String providerId = connection.createData().getProviderId();
		return SocialNetworkType.get(providerId);
	}
}

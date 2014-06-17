/**
 * 
 */
package com.phroogal.core.social;

import java.util.List;

import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.springframework.social.connect.Connection;

import com.phroogal.core.domain.GenderType;
import com.phroogal.core.domain.Location;
import com.phroogal.core.domain.SocialContact;



/**
 * Implemented by domain classes that would provide an access point to Spring social APIs
 * @author Christopher Mariano
 *
 */
public interface SocialNetwork {
	
	/**
	 * Returns the current social connection. Exposed to be used by child classes
	 * @return instance of {@link Connection}
	 */
	public Connection<?> getConnection();
	
	/**
	 * Returns an instance of {@link SocialNetworkType} that corresponds to this network
	 */
	public SocialNetworkType getSocialNetworkType(); 
	
	/**
	 * Returns the first name of the user retrieved via its social connection
	 * @return
	 */
	public String getFirstName();
	
	/**
	 * Returns the last name of the user retrieved via its social connection
	 * @return
	 */
	public String getLastName();
	
	/**
	 * Returns the bio of the user retrieved via its social connection
	 * @return
	 */
	public String getBio();
	
	/**
	 * Returns the {@link Location} of the user retrieved via its social connection
	 * @return
	 */
	public Location getLocation();
	
	
	/**
	 * Returns the link to the user's profile picture
	 * @return
	 */
	public String getProfilePictureUrl();
	
	/**
	 * Returns the link to the user's profile picture (smaller size) 
	 * @return
	 */
	public String getProfileSmallPictureUrl();
	
	/**
	 * Returns the url that links to the user's profile page on that social connection.
	 * @return user birthdate
	 */
	public String getProfileUrl();
	
	/**
	 * Returns the primary email of the current social connection.
	 * @return email
	 */
	public String getPrimaryEmail();
	
	/**
	 * Returns the birth date of the current social connection.
	 * @return user birthdate
	 */
	public DateTime getDateOfBirth();
	
	/**
	 * Returns the gender of the current social connection.
	 * @return gender
	 */
	public GenderType getGender();
	
	/**
	 * Returns the list of {@link SocialContact} that is connected to the current user via 
	 * their social connections
	 * @param userId of the user
	 * @return
	 */
	public List<SocialContact> getSocialContacts(ObjectId userId);
}

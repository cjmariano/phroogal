/**
 * 
 */
package com.phroogal.core.domain;

import java.io.Serializable;
import java.util.List;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;

import com.phroogal.core.social.SocialNetwork;
import com.phroogal.core.social.SocialNetworkType;


/**
 * Holds information that are provided by social network sites that the user
 * opts to connect his/her profile to.  
 * @author Christopher Mariano
 *
 */
public class SocialProfile implements Serializable{
	
	private static final long serialVersionUID = 5094777233145525096L;

	private SocialNetworkType site;
	
	private boolean isPrimaryConnection;
	
	private String userId;
	
	private String email;
	
	private String profileUrl;
	
	private List<SocialContact> connections;
	
	/**
	 * Convenience method that transfers data from a social profile connection into the {@link SocialProfile} domain
	 * @param social network connection that holds data
	 * @return instance of populated {@link SocialProfile} 
	 */
	public static SocialProfile createSocialProfile(SocialNetwork api) {
		Connection<?> connection = api.getConnection();
		SocialProfile socialProfile = new SocialProfile();
		String providerId = connection.createData().getProviderId();
		socialProfile.setSite(SocialNetworkType.get(providerId));
		socialProfile.setUserId(connection.createData().getProviderUserId());
		socialProfile.setProfileUrl(api.getProfileUrl());
		socialProfile.setEmail(api.getPrimaryEmail());
		return socialProfile;
	}
	
	/**
	 * Convenience method that generates a unique handle for a social profile
	 * @param current social connection
	 * @return the unique handle generated for the social connection. 
	 */
	public static String createUniqueHandle(Connection<?> connection) {
		ConnectionData connectionData = connection.createData();
		StringBuffer uniqueHandle = new StringBuffer();
		uniqueHandle.append(connectionData.getProviderUserId());
		uniqueHandle.append("@");
		uniqueHandle.append(connectionData.getProviderId());
		return uniqueHandle.toString();
	}
	
	public SocialNetworkType getSite() {
		return site;
	}

	public void setSite(SocialNetworkType site) {
		this.site = site;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<SocialContact> getConnections() {
		return connections;
	}

	public void setConnections(List<SocialContact> connections) {
		this.connections = connections;
	}

	public String getProfileUrl() {
		return profileUrl;
	}

	public void setProfileUrl(String profileUrl) {
		this.profileUrl = profileUrl;
	}

	public boolean isPrimaryConnection() {
		return isPrimaryConnection;
	}

	public void setPrimaryConnection(boolean isPrimaryConnection) {
		this.isPrimaryConnection = isPrimaryConnection;
	}
}

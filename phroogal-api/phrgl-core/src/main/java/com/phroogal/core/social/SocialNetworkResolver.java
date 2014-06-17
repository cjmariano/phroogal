/**
 * 
 */
package com.phroogal.core.social;

import java.util.Map;

import org.springframework.social.connect.Connection;

import com.phroogal.core.service.AuthenticationDetailsService;

/**
 * Maps the specific implementation of {@link SocialNetwork} from a 
 * given providerId (e.g. facebook, linkedin)
 * @author Christopher Mariano
 *
 */
public class SocialNetworkResolver {

	private Map<String, ? extends SocialNetwork> socialNetworkMap;
	
	private AuthenticationDetailsService<String> authenticationDetailsService;
	
	/**
	 * Returns the mapped social network api given the providerId
	 * @param connection that holds the current social network connection (e.g. facebook, linkedin)
	 * @return instance of {@link SocialNetwork} 
	 */
	public SocialNetwork getApi(Connection<?> connection) {
		String providerId = connection.createData().getProviderId();
		BaseSocialNetworkApi apiBinding = (BaseSocialNetworkApi) socialNetworkMap.get(providerId);
		apiBinding.setConnection(connection);
		return apiBinding;
	}

	public Map<String, ? extends SocialNetwork> getSocialNetworkMap() {
		return socialNetworkMap;
	}

	public void setSocialNetworkMap(Map<String, ? extends SocialNetwork> socialNetworkMap) {
		this.socialNetworkMap = socialNetworkMap;
	}

	public AuthenticationDetailsService<String> getAuthenticationDetailsService() {
		return authenticationDetailsService;
	}

	public void setAuthenticationDetailsService(AuthenticationDetailsService<String> authenticationDetailsService) {
		this.authenticationDetailsService = authenticationDetailsService;
	}
}

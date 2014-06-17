package com.phroogal.core.repository;

import java.util.Collection;
import java.util.List;

import com.phroogal.core.domain.UserSocialConnection;

public interface UserSocialConnectionRepository extends BaseMongoRepository<UserSocialConnection> {
	
	/**
	 * Retrieves user id of users currently connected to the social network
	 * @param userId
	 * @return list of user connection details
	 */
    List<UserSocialConnection> findByUserId(String userId);
     
    /**
     * Retrieves user id and provider id of users currently connected to the social network
     * @param userId
     * @param providerId
     * @return list of user connection details
     */
    List<UserSocialConnection> findByUserIdAndProviderId(String userId, String providerId);
     
    /**
     * Retrieves provider id and provider user id of users currently connected to the social network
     * @param providerId
     * @param providerUserId
     * @return list of user connection details
     */
    List<UserSocialConnection> findByProviderIdAndProviderUserId(String providerId, String providerUserId);
     
    /**
     * Retrieves provider id and provider user id of the user currently connected to the social network
     * @param userId
     * @param providerId
     * @param providerUserId
     * @return the user connection details matching the parameter
     */
    UserSocialConnection findByUserIdAndProviderIdAndProviderUserId(String userId, String providerId, String providerUserId);
    
    /**
     * Retrieves provider id and provider user id of the users currently connected to the social network
     * @param userId
     * @param providerId
     * @param providerUserId
     * @return list of user connection details
     */
    List<UserSocialConnection> findByProviderIdAndProviderUserIdIn(String providerId, Collection<String> providerUserIds);
}
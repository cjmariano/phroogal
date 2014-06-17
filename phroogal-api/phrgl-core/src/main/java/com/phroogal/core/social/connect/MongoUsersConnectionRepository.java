package com.phroogal.core.social.connect;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bson.types.ObjectId;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;

import com.phroogal.core.domain.UserSocialConnection;
import com.phroogal.core.repository.UserSocialConnectionRepository;
import com.phroogal.core.utility.CollectionUtil;

/**
 * Implementation of {@link UsersConnectionRepository} from Spring Social API
 * @author c.j.mariano
 *
 */
public class MongoUsersConnectionRepository implements UsersConnectionRepository{
    
    private UserSocialConnectionRepository userSocialConnectionRepository;
    
    private ConnectionFactoryLocator connectionFactoryLocator;
 
    private TextEncryptor textEncryptor;
 
    private ConnectionSignUp connectionSignUp;
    
    public MongoUsersConnectionRepository(UserSocialConnectionRepository userSocialConnectionRepository,
    		ConnectionFactoryLocator connectionFactoryLocator, TextEncryptor textEncryptor){
        this.userSocialConnectionRepository = userSocialConnectionRepository;
        this.connectionFactoryLocator = connectionFactoryLocator;
        this.textEncryptor = textEncryptor;
    }
     
    /**
     * The command to execute to create a new local user profile in the event no user id could be mapped to a connection.
     * Allows for implicitly creating a user profile from connection data during a provider sign-in attempt.
     * Defaults to null, indicating explicit sign-up will be required to complete the provider sign-in attempt.
     * @see #findUserIdsWithConnection(Connection)
     */
    public void setConnectionSignUp(ConnectionSignUp connectionSignUp) {
        this.connectionSignUp = connectionSignUp;
    }
 
    public List<String> findUserIdsWithConnection(Connection<?> connection) {
        ConnectionKey key = connection.getKey();
        List<UserSocialConnection> userSocialConnectionList =
                this.userSocialConnectionRepository.findByProviderIdAndProviderUserId(key.getProviderId(), key.getProviderUserId());
        List<String> localUserIds = CollectionUtil.arrayList();
        for (UserSocialConnection userSocialConnection : userSocialConnectionList){
        	if (userSocialConnection.getUserId() == null) {
        		if (connectionSignUp != null) {
                    String newUserId = connectionSignUp.execute(connection);
                    userSocialConnection.setUserId(new ObjectId(newUserId));
                    userSocialConnectionRepository.save(userSocialConnection);
        		}
        	}
        	localUserIds.add(userSocialConnection.getUserId().toString());
        }
        return localUserIds;
    }
 
    public Set<String> findUserIdsConnectedTo(String providerId, Set<String> providerUserIds) {
        final Set<String> localUserIds = new HashSet<String>();
        List<UserSocialConnection> userSocialConnectionList =
                this.userSocialConnectionRepository.findByProviderIdAndProviderUserIdIn(providerId, providerUserIds);
        for (UserSocialConnection userSocialConnection : userSocialConnectionList){
            localUserIds.add(userSocialConnection.getUserId().toString());
        }
        return localUserIds;
    }
 
    public ConnectionRepository createConnectionRepository(String userId) {
        if (userId == null) {
            throw new IllegalArgumentException("userId cannot be null");
        }
        return new MongoConnectionRepository(userId, userSocialConnectionRepository, connectionFactoryLocator, textEncryptor);
    }
 
}

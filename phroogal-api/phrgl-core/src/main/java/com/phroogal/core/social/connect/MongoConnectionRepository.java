package com.phroogal.core.social.connect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.DuplicateConnectionException;
import org.springframework.social.connect.NoSuchConnectionException;
import org.springframework.social.connect.NotConnectedException;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.phroogal.core.domain.UserSocialConnection;
import com.phroogal.core.repository.UserSocialConnectionRepository;

public class MongoConnectionRepository implements ConnectionRepository {
	 
    private final String userId;
 
    private final UserSocialConnectionRepository userSocialConnectionRepository;
 
    private final ConnectionFactoryLocator connectionFactoryLocator;
 
    private final TextEncryptor textEncryptor;
 
    public MongoConnectionRepository(String userId, UserSocialConnectionRepository userSocialConnectionRepository,
    		ConnectionFactoryLocator connectionFactoryLocator, TextEncryptor textEncryptor) {
        this.userId = userId;
        this.userSocialConnectionRepository = userSocialConnectionRepository;
        this.connectionFactoryLocator = connectionFactoryLocator;
        this.textEncryptor = textEncryptor;
    }
 
    public MultiValueMap<String, Connection<?>> findAllConnections() {
        List<UserSocialConnection> userSocialConnectionList = this.userSocialConnectionRepository
                .findByUserId(userId);
 
        MultiValueMap<String, Connection<?>> connections = new LinkedMultiValueMap<String, Connection<?>>();
        Set<String> registeredProviderIds = connectionFactoryLocator.registeredProviderIds();
        for (String registeredProviderId : registeredProviderIds) {
            connections.put(registeredProviderId, Collections.<Connection<?>> emptyList());
        }
        for (UserSocialConnection userSocialConnection : userSocialConnectionList) {
            String providerId = userSocialConnection.getProviderId();
            if (connections.get(providerId).size() == 0) {
                connections.put(providerId, new LinkedList<Connection<?>>());
            }
            connections.add(providerId, buildConnection(userSocialConnection));
        }
        return connections;
    }
 
    public List<Connection<?>> findConnections(String providerId) {
        List<Connection<?>> resultList = new LinkedList<Connection<?>>();
        List<UserSocialConnection> userSocialConnectionList = this.userSocialConnectionRepository
                .findByUserIdAndProviderId(userId, providerId);
        for (UserSocialConnection userSocialConnection : userSocialConnectionList) {
            resultList.add(buildConnection(userSocialConnection));
        }
        return resultList;
    }
 
    @SuppressWarnings("unchecked")
    public <A> List<Connection<A>> findConnections(Class<A> apiType) {
        List<?> connections = findConnections(getProviderId(apiType));
        return (List<Connection<A>>) connections;
    }
 
    public MultiValueMap<String, Connection<?>> findConnectionsToUsers(MultiValueMap<String, String> providerUsers) {
        if (providerUsers == null || providerUsers.isEmpty()) {
            throw new IllegalArgumentException("Unable to execute find: no providerUsers provided");
        }
 
        MultiValueMap<String, Connection<?>> connectionsForUsers = new LinkedMultiValueMap<String, Connection<?>>();
         
        for (Iterator<Entry<String, List<String>>> it = providerUsers.entrySet().iterator(); it.hasNext();) {
            Entry<String, List<String>> entry = it.next();
            String providerId = entry.getKey();
            List<String> providerUserIds = entry.getValue();
            List<UserSocialConnection> userSocialConnections =
                    this.userSocialConnectionRepository.findByProviderIdAndProviderUserIdIn(providerId, providerUserIds);
            List<Connection<?>> connections = new ArrayList<Connection<?>>(providerUserIds.size());
            for (int i = 0; i < providerUserIds.size(); i++) {
                connections.add(null);
            }
            connectionsForUsers.put(providerId, connections);
 
            for (UserSocialConnection userSocialConnection : userSocialConnections) {
                String providerUserId = userSocialConnection.getProviderUserId();
                int connectionIndex = providerUserIds.indexOf(providerUserId);
                connections.set(connectionIndex, buildConnection(userSocialConnection));
            }
 
        }
        return connectionsForUsers;
    }
 
    public Connection<?> getConnection(ConnectionKey connectionKey) {
        UserSocialConnection userSocialConnection = this.userSocialConnectionRepository
                .findByUserIdAndProviderIdAndProviderUserId(userId, connectionKey.getProviderId(),
                        connectionKey.getProviderUserId());
        if (userSocialConnection != null) {
            return buildConnection(userSocialConnection);
        }
        throw new NoSuchConnectionException(connectionKey);
    }
 
    @SuppressWarnings("unchecked")
    public <A> Connection<A> getConnection(Class<A> apiType, String providerUserId) {
        String providerId = getProviderId(apiType);
        return (Connection<A>) getConnection(new ConnectionKey(providerId, providerUserId));
    }
 
    @SuppressWarnings("unchecked")
    public <A> Connection<A> getPrimaryConnection(Class<A> apiType) {
        String providerId = getProviderId(apiType);
        Connection<A> connection = (Connection<A>) findPrimaryConnection(providerId);
        if (connection == null) {
            throw new NotConnectedException(providerId);
        }
        return connection;
    }
 
    @SuppressWarnings("unchecked")
    public <A> Connection<A> findPrimaryConnection(Class<A> apiType) {
        String providerId = getProviderId(apiType);
        return (Connection<A>) findPrimaryConnection(providerId);
    }
 
    public void addConnection(Connection<?> connection) {
    	List<UserSocialConnection> userSocialConnectionListByProvider = this.userSocialConnectionRepository.findByProviderIdAndProviderUserId(connection.getKey().getProviderId(),
                        connection.getKey().getProviderUserId());
    	if (userSocialConnectionListByProvider.size() > 0) {
    		return;
    	}
    	
        UserSocialConnection userSocialConnection = this.userSocialConnectionRepository
                .findByUserIdAndProviderIdAndProviderUserId(userId, connection.getKey().getProviderId(),
                        connection.getKey().getProviderUserId());
        if (userSocialConnection == null) {
            ConnectionData data = connection.createData();
            userSocialConnection = new UserSocialConnection(userId, data.getProviderId(), data.getProviderUserId(), 0,
                    data.getDisplayName(), data.getProfileUrl(), data.getImageUrl(), encrypt(data.getAccessToken()),
                    encrypt(data.getSecret()), encrypt(data.getRefreshToken()), data.getExpireTime());
            this.userSocialConnectionRepository.save(userSocialConnection);
        } else {
            throw new DuplicateConnectionException(connection.getKey());
        }
    }
 
    public void updateConnection(Connection<?> connection) {
        ConnectionData data = connection.createData();
        UserSocialConnection userSocialConnection = this.userSocialConnectionRepository
                .findByUserIdAndProviderIdAndProviderUserId(userId, connection.getKey().getProviderId(), connection
                        .getKey().getProviderUserId());
        if (userSocialConnection != null) {
            userSocialConnection.setDisplayName(data.getDisplayName());
            userSocialConnection.setProfileUrl(data.getProfileUrl());
            userSocialConnection.setImageUrl(data.getImageUrl());
            userSocialConnection.setAccessToken(encrypt(data.getAccessToken()));
            userSocialConnection.setSecret(encrypt(data.getSecret()));
            userSocialConnection.setRefreshToken(encrypt(data.getRefreshToken()));
            userSocialConnection.setExpireTime(data.getExpireTime());
            this.userSocialConnectionRepository.save(userSocialConnection);
        }
    }
 
    public void removeConnections(String providerId) {
        List<UserSocialConnection> userSocialConnectionList = this.userSocialConnectionRepository
                .findByUserIdAndProviderId(userId, providerId);
        for (UserSocialConnection userSocialConnection : userSocialConnectionList) {
            this.userSocialConnectionRepository.delete(userSocialConnection);
        }
    }
 
    public void removeConnection(ConnectionKey connectionKey) {
        UserSocialConnection userSocialConnection = this.userSocialConnectionRepository
                .findByUserIdAndProviderIdAndProviderUserId(userId, connectionKey.getProviderId(), connectionKey.getProviderUserId());
        this.userSocialConnectionRepository.delete(userSocialConnection);
    }
 
    // internal helpers
 
    private Connection<?> buildConnection(UserSocialConnection userSocialConnection) {
        ConnectionData connectionData = new ConnectionData(userSocialConnection.getProviderId(),
                userSocialConnection.getProviderUserId(), userSocialConnection.getDisplayName(),
                userSocialConnection.getProfileUrl(), userSocialConnection.getImageUrl(),
                decrypt(userSocialConnection.getAccessToken()), decrypt(userSocialConnection.getSecret()),
                decrypt(userSocialConnection.getRefreshToken()), userSocialConnection.getExpireTime());
        ConnectionFactory<?> connectionFactory = this.connectionFactoryLocator.getConnectionFactory(connectionData
                .getProviderId());
        return connectionFactory.createConnection(connectionData);
    }
 
    private Connection<?> findPrimaryConnection(String providerId) {
        List<UserSocialConnection> userSocialConnectionList = this.userSocialConnectionRepository
                .findByUserIdAndProviderId(userId, providerId);
 
        return buildConnection(userSocialConnectionList.get(0));
    }
 
    private <A> String getProviderId(Class<A> apiType) {
        return connectionFactoryLocator.getConnectionFactory(apiType).getProviderId();
    }
 
    private String encrypt(String text) {
        return text != null ? textEncryptor.encrypt(text) : text;
    }
 
    private String decrypt(String encryptedText) {
        return encryptedText != null ? textEncryptor.decrypt(encryptedText) : encryptedText;
    }
 
}
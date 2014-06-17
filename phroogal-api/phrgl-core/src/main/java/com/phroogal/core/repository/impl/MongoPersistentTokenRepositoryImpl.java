package com.phroogal.core.repository.impl;

import java.util.Date;
import java.util.List;

import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import com.phroogal.core.domain.UserProfile;
import com.phroogal.core.domain.security.RememberMeToken;
import com.phroogal.core.repository.RememberMeTokenRepository;

public class MongoPersistentTokenRepositoryImpl implements PersistentTokenRepository {
	 
    private final RememberMeTokenRepository rememberMeTokenRepository;
     
    public MongoPersistentTokenRepositoryImpl(RememberMeTokenRepository rememberMeTokenRepository){
        this.rememberMeTokenRepository = rememberMeTokenRepository;
    }
     
    @Override
    public void createNewToken(PersistentRememberMeToken token) {
    	if (!token.getUsername().equals(UserProfile.ANONYMOUS_ROLE)) {
    		RememberMeToken newToken = new RememberMeToken(token);
            this.rememberMeTokenRepository.save(newToken);	
    	}
    }
 
    @Override
    public void updateToken(String series, String tokenValue, Date lastUsed) {
        RememberMeToken token = this.rememberMeTokenRepository.findBySeries(series);
        if (token != null){
            token.setTokenValue(tokenValue);
            token.setDate(lastUsed);
            this.rememberMeTokenRepository.save(token);
        }
 
    }
 
    @Override
    public PersistentRememberMeToken getTokenForSeries(String seriesId) {
    	PersistentRememberMeToken persistedToken =  null;
        RememberMeToken token = this.rememberMeTokenRepository.findBySeries(seriesId);
        if (token != null && !token.getUsername().equals(UserProfile.ANONYMOUS_ROLE)) {
        	persistedToken = new PersistentRememberMeToken(token.getUsername(), token.getSeries(), token.getTokenValue(), token.getDate());	
        }
        return persistedToken;
    }
 
    @Override
    public void removeUserTokens(String username) {
        List<RememberMeToken> tokens = this.rememberMeTokenRepository.findByUsername(username);
        this.rememberMeTokenRepository.delete(tokens);
    }
}

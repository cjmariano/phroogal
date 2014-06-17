package com.phroogal.core.test.helper;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.stereotype.Component;

import com.phroogal.core.domain.UserSocialConnection;
import com.phroogal.core.repository.UserSocialConnectionRepository;
import com.phroogal.core.social.SocialNetworkType;

@Component(value="socialNetworkConnectionHelper")
public class SocialNetworkConnectionHelper {
	
	@Autowired
	private UserSocialConnectionRepository userSocialConnectionRepository;
	
	@Value(value="${twitter.appId}")
	private String twitterAppId;
	
	@Value(value="${twitter.appSecret}")
	private String twitterAppSecret;
	
	@Value(value="${facebook.appId}")
	private String facebookAppId;
	
	@Value(value="${facebook.appSecret}")
	private String facebookAppSecret;
	
	private String accessToken;
	
	private String accessTokenSecret;
	
	/**
	 * Retrieves an instance of a twitter social network connection 
	 * @param twitterId - of the user
	 * @return
	 */
	public Twitter getTwitterConnection (String twitterId) {
		initTokens(twitterId, SocialNetworkType.TWITTER);
		return new TwitterTemplate(twitterAppId, twitterAppSecret, accessToken, accessTokenSecret);
	}
	
	/**
	 * Retrieves an instance of a facebook social network connection 
	 * @param facebookId - of the user
	 * @return
	 */
	public Facebook getFacebookConnection (String facebookId) {
		initTokens(facebookId, SocialNetworkType.FACEBOOK);
		return new FacebookTemplate(accessToken);
	}
	
	private void initTokens(String socialNetworkId, SocialNetworkType socialNetwork) {
		List<UserSocialConnection> userSocialConnectionList = userSocialConnectionRepository.findByProviderIdAndProviderUserId(socialNetwork.getId(), socialNetworkId);
		if ( ! CollectionUtils.isEmpty(userSocialConnectionList)) {
			UserSocialConnection userSocialConnection = userSocialConnectionList.get(0);
			accessToken = userSocialConnection .getAccessToken();
			accessTokenSecret = userSocialConnection.getSecret();	
		}
		Assert.assertFalse(StringUtils.isBlank(accessToken));
		Assert.assertFalse(StringUtils.isBlank(accessTokenSecret));
	}
}

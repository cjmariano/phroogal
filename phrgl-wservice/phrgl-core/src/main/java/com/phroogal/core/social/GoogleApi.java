/**
 * 
 */
package com.phroogal.core.social;


import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.springframework.social.google.api.Google;
import org.springframework.social.google.api.userinfo.*;
import org.springframework.stereotype.Component;

import com.phroogal.core.domain.GenderType;
import com.phroogal.core.domain.Location;


/**
 * Google implementation for {@link SocialNetwork}
 * @author Christopher Mariano
 *
 */
@Component("googleApi")
public class GoogleApi extends BaseSocialNetworkApi {

	@Override
	public SocialNetworkType getSocialNetworkType() {
		return SocialNetworkType.GOOGLE;
	}
	
	@Override
	protected List<String> returnContactUserId() {
		return null;
	}

	@Override
	public GenderType getGender() {
		Google google = (Google) getConnection().getApi();
		GoogleUserInfo userInfo = google.userOperations().getUserInfo();
		return GenderType.get(userInfo.getGender());
	}
	
	@Override
	public String getBio() {
		Google google = (Google) getConnection().getApi();
		return google.plusOperations().getGoogleProfile().getAboutMe();
	}
	
	@Override
	/*
	 * As of this writing, Google Plus API does not provide comprehensive information about a user location. 
	 * For now, we will just be populating the display name
	 */
	public Location getLocation() {
		Google google = (Google) getConnection().getApi();
		Map<String, Boolean> locations = google.plusOperations().getGoogleProfile().getPlacesLived();
		for (Map.Entry<String, Boolean> each : locations.entrySet()) {
			if (each.getValue()) {
				Location location = new Location();
				location.setDisplayName(each.getKey());
				return location;
			}
		}
		return null;
	}

	@Override
	public DateTime getDateOfBirth() {
		// TODO Auto-generated method stub
		return null;
	}
}

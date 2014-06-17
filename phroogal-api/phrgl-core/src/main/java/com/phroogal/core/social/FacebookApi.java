/**
 * 
 */
package com.phroogal.core.social;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.FqlResultMapper;
import org.springframework.stereotype.Component;

import com.phroogal.core.domain.GenderType;
import com.phroogal.core.domain.Location;


/**
 * Facebook implementation for {@link SocialNetwork}
 * @author Christopher Mariano
 *
 */
@Component("facebookApi")
public class FacebookApi extends BaseSocialNetworkApi {
	
	private final String PROFILE_PIC_LARGE = "large";
	
	private final String PROFILE_PIC_SMALL = "small";
	
	@Override
	public SocialNetworkType getSocialNetworkType() {
		return SocialNetworkType.FACEBOOK;
	}
	
	@Override
	protected List<String> returnContactUserId() {
		Facebook facebook = (Facebook) getConnection().getApi();
		return  facebook.friendOperations().getFriendIds();
	}

	@Override
	public GenderType getGender() {
		Facebook facebook = (Facebook) getConnection().getApi();
		String gender = facebook.userOperations().getUserProfile().getGender();
		return GenderType.get(gender);
	}
	
	@Override
	public String getProfileSmallPictureUrl() {
		return constructProfilePictureUrl(PROFILE_PIC_SMALL);
	}
	
	@Override
	public String getProfilePictureUrl() {
		return constructProfilePictureUrl(PROFILE_PIC_LARGE);
	}
	
	@Override
	public String getBio() {
		Facebook facebook = (Facebook) getConnection().getApi();
		return facebook.userOperations().getUserProfile().getBio();
	}

	@Override
	public Location getLocation() {
		Facebook facebook = (Facebook) getConnection().getApi();
		FqlResultMapper<Location> mapper = new FacebookLocationResult();
		List<Location> results = facebook.fqlOperations().query("SELECT current_location FROM user WHERE uid=me()", mapper);
		return results.size() > 0 ? results.get(0) : new Location();
	}

	@Override
	public DateTime getDateOfBirth() {
		DateTime dateOfBirth = null;
		Facebook facebook = (Facebook) getConnection().getApi();
		String strBirthdate = facebook.userOperations().getUserProfile().getBirthday();
		if (StringUtils.isNotEmpty(strBirthdate)) {
			DateTimeFormatter formatter = DateTimeFormat.forPattern("MM/dd/yyyy");	
			dateOfBirth = formatter.parseDateTime(strBirthdate);			
		}
		return dateOfBirth;
	}
	
	private String constructProfilePictureUrl(String size) {
		String providerUserId = getConnection().createData().getProviderUserId();
		StringBuffer sb = new StringBuffer("http://graph.facebook.com/");
		sb.append(providerUserId);
		sb.append("/picture?type=");
		sb.append(size);
		return sb.toString();
	}
}

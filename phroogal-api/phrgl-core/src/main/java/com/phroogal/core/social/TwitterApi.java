/**
 * 
 */
package com.phroogal.core.social;

import java.util.List;

import org.joda.time.DateTime;
import org.springframework.social.twitter.api.CursoredList;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Component;

import com.phroogal.core.domain.GenderType;
import com.phroogal.core.domain.Location;
import com.phroogal.core.utility.CollectionUtil;


/**
 * Twitter implementation for {@link SocialNetwork}
 * @author Christopher Mariano
 *
 */
@Component("twitterApi")
public class TwitterApi extends BaseSocialNetworkApi {
	
	
	@Override
	public SocialNetworkType getSocialNetworkType() {
		return SocialNetworkType.TWITTER;
	}

	/**
	 * As of this writing,  The Twitter API does not give access to private information such as email addresses.
	 */
	@Override
	public String getPrimaryEmail() {
		return null;
	}
	
	@Override
	public String getProfileSmallPictureUrl() {
		Twitter twitter = (Twitter) getConnection().getApi();
		return twitter.userOperations().getUserProfile().getProfileImageUrl();
	}
	
	@Override
	public String getProfilePictureUrl() {
		return extractOriginalSizeImg(getProfileSmallPictureUrl());
	}

	@Override
	public String getBio() {
		Twitter twitter = (Twitter) getConnection().getApi();
		return twitter.userOperations().getUserProfile().getDescription();
	}

	@Override
	public Location getLocation() {
		Twitter twitter = (Twitter) getConnection().getApi();
		String locationDisplayName = twitter.userOperations().getUserProfile().getLocation();
		return generateLocation(locationDisplayName);
	}

	private Location generateLocation(String locationDisplayName) {
		Location location = new Location();
		location.setDisplayName(locationDisplayName);
		return location;
	}

	/**
	 * As of this writing,  The Twitter API does not have settings for birthdate.
	 */
	@Override
	public DateTime getDateOfBirth() {
		return null;
	}

	/**
	 * As of this writing,  The Twitter API does not have settings for gender.
	 */
	@Override
	public GenderType getGender() {
		return null;
	}

	@Override
	protected List<String> returnContactUserId() {
		Twitter twitter = (Twitter) getConnection().getApi();
		CursoredList<Long> friendsList = twitter.friendOperations().getFriendIds();
		return convertToListOfString(friendsList);
	}

	/**
	 * As per twitter documentation, (https://dev.twitter.com/docs/user-profile-images-and-banners)
	 * Suffixes on the image file determines the returned size. Removing the suffix returns the original size.
	 * @param profileImageUrl
	 * @return original sized image
	 */
	private String extractOriginalSizeImg(String profileImageUrl) {
		return profileImageUrl.replace("_normal", "");
	}
	
	//TODO : Consider Spring conversion -cjm 
	private List<String> convertToListOfString(CursoredList<Long> friendsList) {
		List<String> strFriendsList = CollectionUtil.arrayList();
		for (Long follower : friendsList) {
			strFriendsList.add(String.valueOf(follower));
		}
		return strFriendsList;
	}
}

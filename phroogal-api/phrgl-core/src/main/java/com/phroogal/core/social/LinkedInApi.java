/**
 * 
 */
package com.phroogal.core.social;

import static com.phroogal.core.social.SocialNetworkType.LINKEDIN;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.json.simple.JSONArray;
import org.springframework.social.linkedin.api.LinkedIn;
import org.springframework.social.linkedin.api.LinkedInDate;
import org.springframework.social.linkedin.api.LinkedInProfile;
import org.springframework.social.linkedin.api.Location;
import org.springframework.stereotype.Component;

import com.phroogal.core.domain.GenderType;
import com.phroogal.core.utility.CollectionUtil;
import com.phroogal.core.utility.HttpRestWebServiceInvoker;
import com.phroogal.core.utility.JsonUtility;


/**
 * LinkedIn implementation for {@link SocialNetwork}
 * @author Christopher Mariano
 *
 */
@Component("linkedInApi")
public class LinkedInApi extends BaseSocialNetworkApi {
	
	private JsonUtility jsonUtility = new JsonUtility();
	
	@Override
	public SocialNetworkType getSocialNetworkType() {
		return LINKEDIN;
	}
	
	@Override
	protected List<String> returnContactUserId() {
		LinkedIn linkedin = (LinkedIn) getConnection().getApi();
		List<String> contacts = CollectionUtil.arrayList();
		List<LinkedInProfile> profiles = linkedin.connectionOperations().getConnections();
		
		for (LinkedInProfile linkedInProfile : profiles) {
			contacts.add(linkedInProfile.getId());
		}
		return contacts;
	}
	
	@Override
	public String getProfilePictureUrl() {
		try {
			String oauthToken = getConnection().createData().getAccessToken();
			String response = HttpRestWebServiceInvoker.doGetRequest("https", "api.linkedin.com", "/v1/people/~/picture-urls::(original)", buildGenericParameters(oauthToken));
			if (response != null) {
				JSONArray values = (JSONArray) jsonUtility.getListByKey(response, "values");
				return  values.get(0).toString();
			}	
		} catch (Exception e) {
			//Silently ignore
			e.printStackTrace();
		}
		return StringUtils.EMPTY;
	}
	
	@Override
	public String getBio() {
		LinkedIn linkedin = (LinkedIn) getConnection().getApi();
		return linkedin.profileOperations().getUserProfile().getHeadline();
	}

	@Override
	public com.phroogal.core.domain.Location getLocation() {
		LinkedIn linkedin = (LinkedIn) getConnection().getApi();
		Location location = linkedin.profileOperations().getUserProfileFull().getLocation();
		return generateLocation(location);
	}

	/*
	 * As of this writing, LinkedIn API does not provide comprehensive information about a user location. 
	 * For now, we will just be populating the display name
	 */
	private com.phroogal.core.domain.Location generateLocation(Location linkedInLocation) {
		com.phroogal.core.domain.Location location = new com.phroogal.core.domain.Location();
		if (linkedInLocation != null) {
			location.setDisplayName(linkedInLocation.getName());	
		}
		return location;
	}

	@Override
	public GenderType getGender() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DateTime getDateOfBirth() {
		LinkedIn linkedin = (LinkedIn) getConnection().getApi();
		LinkedInDate dob = linkedin.profileOperations().getUserProfileFull().getDateOfBirth();
		return dob != null ? new DateTime(dob.getYear(), dob.getMonth(), dob.getDay(), 0, 0) : null;
	}
	
	private Map<String, String> buildGenericParameters(String oauthToken) {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("oauth2_access_token", oauthToken);
		parameters.put("format", "json");
		return parameters;
	}
}

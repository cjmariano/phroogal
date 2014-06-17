package com.phroogal.core.social;

import java.util.HashMap;

import org.springframework.social.facebook.api.FqlResult;
import org.springframework.social.facebook.api.FqlResultMapper;

import com.phroogal.core.domain.Location;

public class FacebookLocationResult implements FqlResultMapper<Location> {

	@Override
	@SuppressWarnings("unchecked")
	public Location mapObject(FqlResult objectValues) {
		Location location = new Location();
		HashMap<String,Object> result = (HashMap<String, Object>) objectValues.getObject("current_location");
		if (result != null) {
			location.setCity(result.get("city").toString());
			location.setState(result.get("state").toString());
			location.setCountry(result.get("country").toString());
			location.setLatitude((Double) result.get("latitude"));
			location.setLongtitude((Double) result.get("longitude"));
			location.setDisplayName(getdisplayName(result));	
		}
		return location;
	}

	private String getdisplayName(HashMap<String, Object> result) {
		StringBuffer sb = new StringBuffer();
		sb.append(result.get("city"));
		sb.append(" ");
		sb.append(result.get("state"));
		sb.append(", ");
		sb.append(result.get("country"));
		return sb.toString();
	}
}

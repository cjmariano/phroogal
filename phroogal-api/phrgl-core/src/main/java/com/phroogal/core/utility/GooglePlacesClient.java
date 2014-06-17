package com.phroogal.core.utility;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.phroogal.core.domain.Location;
import com.phroogal.core.domain.LocationIndex;

@Component("googlePlacesClient")
public class GooglePlacesClient {

	@Value("${google.cse.apiKey}")
	private String googleApiKey;
	
	private JsonUtility jsonUtility = new JsonUtility();

	/**
	 * Gets a list of instances of {@link LocationIndex}, that matches the keyword specified.
	 * @param types of location to be returned (i.e. (cities) would return lists of cities)
	 * refer to list of google supported types <a href=https://developers.google.com/places/documentation/supported_types?hl=fr>here</a>
	 * @param keyword used to filter locations returned 
	 * @return list of {@link LocationIndex}
	 */
	public List<LocationIndex> queryLocationsByKeyword(String types, String keyword) {
		List<LocationIndex> locations = CollectionUtil.arrayList();
		String response = HttpRestWebServiceInvoker.doGetRequest("https", "maps.googleapis.com", "/maps/api/place/autocomplete/json", buildGetLocationsParameters(types, keyword));
		if (response != null) {
			List<Object> predictions = jsonUtility.getListByKey(response, "predictions");
			return populateLocationSuggestions(locations, predictions);
		}
		return locations;
	}

	/**
	 * Returns an instance of {@link Location} given the locationRef specified by google places
	 * @param locationRef provided by google places api that points to a location
	 * @return an instance of {@link Location}, null if there are no matches
	 */
	public Location getLocationByRef(String locationRef) {
		Location location = null;
		String response = HttpRestWebServiceInvoker.doGetRequest("https", "maps.googleapis.com", "/maps/api/place/details/json", buildGetLocationByRefParameters(locationRef));
		String status =  (String) jsonUtility.getValueByKey(response, "status");
		if (response != null && ! status.equals("INVALID_REQUEST") ) {
			location = new Location();
			location.setLocationRef(locationRef);
			String result = ((JSONObject) jsonUtility.getValueByKey(response, "result")).toJSONString();
			populateAddressComponents(location, result);
			populateAddressCoordinates(location, result);
		}
		return location;
	}

	private List<LocationIndex> populateLocationSuggestions(List<LocationIndex> locations , List<Object> predictions) {
		for (Object prediction : predictions) {
			LocationIndex location = new LocationIndex();
			location.setName((String) ((JSONObject)prediction).get("description"));
			location.setReference((String) ((JSONObject)prediction).get("reference"));
			locations.add(location);
		}
		return locations;
	}

	private void populateAddressCoordinates(Location location, String result) {
		JSONObject geometry = (JSONObject) jsonUtility.getValueByKey(result, "geometry");
		JSONObject geoLocation =  (JSONObject) jsonUtility.getValueByKey(geometry.toJSONString(), "location");
		location.setLatitude((Double)jsonUtility.getValueByKey(geoLocation.toJSONString(), "lat"));
		location.setLongtitude((Double)jsonUtility.getValueByKey(geoLocation.toJSONString(), "lng"));
	}

	private void populateAddressComponents(Location location, String result) {
		String formattedAddress = (String) jsonUtility.getValueByKey(result, "formatted_address");
		location.setDisplayName(formattedAddress);
		List<Object> addressComponents = jsonUtility.getListByKey(result, "address_components");
		for (Object addressJson : addressComponents) {
			String address = ((JSONObject) addressJson).toJSONString();
			List<Object> types = jsonUtility.getListByKey(address, "types");
			for (Object type : types) {
				if (type.equals("locality")) {
					String city = (String) jsonUtility.getValueByKey(address, "long_name");
					location.setCity(city);
					break;
				}
				if (type.equals("administrative_area_level_1")) {
					String state = (String) jsonUtility.getValueByKey(address, "short_name");
					location.setState(state);
					break;
				}
				if (type.equals("country")) {
					String country = (String) jsonUtility.getValueByKey(address, "short_name");
					location.setCountry(country);
					break;
				}
			}
		}
	}

	private Map<String, String> buildGetLocationsParameters(String types, String keyword) {
		Map<String, String> parameters = initializeRequestParameters();
		parameters.put("types", types);
		parameters.put("input", keyword);
		return parameters;
	}
	
	private Map<String, String> buildGetLocationByRefParameters(String locationRef) {
		Map<String, String> parameters = initializeRequestParameters();
		parameters.put("reference", locationRef);
		return parameters;
	}

	private Map<String, String> initializeRequestParameters() {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("sensor", "true");
		parameters.put("key", googleApiKey);
		return parameters;
	}
}

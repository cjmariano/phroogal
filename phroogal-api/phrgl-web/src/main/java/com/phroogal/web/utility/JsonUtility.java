/**
 * 
 */
package com.phroogal.web.utility;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Utility class that provides convenience methods for JSON functionalities
 * @author Christopher Mariano
 *
 */
public final class JsonUtility {
	
	/**
	 * Returns the value in a given JSON based on the given key
	 * @param jsonString - data to be evaluated
	 * @param key - use to match values with
	 * @return the value corresponding to the given key
	 */
	public Object getValueByKey(String jsonString, String key) {
		return getGenericByKey(new JSONObject(), jsonString, key);
	}
	
	/**
	 * Returns is list of {@link JSONObject} based on the given key
	 * @param jsonString - data to be evaluated
	 * @param key - use to match values with
	 * @return the value corresponding to the given key
	 */
	public List<Object> getListByKey(String jsonString, String key) {
		List<Object> result = getGenericByKey(new ArrayList<Object>(), jsonString, key);
		return result == null ? new ArrayList<Object>() : result;
	}
	
	@SuppressWarnings("unchecked")
	private <T> T getGenericByKey(T type, String jsonString, String key) {
		JSONParser parser = new JSONParser();
		if (jsonString != null) {
			try {
				JSONObject jsonObject = (JSONObject) parser.parse(jsonString);
				return (T) jsonObject.get(key);
			} catch (ParseException e) {
				//Ignore and just return null
				e.printStackTrace();
				return null;
			}	
		}
		return null;
	}
}

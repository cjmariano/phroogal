package com.phroogal.core.utility;

import java.util.List;

import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.Test;

public class JsonUtilityTest {

	@Test
	public void testGetValueByKey() {
		JsonUtility utility = new JsonUtility();
		String json = "{\"name\":\"John\"}";
		String name = (String) utility.getValueByKey(json, "name");
		Assert.assertEquals("John", name);
	}
	
	@Test
	public void testGetListByKey() {
		JsonUtility utility = new JsonUtility();
		String json = "{\"persons\" : [{\"name\":\"John\"}, {\"name\":\"Mary\"}]}";
		List<Object> persons = utility.getListByKey(json, "persons");
		Assert.assertEquals(persons.size(), 2);
		
		Assert.assertEquals("John", ((JSONObject)persons.get(0)).get("name"));
		Assert.assertEquals("Mary", ((JSONObject)persons.get(1)).get("name"));
	}
}

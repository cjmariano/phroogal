package com.phroogal.core.utility;

import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;

public class HttpRestWebServiceInvokerTest {

	@Test
	@SuppressWarnings("serial")
	public void testDoGetRequest() {
		String response = HttpRestWebServiceInvoker.doGetRequest("http", "www.google.com", "/search", new HashMap<String, String>(){{put("q", "java");}});
		Assert.assertNotNull(response);
	}

}

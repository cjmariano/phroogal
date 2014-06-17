/**
 * 
 */
package com.phroogal.core.utility;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * Simple REST web service invoker to be used for 3rd party APIs
 * @author Christopher Mariano
 *
 */
public final class HttpRestWebServiceInvoker {
	
	/**
	 * Convenience method that performs a get request on a web service uri
	 * <br>Sample usage<br>
	 * <code>String response = HttpRestWebServiceInvoker.doGetRequest("https", "maps.googleapis.com", "/maps/api/place/details/json", parameterMap);</code>
	 * 
	 * @param scheme to be used i.e. htpp / https
	 * @param host - server where the service resides
	 * @param path - uri resource location
	 * @param parameters - url parameters to provide to the service
	 * @return
	 */
	public static String doGetRequest(String scheme, String host, String path, Map<String, String> parameters) {
		String result = StringUtils.EMPTY;
		try {
			HttpClient client = new DefaultHttpClient();
			URIBuilder builder = generateUriBuilder(scheme, host, path, parameters);
			HttpUriRequest request = new HttpGet(builder.build());
			HttpResponse execute = client.execute(request);
			result = EntityUtils.toString(execute.getEntity()); 
		} catch (Exception e) {
			e.printStackTrace();
			return result;
		}
		return result;
	}

	private static URIBuilder generateUriBuilder(String scheme, String host, String path, Map<String, String> parameters) {
		URIBuilder builder = new URIBuilder().setScheme(scheme).setHost(host).setPath(path);
		for (Map.Entry<String, String> entry : parameters.entrySet()) {
			builder.addParameter(entry.getKey(), entry.getValue());
		}
		return builder;
	}
}

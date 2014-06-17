/**
 * 
 */
package com.phroogal.core.notification;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds information on the context to be used for generating the content of a mail message
 * @author Christopher Mariano
 *
 */
public class MailContentContext {

	private Map<String, String> context = new HashMap<String, String>();
	
	public void addParam(String name, String value) {
		context.put(name, value);
	}
	
	public Map<String, String> getContext() {
		return context;
	}
}

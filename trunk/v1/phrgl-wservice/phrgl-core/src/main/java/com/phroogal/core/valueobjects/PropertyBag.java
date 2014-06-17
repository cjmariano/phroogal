/**
 * 
 */
package com.phroogal.core.valueobjects;

/**
 * Serves as container for key value pairs 
 * @author Christopher Mariano
 *
 */
public class PropertyBag {

private String property;
	
	private Object value;

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object object) {
		this.value = object;
	}
}

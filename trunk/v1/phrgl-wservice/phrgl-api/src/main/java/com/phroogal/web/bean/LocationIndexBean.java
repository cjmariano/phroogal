package com.phroogal.web.bean;

/**
 * Contains indexed primary information about a given location.
 * @author c.j.mariano
 *
 */
public class LocationIndexBean {

	/*
	 * Name of the location
	 */
	private String name;
	
	/*
	 * Reference key which contains details for the location 
	 */
	private String reference;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}
}

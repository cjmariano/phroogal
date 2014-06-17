/**
 * 
 */
package com.phroogal.web.bean;


/**
 * Holds information about a location.
 * @author Christopher Mariano
 *
 */
public class LocationBean {

	private String locationRef;
	
	private String displayName;
	
	private String city;
	
	private String state;
	
	private String country;
	
	private Double longtitude;
	
	private Double latitude;

	public String getLocationRef() {
		return locationRef;
	}

	public void setLocationRef(String locationRef) {
		this.locationRef = locationRef;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Double getLongtitude() {
		return longtitude;
	}

	public void setLongtitude(Double longtitude) {
		this.longtitude = longtitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
}

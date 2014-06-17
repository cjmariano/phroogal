/**
 * 
 */
package com.phroogal.web.bean;

import com.phroogal.core.domain.UserProfile;

/**
 * Data Transfer object for {@link UserProfile}
 * @author Christopher Mariano
 *
 */
public class BrandProfileBean {
	
	private String brandName;
	
	private String brandShortDescription;
	
	private String brandLongDescription;
	
	private String[] brandTags;	
	
	private String brandProfilePictureUrl;
	
	private String brandUrl;
	

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getBrandShortDescription() {
		return brandShortDescription;
	}

	public void setBrandShortDescription(String brandShortDescription) {
		this.brandShortDescription = brandShortDescription;
	}

	public String getBrandLongDescription() {
		return brandLongDescription;
	}

	public void setBrandLongDescription(String brandLongDescription) {
		this.brandLongDescription = brandLongDescription;
	}

	public String[] getBrandTags() {
		return brandTags;
	}

	public void setBrandTags(String[] brandTags) {
		this.brandTags = brandTags;
	}

	public String getBrandProfilePictureUrl() {
		return brandProfilePictureUrl;
	}

	public void setBrandProfilePictureUrl(String brandProfilePictureUrl) {
		this.brandProfilePictureUrl = brandProfilePictureUrl;
	}

	public String getBrandUrl() {
		return brandUrl;
	}

	public void setBrandUrl(String brandUrl) {
		this.brandUrl = brandUrl;
	}	
	
}

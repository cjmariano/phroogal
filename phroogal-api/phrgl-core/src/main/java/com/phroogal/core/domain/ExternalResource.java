/**
 * 
 */
package com.phroogal.core.domain;

/**
 * Holds information on that is used for searching the external resources
 * @author Christopher Mariano
 *
 */
public class ExternalResource {
	
	private String title;
	
	private String source;
	
	private String snippet;
	
	private String externalLink;
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getExternalLink() {
		return externalLink;
	}

	public void setExternalLink(String externalLink) {
		this.externalLink = externalLink;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSnippet() {
		return snippet;
	}

	public void setSnippet(String snippet) {
		this.snippet = snippet;
	}
}

package com.phroogal.web.bean;


/**
 * Basic bean shared by Posts documents
 * @author Christopher Mariano
 *
 */
public class PostBean<ID> extends PersistentBean<ID>  {

	private String content;
	
	private UserPreviewBean postBy;
	
	private String createdOn;
	
	private String modifiedOn;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	public UserPreviewBean getPostBy() {
		return postBy;
	}

	public void setPostBy(UserPreviewBean postBy) {
		this.postBy = postBy;
	}

	public String getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(String modifiedOn) {
		this.modifiedOn = modifiedOn;
	}
}

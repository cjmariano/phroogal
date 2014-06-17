/**
 * 
 */
package com.phroogal.web.bean;

/**
 * Bean to hold documents of type Tags.
 * @author Christopher Mariano
 *
 */
public class TagBean {
	
	private String id;
	
	private String name;
	
	private long totalNumQuestionsTagged;
	
	private long totalNumViewsForQuestionsTagged;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public long getTotalNumQuestionsTagged() {
		return totalNumQuestionsTagged;
	}

	public void setTotalNumQuestionsTagged(long totalNumQuestionsTagged) {
		this.totalNumQuestionsTagged = totalNumQuestionsTagged;
	}
	
	public long getTotalNumViewsForQuestionsTagged() {
		return totalNumViewsForQuestionsTagged;
	}
	 
	public void setTotalNumViewsForQuestionsTagged(long totalNumViewsForQuestionsTagged) {
		this.totalNumViewsForQuestionsTagged = totalNumViewsForQuestionsTagged;
	}
}

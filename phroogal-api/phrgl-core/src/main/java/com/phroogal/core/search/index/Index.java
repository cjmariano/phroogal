package com.phroogal.core.search.index;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;

import com.phroogal.core.domain.Persistent;



/**
 * Models indices that are stored on the index repository for search
 * 
 */
public class Index implements Persistent<String> {
	
	@Id
	@Field
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}

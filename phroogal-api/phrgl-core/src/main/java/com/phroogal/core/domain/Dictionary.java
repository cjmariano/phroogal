/**
 * 
 */
package com.phroogal.core.domain;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 
 * Domain representation for Terms in Dictionary.
 * @author Christopher Mariano
 *
 */
@Document(collection = ApplicationConstants.COLLECTION_DICTIONARY)
public class Dictionary implements Persistent<ObjectId> {

	@Id
	private ObjectId id;
	
	private String term;
	
	private String definition;
	
	private List<String> tags;

	@Override
	public ObjectId getId() {
		return id;
	}

	@Override
	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public String getDefinition() {
		return definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}
}

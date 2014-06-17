/**
 * 
 */
package com.phroogal.core.domain;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = ApplicationConstants.COLLECTION_COLLEGES)
public class College implements Persistent<ObjectId> {
	
	@Id
	private ObjectId id;
	
	@Indexed
	private String name;
	
	@Override
	public ObjectId getId() {
		return id;
	}

	@Override
	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}

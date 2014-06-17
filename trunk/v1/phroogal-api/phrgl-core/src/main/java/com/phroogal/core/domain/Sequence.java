package com.phroogal.core.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Sequence for auto incrementing a custom id in a collection  
 * @author Christopher Mariano
 *
 */
@Document(collection = ApplicationConstants.COLLECTION_SEQUENCES)
public class Sequence {

	@Transient
	public static final Long INITIAL_SEQ_VALUE = 1000L;
	
	@Id
	private String collection;
	
	private Long lastValue = INITIAL_SEQ_VALUE;

	public String getCollection() {
		return collection;
	}

	public void setCollection(String collection) {
		this.collection = collection;
	}

	public Long getLastValue() {
		return lastValue;
	}

	public void setLastValue(Long lastValue) {
		this.lastValue = lastValue;
	}
}

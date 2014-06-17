/**
 * 
 */
package com.phroogal.core.domain;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Extension of {@link Tag} that is user-defined
 * @author Christopher Mariano
 *
 */
@Document(collection = ApplicationConstants.COLLECTION_USER_TAGS)
public class UserTag extends Tag {
	
	@Indexed
	private ObjectId userId;

	public ObjectId getUserId() {
		return userId;
	}

	public void setUserId(ObjectId userId) {
		this.userId = userId;
	}
}

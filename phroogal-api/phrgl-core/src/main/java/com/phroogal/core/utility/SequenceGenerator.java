/**
 * 
 */
package com.phroogal.core.utility;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.phroogal.core.domain.Sequence;

/**
 * Provides a next generated sequence for a given collection
 * 
 * @author Christopher Mariano
 * 
 */
@Service
public class SequenceGenerator {

	@Autowired
	private MongoOperations mongo;

	public Long getNextSequence(String collectionName) {
		Sequence sequence = mongo.findAndModify(query(where("_id").is(collectionName)),
				new Update().inc("lastValue", 1), options().returnNew(true),
				Sequence.class);
		if (sequence == null) {
			sequence = new Sequence();
			sequence.setCollection(collectionName);
			mongo.save(sequence);
		}
		return sequence.getLastValue();
	}
}

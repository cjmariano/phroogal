package com.phroogal.core.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Base class for classes implementing Spring Data Mongo repository
 * @author c.j.mariano
 *
 */
public interface BaseMongoRepository<T>  extends MongoRepository<T, ObjectId>{

}

/**
 * 
 */
package com.phroogal.core.mongodb;

import org.bson.types.ObjectId;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoPersistentEntity;
import org.springframework.data.mongodb.core.mapping.MongoPersistentProperty;

/**
 * @author Christopher Mariano
 *
 */
public class CustomMongoConverter extends MappingMongoConverter {
	
	
    public CustomMongoConverter(MongoDbFactory mongoDbFactory, MappingContext<? extends MongoPersistentEntity<?>, MongoPersistentProperty> mappingContext) {
        super(mongoDbFactory, mappingContext);
        conversionService.addConverter(new Converter<String, ObjectId>() {
            @Override
            public ObjectId convert(String source) {
                throw new UnsupportedOperationException("Conversion from String to ObjectId supressed");
            }
        });
    }
}

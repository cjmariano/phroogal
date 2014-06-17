/**
 * 
 */
package com.phroogal.core.test.helper;

import org.springframework.data.mongodb.core.MongoTemplate;

import com.phroogal.core.utility.SolrUtility;

/**
 * Provides cleanup functionality on repositories
 * @author Christopher Mariano
 *
 */
public final class RepositoryCleanupHelper {

	/**
     * Drops the collection if the collection does already exists
     */
    public static void dropCollection(MongoTemplate mongoTemplate) {
    	for (String collections : mongoTemplate.getCollectionNames()) {
    		if (! (collections.contains("system") || collections.contains("local"))) {
    			mongoTemplate.dropCollection(collections);	
    		}
		} 
    }
    
    public static void deleteAllSolrData(SolrUtility solrUtility) {
        try {
        	  solrUtility.deleteAllIndex();
        } catch (Exception e) {
          e.printStackTrace();
        }	
    }
}

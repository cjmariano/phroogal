/**
 * 
 */
package com.phroogal.core.utility;

import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.phroogal.core.domain.Persistent;
import com.phroogal.core.repository.BaseMongoRepository;
import com.phroogal.core.service.Service;

/**
 * Helper class to loop through all the elements in a collection
 * @author Christopher Mariano
 *
 */
@SuppressWarnings("rawtypes")
public abstract class EntityIteratorHelper<T extends Persistent<ObjectId>, REPO extends BaseMongoRepository<T>,SERVICE extends Service > {
	
	protected abstract boolean processEach(T domain);
	
	private static final int BATCH_SIZE = 10;
	
	private REPO repository;
	
	private SERVICE service;
	
	private long processedCount;
	
	private long errorsCount;
	
	private long entityProcessed;
	
	private Map<String, String> errorsOnObjectIds = CollectionUtil.hashMap();
	
	public EntityIteratorHelper(REPO repository, SERVICE service) {
		this.repository = repository;
		this.service = service;
	}
	
	public void startAndContinueOnErrors() {
		start(true);
	}
	
	public void startAndBreakOnErrors() {
		start(false);
	}

	private void start(boolean continueOnError) {
		Long count = service.count();
		System.out.println("Processing " + count + " items");
		for (int i = 0;  true ; i++) {
			Page<T> results = repository.findAll(new PageRequest(i, BATCH_SIZE));
			for (T domain : results) {
				try {
					System.out.println("Processing " + processedCount + " out of "+ count);
					if (processEach(domain)) {
						System.out.println("Processed entity with id[" + domain.getId().toString() + "]");
						entityProcessed ++;
					};
					processedCount ++;
				} catch (Exception e) {
					logError(domain, e);
					if (continueOnError) {
						continue;
					}
				}
			}
			
			if (results.getContent().size() != BATCH_SIZE) {
				break;
			}
		}
		logReport();
	}

	private void logError(T domain, Exception e) {
		e.printStackTrace();
		System.out.println("Error processing entity with id[" + domain.getId().toString() + "]");
		errorsOnObjectIds.put(domain.getId().toString(), e.getMessage());
		processedCount ++;
		errorsCount ++;
	}
	
	private void logReport() {
		System.out.println("Processed [" + processedCount + "] items");
		System.out.println("Error on [" + errorsCount + "] items with id:");
		for (Map.Entry<String, String> entry : errorsOnObjectIds.entrySet()) {
			System.out.println(" " + entry.getKey() + " - " + entry.getValue());
		}
		System.out.println("Executed logic on [" + entityProcessed + "] entities");
	}
}

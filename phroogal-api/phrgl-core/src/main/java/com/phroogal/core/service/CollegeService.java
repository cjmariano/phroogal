/**
 * 
 */
package com.phroogal.core.service;



import java.util.List;

import org.bson.types.ObjectId;

import com.phroogal.core.domain.College;


public interface CollegeService extends Service<College, ObjectId> {

public List<College> findByNameContaining(String keyword);	
	
}

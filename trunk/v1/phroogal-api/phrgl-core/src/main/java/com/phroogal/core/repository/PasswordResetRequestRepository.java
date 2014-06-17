/**
 * 
 */
package com.phroogal.core.repository;

import com.phroogal.core.domain.Answer;
import com.phroogal.core.domain.security.PasswordResetRequest;

/**
 * Repository for {@link Answer} data
 * 
 * @author Christopher Mariano
 * 
 */
public interface PasswordResetRequestRepository extends BaseMongoRepository<PasswordResetRequest>{
	
}

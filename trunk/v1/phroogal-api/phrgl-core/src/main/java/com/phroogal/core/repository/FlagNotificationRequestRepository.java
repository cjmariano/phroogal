/**
 * 
 */
package com.phroogal.core.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;

import com.phroogal.core.domain.FlagNotificationRequest;
import com.phroogal.core.domain.FlagNotificationStatusType;
import com.phroogal.core.domain.PostType;

/**
 * Repository for {@link FlagNotificationRequest} data
 * 
 * @author Christopher Mariano
 * 
 */
public interface FlagNotificationRequestRepository extends BaseMongoRepository<FlagNotificationRequest> {
	
	/**
	 * Retrieves a list of {@link FlagNotificationRequest} given the reference id and post type.
	 * @param refId - id to retrieve the post
	 * @param type of post (i.e. question, answer etc.)
	 * @return a list of flag notification request by ref id, and type
	 */
	public List<FlagNotificationRequest> findByRefIdAndType(ObjectId refId, PostType type);
	
	/**
	 * Retrieves a list of {@link FlagNotificationRequest} by {@link FlagNotificationStatusType}.
	 * @param status to filter the notification request.
	 * @param type of post (i.e. question, answer etc.)
	 * @param pageable - provides option for pagination
	 * @return a list of flag notification request by status, and type
	 */
	public List<FlagNotificationRequest> findByStatusAndType(FlagNotificationStatusType status, PostType type, Pageable pageable);
}

/**
 * 
 */
package com.phroogal.core.service;

import java.util.List;

import org.bson.types.ObjectId;

import com.phroogal.core.domain.FlagNotificationRequest;
import com.phroogal.core.domain.FlagNotificationStatusType;
import com.phroogal.core.domain.Post;
import com.phroogal.core.domain.PostType;
/**
 * Service for {@link FlagNotificationRequest} functions
 * @author Christopher Mariano
 *
 */
public interface FlagNotificationService extends Service<FlagNotificationRequest, ObjectId> {

	/**
	 * Creates a request to flag a resource, or post
	 * @param {@link Post} to be flagged
	 * @param content - of the flagged resource, or post
	 * @return the {@link FlagNotificationRequest}
	 */
	public FlagNotificationRequest createRequest(Post post, String content);
	
	/**
	 * Updates the status of the flag notification request given the status
	 * @param postRefId - id to reference the notification request
	 * @param post type - to be flagged (i.e. question, answer)
	 * @param status - the notification should be updated to.
	 */
	public void updateStatus(Post post, FlagNotificationStatusType status);
	
	/**
	 * Retrieves a list of {@link FlagNotificationRequest} by {@link FlagNotificationStatusType}.
	 * @param status to filter the notification request.
	 * @param type of post (i.e. question, answer etc.)
	 * @param pageAt - states to return results starting from this page (number).
	 * @param pageSize - number of results from this page.
	 * @return a list of flag notification request by status, and type
	 */
	public List<FlagNotificationRequest> getByStatusAndType(FlagNotificationStatusType status, PostType type, int pageAt, int pageSize);
	
	/**
	 * Retrieves a list of reference Ids as {@link ObjectId} of {@link FlagNotificationRequest} by {@link FlagNotificationStatusType}.
	 * @param status to filter the notification request.
	 * @param type of post (i.e. question, answer etc.)
	 * @param pageAt - states to return results starting from this page (number).
	 * @param pageSize - number of results from this page.
	 * @return a list of flag notification request by status, and type
	 */
	public List<ObjectId> getRefIdByStatusAndType(FlagNotificationStatusType status, PostType type, int pageAt, int pageSize);
	
	/**
	 * Retrieves a list of reference Ids <i>as string</i> of {@link FlagNotificationRequest} by {@link FlagNotificationStatusType}.
	 * @param status to filter the notification request.
	 * @param type of post (i.e. question, answer etc.)
	 * @param pageAt - states to return results starting from this page (number).
	 * @param pageSize - number of results from this page.
	 * @return a list of flag notification request by status, and type
	 */
	public List<String> getRefIdAsStringByStatusAndType(FlagNotificationStatusType status, PostType type, int pageAt, int pageSize);
}

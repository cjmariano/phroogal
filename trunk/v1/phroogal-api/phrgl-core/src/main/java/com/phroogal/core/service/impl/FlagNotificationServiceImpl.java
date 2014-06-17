package com.phroogal.core.service.impl;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.phroogal.core.domain.FlagNotificationRequest;
import com.phroogal.core.domain.FlagNotificationStatusType;
import com.phroogal.core.domain.Post;
import com.phroogal.core.domain.PostType;
import com.phroogal.core.repository.FlagNotificationRequestRepository;
import com.phroogal.core.service.FlagNotificationService;
import com.phroogal.core.utility.CollectionUtil;

/**
 * Default implementation of the {@link FlagNotificationService} interface
 * @author Christopher Mariano
 *
 */
@Service
public class FlagNotificationServiceImpl extends BaseService<FlagNotificationRequest, ObjectId, FlagNotificationRequestRepository> implements FlagNotificationService{

	@Autowired
	private FlagNotificationRequestRepository flagNotificationRequestRepository;
	
	@Override
	protected FlagNotificationRequestRepository getRepository() {
		return flagNotificationRequestRepository;
	}

	@Override
	public FlagNotificationRequest createRequest(Post post, String content) {
		FlagNotificationRequest request = new FlagNotificationRequest();
		request.setRefId(post.getId());
		request.setType(post.getPostType());
		request.setContent(content);
		request.setPostBy(post.getPostBy());
		return this.saveOrUpdate(request);
	}
	
	@Override
	public void updateStatus(Post post, FlagNotificationStatusType status) {
		List<FlagNotificationRequest> requests = flagNotificationRequestRepository.findByRefIdAndType(post.getId(), post.getPostType());
		for (FlagNotificationRequest flagNotificationRequest : requests) {
			flagNotificationRequest.setStatus(status);
			this.saveOrUpdate(flagNotificationRequest);
		}
	}

	@Override
	public List<FlagNotificationRequest> getByStatusAndType(FlagNotificationStatusType status, PostType type, int pageAt, int pageSize) {
		PageRequest pageRequest = generatePageRequest(pageAt, pageSize);
		return flagNotificationRequestRepository.findByStatusAndType(status, type, pageRequest);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ObjectId> getRefIdByStatusAndType(FlagNotificationStatusType status, PostType type, int pageAt, int pageSize) {
		return doGetRefIdByStatusAndType(status, type, pageAt, pageSize, false);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<String> getRefIdAsStringByStatusAndType(FlagNotificationStatusType status, PostType type, int pageAt, int pageSize) {
		return doGetRefIdByStatusAndType(status, type, pageAt, pageSize, true);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List doGetRefIdByStatusAndType(FlagNotificationStatusType status, PostType type, int pageAt, int pageSize, boolean refIdAsString) {
		List referenceIds = CollectionUtil.arrayList();
		List<FlagNotificationRequest> flagRequests = getByStatusAndType(status, type, pageAt, pageSize);
		for (FlagNotificationRequest flagNotificationRequest : flagRequests) {
			ObjectId refid = flagNotificationRequest.getRefId();
			referenceIds.add(refIdAsString ? refid.toString() : refid);
		}
		return referenceIds;
	}
	
}

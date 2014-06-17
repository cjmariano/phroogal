package com.phroogal.core.service.impl;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.phroogal.core.domain.User;
import com.phroogal.core.domain.UserTag;
import com.phroogal.core.repository.UserTagRepository;
import com.phroogal.core.service.UserTagService;
import com.phroogal.core.utility.CollectionUtil;

/**
 * Default implementation of the {@link UserTagService} interface
 * @author Christopher Mariano
 *
 */
@Service
public class UserTagServiceImpl extends BaseService<UserTag, ObjectId, UserTagRepository> implements UserTagService{

	@Autowired
	private UserTagRepository userTagRepository;
	
	@Value(value="${usertags.default.values}")
	public String[] defaultUserTags;
	
	@Override
	protected UserTagRepository getRepository() {
		return userTagRepository;
	}
	
	@Override
	protected boolean onBeforeSaveOrUpdate(UserTag userTag) {
		List<UserTag> userTags = getRepository().findByUserIdAndName(userTag.getUserId(), userTag.getName());
		if ( isNotEmpty(userTags) ) {
			return false;
		}
		return true;
	}

	@Override
	public List<UserTag> getByUserId(ObjectId userId) {
		return getRepository().findByUserId(userId);
	}
	
	@Override
	public void createDefaultUserTagsFor(User user) {
		List<UserTag> userTags = CollectionUtil.arrayList();
		for (int i = 0; i < defaultUserTags.length; i++) {
			UserTag userTag = new UserTag();
			userTag.setName(defaultUserTags[i]);
			userTag.setUserId(user.getId());
			userTags.add(userTag);
		}
		this.saveOrUpdate(userTags);
	}
	
	private boolean isNotEmpty(List<UserTag> userTags) {
		return userTags!= null && userTags.size() > 0;
	}
}

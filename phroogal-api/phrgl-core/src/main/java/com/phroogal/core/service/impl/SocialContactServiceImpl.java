package com.phroogal.core.service.impl;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.stereotype.Service;

import com.phroogal.core.domain.SocialContact;
import com.phroogal.core.domain.User;
import com.phroogal.core.repository.SocialContactRepository;
import com.phroogal.core.service.SocialContactService;
import com.phroogal.core.social.SocialNetwork;
import com.phroogal.core.social.SocialNetworkResolver;
import com.phroogal.core.social.SocialNetworkType;

/**
 * Default implementation of the {@link SocialContact} interface
 * @author Christopher Mariano
 *
 */
@Service
public class SocialContactServiceImpl extends BaseService<SocialContact, ObjectId, SocialContactRepository> implements SocialContactService{
	
	@Autowired
	private SocialContactRepository socialcontactRepository;
	
	@Autowired
	private SocialNetworkResolver socialNetworkResolver;
	
	@Override
	protected SocialContactRepository getRepository() {
		return socialcontactRepository;
	}
	
	@Override
	public List<SocialContact> getByUserId(ObjectId userId) {
		return socialcontactRepository.findByUserId(userId);
	}

	@Override
	public List<SocialContact> getByUserIdAndConnectedThru(ObjectId userId, SocialNetworkType connectedThru) {
		return socialcontactRepository.findByUserIdAndConnectedThru(userId, connectedThru);
	}

	@Override
	public void synchSocialNetworkContacts(User user, Connection<?> connection) {
		ObjectId userId = user.getId();
		SocialNetwork api = socialNetworkResolver.getApi(connection);
		removeSocialNetworkContacts(userId, api.getSocialNetworkType());
		saveOrUpdate(api.getSocialContacts(userId));
	}

	@Override
	public void removeSocialNetworkContacts(ObjectId userId, SocialNetworkType socialNetworkType) {
		List<SocialContact> existingSocialContacts = this.getByUserIdAndConnectedThru(userId, socialNetworkType);
		this.delete(existingSocialContacts);
	}
}

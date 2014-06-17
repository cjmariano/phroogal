package com.phroogal.core.service.impl;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.phroogal.core.domain.User;
import com.phroogal.core.domain.UserSocialContact;
import com.phroogal.core.repository.UserSocialContactRepository;
import com.phroogal.core.rule.Rule;
import com.phroogal.core.rule.RuleExecutionContext;
import com.phroogal.core.service.SocialContactService;
import com.phroogal.core.service.UserSocialContactService;
import com.phroogal.core.utility.CollectionUtil;


/**
 * Default implementation of the {@link UserSocialContactService} interface
 * @author Christopher Mariano
 *
 */
@Service
public class UserSocialContactServiceImpl extends BaseService<UserSocialContact, ObjectId, UserSocialContactRepository> implements UserSocialContactService{

	@Autowired
	private UserSocialContactRepository userSocialContactRepository;
	
	@Autowired
	private SocialContactService socialContactService;
	
	@Autowired
	@Qualifier(value="socialContactsFilter")
	private Rule socialContactsFilter; 
	
	@Override
	protected UserSocialContactRepository getRepository() {
		return userSocialContactRepository;
	}

	@Override
	public UserSocialContact getByUserId(ObjectId userId) {
		return userSocialContactRepository.findByUserId(userId);		
	}

	@Override
	public void refreshUserSocialContact(User user) {
		if (user.getSocialProfiles().size() > 0) {
			ObjectId userId = user.getId();
			UserSocialContact userSocialContact = getUserSocialContact(userId);
			userSocialContact.addSocialContact(filterSocialContacts(user));
			saveOrUpdate(userSocialContact);			
		}
	}

	private UserSocialContact getUserSocialContact(ObjectId userId) {
		UserSocialContact userSocialContact = this.getByUserId(userId);
		if (userSocialContact == null) {
			userSocialContact = new UserSocialContact();
			userSocialContact.setUserId(userId);
		}
		return userSocialContact;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<ObjectId> filterSocialContacts(User user) {
		List facts = CollectionUtil.arrayList();
		facts.add(user);
		facts.addAll(socialContactService.findAll());
		socialContactsFilter.setFacts(facts);
		RuleExecutionContext<ObjectId> executionContext = socialContactsFilter.execute();
		return executionContext.getResults();
	}
}

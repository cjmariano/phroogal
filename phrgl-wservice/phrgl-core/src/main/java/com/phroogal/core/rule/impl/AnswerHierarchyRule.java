/**
 * 
 */
package com.phroogal.core.rule.impl;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.phroogal.core.domain.SocialContact;
import com.phroogal.core.domain.User;
import com.phroogal.core.rule.Rule;
import com.phroogal.core.service.AuthenticationDetailsService;
import com.phroogal.core.service.SocialContactService;
import com.phroogal.core.utility.CollectionUtil;

/**
 * Sorts answers by Hierarchy rule<br>
 * Drools rule is declared on rules.drl. 
 * @author Christopher Mariano
 *
 */
@Component("answerHierarchyRule")
public class AnswerHierarchyRule extends Rule {
	
	@Autowired
	private AuthenticationDetailsService<ObjectId> authenticationService;
	
	@Autowired
	private SocialContactService socialContactService;
	
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void preProcess(List facts) {
		User loggedInUser = authenticationService.getAuthenticatedUser();
		facts.add(loggedInUser);
		facts.addAll(getSocialContactsFromUser(loggedInUser));
	}

	private List<SocialContact> getSocialContactsFromUser(User loggedInUser) {
		List<SocialContact> socialContacts = CollectionUtil.arrayList(); 
		if (loggedInUser != null) {
			return socialContactService.getByUserId(loggedInUser.getId());	
		}
		return socialContacts;
	}
}

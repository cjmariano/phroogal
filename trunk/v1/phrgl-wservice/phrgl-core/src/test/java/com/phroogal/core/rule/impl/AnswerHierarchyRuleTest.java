package com.phroogal.core.rule.impl;


import java.util.List;

import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.phroogal.core.domain.Answer;
import com.phroogal.core.domain.AnswerSortType;
import com.phroogal.core.domain.Location;
import com.phroogal.core.domain.SocialContact;
import com.phroogal.core.domain.User;
import com.phroogal.core.repository.TestEntityGenerator;
import com.phroogal.core.rule.Rule;
import com.phroogal.core.rule.RulesContext;
import com.phroogal.core.service.AuthenticationDetailsService;
import com.phroogal.core.service.SocialContactService;
import com.phroogal.core.social.SocialNetworkType;
import com.phroogal.core.utility.CollectionUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/spring/app-context.xml"})
public class AnswerHierarchyRuleTest {
	
	@Autowired
	@Qualifier(value="answerHierarchyRule")
	private Rule answerHierarchyRule; 
	
	@Autowired
	private RulesContext rulesContext;
	
	private SocialContactService socialContactService = Mockito.mock(SocialContactService.class);
	
	@SuppressWarnings("unchecked")
	private AuthenticationDetailsService<ObjectId> authenticationService = Mockito.mock(AuthenticationDetailsService.class);
	
	private TestEntityGenerator generator = new TestEntityGenerator();

	@Test
	public void testFireAnswerHierachyRules() throws Exception {
		List<Answer> answers = CollectionUtil.arrayList();
		SocialContact socialContact = generator.generateTestSocialContact(SocialNetworkType.FACEBOOK);
		List<SocialContact> socialContactsList = CollectionUtil.arrayList();
		socialContactsList.add(socialContact);
		Mockito.when(socialContactService.getByUserId(Mockito.any(ObjectId.class))).thenReturn(socialContactsList );
		Answer answer1 = generator.generateAnswerWithVotesTotalOf(100);
		Answer answer2 = generator.generateAnswerWithVotesTotalOf(200);
		Answer answer3 = generator.generateAnswerWithVotesTotalOf(300);
		answers.add(answer1);
		answers.add(answer2);
		answers.add(answer3);
		
		fireAnswerHierarchyRule(answers);
		
		Assert.assertTrue(answer1.getRank() == 100);
		Assert.assertTrue(answer2.getRank() == 200);
		Assert.assertTrue(answer3.getRank() == getHighestVoteWeight() + 300);
		Assert.assertTrue(answer1.getAnswerSortType().size() == 0);
		Assert.assertTrue(answer2.getAnswerSortType().size() == 0);
		Assert.assertTrue(answer3.getAnswerSortType().get(0).equals(AnswerSortType.VOTES));
	}
	
	@Test
	public void testAnswerBySocialContactsHasCorrectRank_Facebook() throws Exception {
		assertAnswerBySocialContactsHasCorrectRank(SocialNetworkType.FACEBOOK, rulesContext.answerWeightFacebook);
	}
	
	@Test
	public void testAnswerBySocialContactsHasCorrectRank_Linkedin() throws Exception {
		assertAnswerBySocialContactsHasCorrectRank(SocialNetworkType.LINKEDIN, rulesContext.answerWeightLinkedIn);
	}
	
	@Test
	public void testAnswerBySocialContactsHasCorrectRank_Google() throws Exception {
		assertAnswerBySocialContactsHasCorrectRank(SocialNetworkType.GOOGLE, rulesContext.answerWeightGoogle);
	}
	
	@Test
	public void assertAnswerBySameLocationHasCorrectRank() throws Exception {
		List<Answer> answers = CollectionUtil.arrayList();
		String testLocationState = "CA";
		int answerWeight = rulesContext.answerWeightLocation;
		int totalVotesForAnswer = 100;
		Answer answer = generator.generateAnswerWithVotesTotalOf(totalVotesForAnswer);
		answer.getPostBy().getProfile().getLocation().setState(testLocationState);
		answers.add(answer);
		
		User loggedInUser = new User();
		Location userLocation = generator.generateTestLocation(); 
		userLocation.setState(testLocationState);
		loggedInUser.setId(ObjectId.get());
		loggedInUser.getProfile().setLocation(userLocation);
		Mockito.when(authenticationService.getAuthenticatedUser()).thenReturn(loggedInUser);
		
		fireAnswerHierarchyRule(answers);
		Assert.assertTrue(answers.get(0).getRank() == getHighestVoteWeight() + totalVotesForAnswer + answerWeight);
		Assert.assertTrue(answers.get(0).getAnswerSortType().get(0).equals(AnswerSortType.VOTES));
		Assert.assertTrue(answers.get(0).getAnswerSortType().get(1).equals(AnswerSortType.LOCATION));
	}
	
	@Test
	public void assertAnswerByDifferentLocationHasCorrectRank() throws Exception {
		List<Answer> answers = CollectionUtil.arrayList();
		String testLocationState = "CA";
		String testLocationStateOther = "NY";
		int totalVotesForAnswer = 100;
		Answer answer = generator.generateAnswerWithVotesTotalOf(totalVotesForAnswer);
		answer.getPostBy().setId(ObjectId.get());
		answer.getPostBy().getProfile().getLocation().setState(testLocationState);
		answers.add(answer);
		
		User loggedInUser = new User();
		loggedInUser.setId(ObjectId.get());
		loggedInUser.getProfile().getLocation().setState(testLocationStateOther);
		Mockito.when(authenticationService.getAuthenticatedUser()).thenReturn(loggedInUser);
		
		fireAnswerHierarchyRule(answers);
		Assert.assertTrue(answers.get(0).getRank() == getHighestVoteWeight() + totalVotesForAnswer);
	}
	
	@Test
	public void assertAnswerByNoLocationHasNoEffect() throws Exception {
		List<Answer> answers = CollectionUtil.arrayList();
		int totalVotesForAnswer = 100;
		Answer answer = generator.generateAnswerWithVotesTotalOf(totalVotesForAnswer);
		answer.getPostBy().setId(ObjectId.get());
		answer.getPostBy().getProfile().setLocation(new Location());
		answers.add(answer);
		
		User loggedInUser = new User();
		loggedInUser.setId(ObjectId.get());
		loggedInUser.getProfile().setLocation(new Location());
		Mockito.when(authenticationService.getAuthenticatedUser()).thenReturn(loggedInUser);
		
		fireAnswerHierarchyRule(answers);
		Assert.assertTrue(answers.get(0).getRank() == getHighestVoteWeight() + totalVotesForAnswer);
	}
	
	private void assertAnswerBySocialContactsHasCorrectRank(SocialNetworkType socialNetworkType, int answerWeight) throws Exception {
		List<Answer> answers = CollectionUtil.arrayList();
		int totalVotesForAnswer = 100;
		SocialContact socialContact = generator.generateTestSocialContact(socialNetworkType);
		List<SocialContact> socialContactsList = CollectionUtil.arrayList();
		socialContactsList.add(socialContact);
		Mockito.when(socialContactService.getByUserId(Mockito.any(ObjectId.class))).thenReturn(socialContactsList);
		Mockito.when(authenticationService.getAuthenticatedUser()).thenReturn(new User());
		
		Answer answer = generator.generateAnswerWithVotesTotalOf(totalVotesForAnswer);
		answers.add(answer);
		
		fireAnswerHierarchyRule(answers);
		Assert.assertTrue(answers.get(0).getRank() == getHighestVoteWeight() + totalVotesForAnswer + answerWeight);
		Assert.assertTrue(answers.get(0).getAnswerSortType().get(0).equals(AnswerSortType.VOTES));
		Assert.assertTrue(answers.get(0).getAnswerSortType().get(1).equals(resolveAnswerSortType(socialNetworkType)));
	}

	private AnswerSortType resolveAnswerSortType(SocialNetworkType socialNetworkType) {
		switch (socialNetworkType) {
		case FACEBOOK:
			return AnswerSortType.SOCIAL_MEDIA_FACEBOOK;
		case LINKEDIN:
			return AnswerSortType.SOCIAL_MEDIA_LINKEDIN;
		case GOOGLE:
			return AnswerSortType.SOCIAL_MEDIA_GOOGLE;
		default:
			return null;
		}
	}

	private void fireAnswerHierarchyRule(List<Answer> answers) {
		ReflectionTestUtils.setField(answerHierarchyRule, "socialContactService", socialContactService);
		ReflectionTestUtils.setField(answerHierarchyRule, "authenticationService", authenticationService);
		answerHierarchyRule.setFacts(answers);
		answerHierarchyRule.execute();
	}
	
	private int getHighestVoteWeight() {
		return rulesContext.getAnswerWeightVotes();
	}
}

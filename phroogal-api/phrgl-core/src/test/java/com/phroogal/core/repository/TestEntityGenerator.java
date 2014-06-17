package com.phroogal.core.repository;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.joda.time.DateTime;

import com.phroogal.core.domain.Answer;
import com.phroogal.core.domain.College;
import com.phroogal.core.domain.Comment;
import com.phroogal.core.domain.CreditUnion;
import com.phroogal.core.domain.Dictionary;
import com.phroogal.core.domain.FlagNotificationRequest;
import com.phroogal.core.domain.FlagNotificationStatusType;
import com.phroogal.core.domain.Location;
import com.phroogal.core.domain.PostType;
import com.phroogal.core.domain.Question;
import com.phroogal.core.domain.Reply;
import com.phroogal.core.domain.SocialContact;
import com.phroogal.core.domain.SocialProfile;
import com.phroogal.core.domain.Tag;
import com.phroogal.core.domain.User;
import com.phroogal.core.domain.UserProfile;
import com.phroogal.core.domain.UserSocialContact;
import com.phroogal.core.domain.UserTag;
import com.phroogal.core.domain.Votes;
import com.phroogal.core.domain.analytics.UserSearchHistory;
import com.phroogal.core.domain.security.EmailConfirmationRequest;
import com.phroogal.core.domain.security.PasswordResetRequest;
import com.phroogal.core.domain.security.RememberMeToken;
import com.phroogal.core.search.index.QuestionIndex;
import com.phroogal.core.social.SocialNetworkType;
import com.phroogal.core.utility.CollectionUtil;

public final class TestEntityGenerator {

	public User generateTestUser() {
		User user = new User();
		user.setId(ObjectId.get());
		user.setProfile(generateTestUserProfile());
		user.setSocialProfiles(generateTestSocialProfilesList());
		user.setCreatedOn(DateTime.now());
		user.setModifiedOn(DateTime.now());
		return user;
	}
	
	public List<SocialProfile> generateTestSocialProfilesList() {
		List<SocialProfile> socialProfiles = CollectionUtil.arrayList();
		socialProfiles.add(generateTestSocialProfile(SocialNetworkType.FACEBOOK));
		socialProfiles.add(generateTestSocialProfile(SocialNetworkType.GOOGLE));
		socialProfiles.add(generateTestSocialProfile(SocialNetworkType.LINKEDIN));
		return socialProfiles;
	}

	public SocialProfile generateTestSocialProfile(SocialNetworkType site) {
		SocialProfile socialProfile = new SocialProfile();
		socialProfile.setSite(site);
		socialProfile.setEmail("a@b.com");
		socialProfile.setUserId(generateTestSocialProfileUserId(site));
		return socialProfile;
	}

	public SocialContact generateTestSocialContact(SocialNetworkType socialNetworkType) {
		SocialContact socialContact = new SocialContact();
		socialContact.setId(ObjectId.get());
		socialContact.setConnectedThru(socialNetworkType);
		socialContact.setContactUserId((generateTestSocialProfileUserId(socialNetworkType)));
		return socialContact;
	}

	public UserProfile generateTestUserProfile() {
		UserProfile userProfile = new UserProfile();
		userProfile.setPassword("password");	
		userProfile.setFirstname("John");
		userProfile.setLastname("Smith");
		userProfile.setEmail("john@isp.com");
		userProfile.setLocation(generateTestLocation());
		return userProfile;
	}

	public Question generateTestQuestion() {
		Question question = new Question();
		question.setCreatedOn(DateTime.now());
		question.setId(ObjectId.get());
		question.setTitle("What is Phroogal");
		question.setContent("Could anybody tell me what this site is all about?");
		question.setTags(Arrays.asList(generateTestTag().getName()));
		question.setPostBy(generateTestUser());
		return question;
	}
	
	public QuestionIndex generateTestQuestionIndex() {
		QuestionIndex questionIndex = new QuestionIndex();
		questionIndex.setId(ObjectId.get().toString());
		questionIndex.setTitle("What is Phroogal");
		questionIndex.setContent("Any ideas?");
		questionIndex.setTags(Arrays.asList(generateTestTag().getName()));
		questionIndex.setPostedByUser(generateTestUser().getUserCompleteName());
		questionIndex.setCreatedOn(DateTime.now());
		return questionIndex;
	}

	public Answer generateTestAnswer() {
		Answer answer = new Answer();
		answer.setCreatedOn(DateTime.now());
		answer.setPostBy(generateTestUser());
		answer.setId(ObjectId.get());
		answer.setQuestionRef(generateTestQuestion().getId());
		answer.setContent("Phroogal is crowd sourced financial "
			+ "information to share knowledge, discover new resources "
			+ "and connect with money-savvy peers and experts.");
		return answer;
	}
	
	public Answer generateAnswerWithVotesTotalOf(int total) {
		Answer answer = new Answer();
		answer.setPostBy(generateTestUser());
		Votes votes = new Votes();
		votes.setTotal(total);
		answer.setId(ObjectId.get());
		answer.setVotes(votes);
		return answer;
	}
	
	public Comment generateTestComment() {
		Comment comment = new Comment();
		comment = new Comment();
		comment.setId(ObjectId.get());
		comment.setAnswerRef(generateTestAnswer().getId());
		comment.setContent("Nice!");	
		comment.setCreatedOn(DateTime.now());
		comment.setModifiedOn(DateTime.now());
		return comment;
	}
	
	public Reply generateTestReply() {
		Reply reply = new Reply();
		reply.setId(ObjectId.get());
		reply.setCommentRef(generateTestComment().getId());
		reply.setContent("I would like to signin, where should I start?");	
		return reply;
	}

	public RememberMeToken generateTestRememberMeToken() {
		RememberMeToken rememberMeToken = new RememberMeToken();
		rememberMeToken.setId(ObjectId.get());
		rememberMeToken.setSeries("12345");
		rememberMeToken.setTokenValue("token");
		rememberMeToken.setUsername("user1");
		rememberMeToken.setDate(new Date());
		return rememberMeToken;
	}

	public PasswordResetRequest generateTestPasswordResetRequest() {
		PasswordResetRequest passwordResetRequest = new PasswordResetRequest();
		passwordResetRequest.setId(ObjectId.get());
		passwordResetRequest.setEmail("a@b.com");
		passwordResetRequest.setRequestDate(new DateTime());
		return passwordResetRequest;
	}
	
	public EmailConfirmationRequest generateTestEmailRepositoryRequest() {
		EmailConfirmationRequest emailConfirmationRequest = new EmailConfirmationRequest();
		emailConfirmationRequest.setId(ObjectId.get());
		emailConfirmationRequest.setEmail("a@b.com");
		return emailConfirmationRequest;
	}

	public SocialContact generateTestSocialContact() {
		SocialContact socialContact = new SocialContact();
		socialContact.setId(ObjectId.get());
		socialContact.setContactUserId("abcde12345");
		socialContact.setConnectedThru(SocialNetworkType.LINKEDIN);
		socialContact.setUserId(ObjectId.get());
		return socialContact;
	}
	
	public Location generateTestLocation() {
		Location location = new Location();
		location.setLocationRef("CkQ0AAAA3dhObIeWI5VPtDZaDr8UVwfpjf79h9V9GlHE3nD7tnlFTeyIoCvlCKKdHXjY9epAFWxu1MQrthwDUv7sl7XqaxIQPbU8-_UB1EFNVdGLsV9aNxoUmHFfdbP3RhTXLK0GfBoD0S8FGEI");
		location.setDisplayName("Elizabeth, NJ, USA");
		location.setCity("Elizabeth");
		location.setState("NJ");
		location.setCountry("US");
		location.setLatitude(40.6639916);
		location.setLongtitude(-74.2107006);
		return location;
	}

	public Tag generateTestTag() {
		return generateTestTag("Investment");
	}
	
	public Tag generateTestTag(String tagName) {
		Tag tag = new Tag();
		tag.setId(ObjectId.get());
		tag.setName(tagName);
		return tag;
	}
	
	private String generateTestSocialProfileUserId(SocialNetworkType site) {
		StringBuffer sb = new StringBuffer(site.getId());
		return sb.append("-aabbccddee").toString();
	}

	public College generateTestCollege() {
		College college = new College();
		college.setId(ObjectId.get());
		college.setName("Boulder University Colorado");
		return college;
	}

	public CreditUnion generateTestCreditUnion() {
		CreditUnion creditUnion = new CreditUnion();
		creditUnion.setId(ObjectId.get());
		creditUnion.setName("California Credit Union");
		return creditUnion;
	}

	public UserSearchHistory generateTestUserSearchHistory() {
		UserSearchHistory userSearchHistory = new UserSearchHistory();
		userSearchHistory.setId(ObjectId.get());
		userSearchHistory.setUserPrimaryEmail("a@b.com");
		userSearchHistory.addSearchTerm("California Credit Union");
		return userSearchHistory;
	}

	public UserTag generateTestUserTag() {
		UserTag userTag = new UserTag();
		userTag.setId(ObjectId.get());
		userTag.setName("Investment");
		userTag.setUserId(ObjectId.get());
		return userTag;
	}

	public UserSocialContact generateTestUserSocialContact() {
		UserSocialContact userSocialContact = new UserSocialContact();
		List<ObjectId> socialContactIds = CollectionUtil.arrayList();
		socialContactIds.add(ObjectId.get());
		userSocialContact.setId(ObjectId.get());
		userSocialContact.setUserId(ObjectId.get());
		userSocialContact.addSocialContact(socialContactIds);
		return userSocialContact;
	}

	public FlagNotificationRequest generateTestFlagNotificationRequest() {
		FlagNotificationRequest flagNotificationRequest = new FlagNotificationRequest();
		User postByUser = generateTestUser();
		flagNotificationRequest.setId(ObjectId.get());
		flagNotificationRequest.setCreatedOn(DateTime.now());
		flagNotificationRequest.setContent("This is a flagged content");
		flagNotificationRequest.setFlaggedBy(postByUser);
		flagNotificationRequest.setRefId(ObjectId.get());
		flagNotificationRequest.setType(PostType.QUESTION);
		flagNotificationRequest.setStatus(FlagNotificationStatusType.ACTIVE);
		return flagNotificationRequest;
	}

	public Dictionary generateTestDictionary() {
		Dictionary dictionary = new Dictionary();
		dictionary.setId(ObjectId.get());
		dictionary.setTerm("Credit Union");
		dictionary.setDefinition("A not-for-profit financial institution owned by its members and represented by a volunteer Board of Directors that are elected by the membership. Credit unions do not seek to make a profit. Instead, credit unions use earnings above expenses to provide members with better rates on loans, higher rates on savings and convenient products and services like Internet banking. Credit union deposits are federally insured for up to $250,000 by the NCUA (National Credit Union Administration).");
		dictionary.setTags(Arrays.asList("Definitions", "Credit Union", "Banking"));
		return dictionary;
	}
}


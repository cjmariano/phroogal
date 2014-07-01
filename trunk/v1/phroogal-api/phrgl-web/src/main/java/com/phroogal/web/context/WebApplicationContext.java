/**
 * 
 */
package com.phroogal.web.context;


/**
 * Holds the context that would be available throughout this web application
 * @author Christopher Mariano
 *
 */
public final class WebApplicationContext {
	
	/*
	 * Static values for request uri
	 */
	
	public static final String URI_RESID_PLACEHOLDER = "{id}";
	public static final String URI_QUESTION_LINK_FORMAT = "question/%s/%s";
	
	public static final String URI_LOGIN = "login";
	public static final String URI_LOGIN_SOCIAL = "login/{providerId}";
	public static final String URI_LOGIN_PROVIDER_USER = "login/{providerId}/{providerUserId}";
	
	public static final String URI_SIGNUP = "signup";
	
	public static final String URI_USERS = "users";
	public static final String URI_USERS_ID = "users/{id}";
	public static final String URI_USERS_ID_PROFILEPIC ="users/{id}/profile-picture";
	public static final String URI_USERS_ID_PASSWORD ="users/{id}/password";
	public static final String URI_USERS_ID_LOCATION = "users/{id}/location";
	public static final String URI_USERS_ID_SOCIALPROFILES_PROVIDERID = "users/{id}/social-profiles/{providerId}";
	
	public static final String URI_QUESTIONS = "questions";
	public static final String URI_QUESTIONS_DOCID = "questions/{id}";
	public static final String URI_QUESTIONS_DOCID_TAGS="questions/{id}/tags/{tag}";
	public static final String URI_QUESTIONS_TOTALVIEWCOUNT = "questions/{id}/totalViewCount";
	
	public static final String URI_ANSWERS = "answers";
	public static final String URI_ANSWERS_ID = "answers/{id}";
	public static final String URI_ANSWERS_ID_VOTE = "answers/{id}/vote";
	
	public static final String URI_COMMENTS = "comments";
	public static final String URI_COMMENTS_ID = "comments/{id}";
	
	public static final String URI_REPLIES = "replies";
	public static final String URI_REPLIES_ID = "replies/{id}";
	
	public static final String URI_TAGS = "tags";
	//TODO: Consider merging with tags service
	public static final String URI_USERTAGS_ID="user-tags/{id}";
	public static final String URI_USERTAGS_USERID="user-tags/user/{id}";
	
	public static final String URI_PASSWORD_RESET_REQUEST = "password-reset-request";
	public static final String URI_PASSWORD_RESET_REQUEST_ID = "password-reset-request/{id}";
	public static final String URI_PASSWORD_RESET_REQUEST_ID_PASSWORD = "password-reset-request/{id}/password";
	
	public static final String URI_EXTERNAL_RESOURCE = "external-resource";
	public static final String URI_EXTERNAL_RESOURCE_CATEGORY = "external-resource/{category}";
	
	public static final String URI_COLLEGES = "colleges";
	
	public static final String URI_LOCATIONS = "locations";
	
	public static final String URI_CREDITUNIONS = "credit-unions";
	
	public static final String PAGE_PASSWORD_RESET = "password_reset";
	public static final String PAGE_EMAIL_CONFIRMATION = "email-confirmation";
	
}

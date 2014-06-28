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
	
	public static final String URI_LOGIN = "api/login";
	public static final String URI_LOGIN_PROVIDER = "api/login/{providerId}";
	public static final String URI_LOGIN_PROVIDER_USER = "api/login/{providerId}/{providerUserId}";
	
	public static final String URI_SIGNUP_POST = "api/signup";
	
	public static final String URI_USER_POST = "api/users/user";
	public static final String URI_USER_GET = "api/users/user-{id}";
	public static final String URI_USER_PARTIAL_POST = URI_USER_GET;
	public static final String URI_USER_UPLOAD_PICTURE_POST ="api/users/user-{id}/profile-picture";
	public static final String URI_USER_CHANGE_PASSWORD_POST ="api/users/user-{id}/password";
	public static final String URI_USER_GET_ALL = "api/users";
	public static final String URI_USER_SEARCH = URI_USER_POST;
	public static final String URI_USER_REMOVE_SOCIAL_NETWORK = "api/users/user-{id}/socialProfiles/{providerId}";
	public static final String URI_USER_LOCATION_POST = "api/users/user-{id}/location";
	
	public static final String URI_QUESTION_PREFIX = "api/posts/*";
	public static final String URI_QUESTIONS = "/questions";
	public static final String URI_QUESTION_POST = "/question";
	public static final String URI_QUESTION_GET = "/question-{id}";
	public static final String URI_QUESTION_DELETE = URI_QUESTION_GET;
	public static final String URI_QUESTION_PARTIAL_POST = URI_QUESTION_GET;
	public static final String URI_QUESTION_GET_ALL = "/questions";
	public static final String URI_QUESTION_SEARCH = "/questions/query";
	public static final String URI_QUESTION_TOTAL_VIEW = "/question-{id}/totalViewCount";
	public static final String URI_QUESTION_ANONYMOUS_TOGGLE = "/question-{id}/updatePostType";
	public static final String URI_QUESTION_UPDATE_TAGS="/question-{id}/tags/{tag}";
	
	public static final String URI_ANSWER_PREFIX = "api/posts/questions/*";
	public static final String URI_ANSWER_POST = "/answer";
	public static final String URI_ANSWER_GET = "/answer-{id}";
	public static final String URI_ANSWER_DELETE = URI_ANSWER_GET;
	public static final String URI_ANSWER_PARTIAL_POST = URI_ANSWER_GET;
	public static final String URI_ANSWER_GET_ALL = "/answers";
	public static final String URI_ANSWER_VOTE_POST = "/answer-{id}/rating";
	
	public static final String URI_COMMENT_PREFIX = "api/posts/questions/answers/*";
	public static final String URI_COMMENT_POST = "/comment";
	public static final String URI_COMMENT_GET = "/comment-{id}";
	public static final String URI_COMMENT_PARTIAL_POST = URI_COMMENT_GET;
	public static final String URI_COMMENT_GET_ALL = "/comments";
	
	public static final String URI_REPLY_PREFIX = "api/posts/questions/answers/comments/*";
	public static final String URI_REPLY_POST = "/reply";
	public static final String URI_REPLY_GET = "/reply-{id}";
	public static final String URI_REPLY_PARTIAL_POST = URI_REPLY_GET;
	public static final String URI_REPLY_GET_ALL = "/replies";
	
	public static final String PAGE_PASSWORD_RESET = "api/password_reset";
	public static final String PAGE_EMAIL_CONFIRMATION = "api/email-confirmation";
	
	public static final String URI_USER_REQUEST_PASSWORD_RESET = "api/password-reset-request";
	public static final String URI_USER_REQUEST_PASSWORD_RESET_GET = "api/password-reset-request-{id}";
	public static final String URI_USER_REQUEST_PASSWORD_RESET_POST = "api/password-reset-request-{id}/password";
	
	public static final String URI_EXTERNAL_RESOURCE_SEARCH = "api/external-resource/category/{category}/query";
	public static final String URI_EXTERNAL_RESOURCE_SEARCH_ALL = "api/external-resource/query";
	
	public static final String URI_COLLEGES_PREFIX = "api/posts/*";
	public static final String URI_COLLEGES_GET_ALL = "colleges";
	public static final String URI_COLLEGES_SEARCH = "colleges/query";
	public static final String URI_COLLEGE_POST = "colleges";
	
	public static final String URI_LOCATION_SEARCH = "api/locations/query";
	
	public static final String URI_CREDIT_UNION_PREFIX= "api/posts/*";
	public static final String URI_CREDIT_UNION_GET_ALL = "creditUnions";
	public static final String URI_CREDIT_UNION_SEARCH = "creditUnions/query";
	
	public static final String URI_TAG_GET_ALL="api/tags";
	public static final String URI_TAG_SEARCH= "api/tags/query";
	public static final String URI_USER_TAG_POST="api/tags/user-{id}";
	public static final String URI_USER_TAG_DELETE="api/tags/user/tag-{id}";
	
}

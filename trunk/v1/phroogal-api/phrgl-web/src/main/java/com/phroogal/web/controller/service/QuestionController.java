package com.phroogal.web.controller.service;

import static com.phroogal.web.context.WebApplicationContext.URI_QUESTIONS;
import static com.phroogal.web.context.WebApplicationContext.URI_QUESTION_ANONYMOUS_TOGGLE;
import static com.phroogal.web.context.WebApplicationContext.URI_QUESTION_DELETE;
import static com.phroogal.web.context.WebApplicationContext.URI_QUESTION_GET;
import static com.phroogal.web.context.WebApplicationContext.URI_QUESTION_GET_ALL;
import static com.phroogal.web.context.WebApplicationContext.URI_QUESTION_PARTIAL_POST;
import static com.phroogal.web.context.WebApplicationContext.URI_QUESTION_POST;
import static com.phroogal.web.context.WebApplicationContext.URI_QUESTION_PREFIX;
import static com.phroogal.web.context.WebApplicationContext.URI_QUESTION_SEARCH;
import static com.phroogal.web.context.WebApplicationContext.URI_QUESTION_TOTAL_VIEW;
import static com.phroogal.web.context.WebApplicationContext.URI_QUESTION_UPDATE_TAGS;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.solr.client.solrj.SolrServerException;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.phroogal.core.domain.FlagNotificationRequest;
import com.phroogal.core.domain.Question;
import com.phroogal.core.search.index.QuestionIndex;
import com.phroogal.core.service.QuestionService;
import com.phroogal.core.service.Service;
import com.phroogal.core.service.UserSearchHistoryService;
import com.phroogal.web.bean.QuestionBean;
import com.phroogal.web.bean.QuestionIndexBean;
import com.phroogal.web.bean.mapper.MapperService;
import com.phroogal.web.controller.rest.RestPatchRequest;
import com.phroogal.web.notification.FlagNotification;
import com.wordnik.swagger.annotations.Api;

@Controller
@Api(value="question", description="Question Operations", position = 6)
@RequestMapping(URI_QUESTION_PREFIX)
public class QuestionController extends BasicController<Question, QuestionBean, ObjectId> {

	@Autowired
	private QuestionService questionService;
	
	@Autowired
	@Qualifier(value="flagNotification")
	private FlagNotification flagNotification;
	
	@Autowired
	private MapperService<QuestionIndex, QuestionIndexBean> indexMapper;
	
	@Autowired
	private UserSearchHistoryService userSearchHistoryService;
	
	@Override
	protected Service<Question, ObjectId> returnDomainService() {
		return questionService;
	}
	
	@RequestMapping(value = URI_QUESTION_POST, method = RequestMethod.POST)
	public @ResponseBody
	Object addUpdateQuestion(HttpServletRequest request, HttpServletResponse response, @RequestBody QuestionBean questionBean) {
		return super.addUpdateResource(request, response, questionBean);
	}
	
	@RequestMapping(value = URI_QUESTION_GET, method = RequestMethod.GET)
	public @ResponseBody
	Object getQuestion(@PathVariable long id, HttpServletRequest request, HttpServletResponse response) {
		Question question = questionService.getByDocId(id);
		return getObjectMapper().toBean(question, QuestionBean.class);
	}
	
	@RequestMapping(value = URI_QUESTION_PARTIAL_POST, method = RequestMethod.PATCH)
	public @ResponseBody Object doPatchResource(@PathVariable ObjectId id, HttpServletRequest request, HttpServletResponse response, @RequestBody List<RestPatchRequest> patchRequest) throws Exception {
		return super.doPatchResource(id, request, response, patchRequest);
	}
	
	@RequestMapping(value = URI_QUESTION_GET_ALL, method = RequestMethod.GET)
	public @ResponseBody
	Object getAllQuestions(HttpServletRequest request, HttpServletResponse response) {
		return super.getAllResources(request, response);
	}
	
	@RequestMapping(value = URI_QUESTION_GET_ALL, method = RequestMethod.GET, params={"pageAt", "pageSize"})
	public @ResponseBody
	Object getAllQuestions(@RequestParam("pageAt") int pageAt, @RequestParam("pageSize") int pageSize, HttpServletRequest request, HttpServletResponse response) {
		return super.getAllResources(pageAt, pageSize, request, response);
	}
	
	@RequestMapping(value = URI_QUESTIONS, method = RequestMethod.GET, params={"postBy","pageAt","pageSize"})
	public @ResponseBody
	Object getQuestionsByUser(@RequestParam("postBy") ObjectId userId, @RequestParam("pageAt") int pageAt, @RequestParam("pageSize") int pageSize, HttpServletRequest request, HttpServletResponse response) {
		Page<Question> resultsList = questionService.getByUserId(userId, pageAt, pageSize);
		return getPaginatedResults(resultsList, QuestionBean.class);
	}
	
	@RequestMapping(value = URI_QUESTIONS, method = RequestMethod.GET, params={"socialContactsOf","pageAt","pageSize"})
	public @ResponseBody
	Object getSocialQuestionsByUser(@RequestParam("socialContactsOf") ObjectId userId, @RequestParam("pageAt") int pageAt, @RequestParam("pageSize") int pageSize, HttpServletRequest request, HttpServletResponse response) {
		Page<Question> resultsList = questionService.getSocialQuestions(userId, pageAt, pageSize);
		return getPaginatedResults(resultsList, QuestionBean.class);
	}
	
	@RequestMapping(value = URI_QUESTIONS, method = RequestMethod.GET, params={"showMostRecent=true","pageAt","pageSize"})
	public @ResponseBody
	Object getMostRecentQuestions(@RequestParam("pageAt") int pageAt, @RequestParam("pageSize") int pageSize, HttpServletRequest request, HttpServletResponse response) {
		Page<QuestionIndex> resultsList = questionService.getRecentQuestions(pageAt, pageSize);
		return getPaginatedResults(resultsList, QuestionIndexBean.class, indexMapper);
	}
	
	@RequestMapping(value = URI_QUESTIONS, method = RequestMethod.GET, params={"showFlagged=true", "pageAt", "pageSize"})
	public @ResponseBody
	Object getFlaggedQuestions(@RequestParam("pageAt") int pageAt, @RequestParam("pageSize") int pageSize, HttpServletRequest request, HttpServletResponse response) {
		Page<QuestionIndex> resultsList = questionService.getFlaggedQuestions(pageAt, pageSize);
		return getPaginatedResults(resultsList, QuestionIndexBean.class, indexMapper);
	}

	@RequestMapping(value = URI_QUESTIONS, method = RequestMethod.GET, params={"showTrending=true","pageAt","pageSize"})
	public @ResponseBody
	Object getTrendingQuestions(@RequestParam("pageAt") int pageAt, @RequestParam("pageSize") int pageSize, HttpServletRequest request, HttpServletResponse response) {
		Page<QuestionIndex> resultsList = questionService.getTrendingQuestions(pageAt, pageSize);
		return getPaginatedResults(resultsList, QuestionIndexBean.class, indexMapper);
	}
	
	@RequestMapping(value = URI_QUESTIONS, method = RequestMethod.GET, params={"showTrending=true","excludeDocId","pageAt","pageSize"})
	public @ResponseBody
	Object getTrendingQuestions(@RequestParam("pageAt") int pageAt, @RequestParam("pageSize") int pageSize, @RequestParam("excludeDocId") long excludeDocId, HttpServletRequest request, HttpServletResponse response) {
		Page<QuestionIndex> resultsList = questionService.getTrendingQuestions(pageAt, pageSize, excludeDocId);
		return getPaginatedResults(resultsList, QuestionIndexBean.class, indexMapper);
	}
	
	@RequestMapping(value = URI_QUESTIONS, method = RequestMethod.GET, params={"showMostViewed=true","numberOfDays","pageAt","pageSize"})
	public @ResponseBody
	Object getMostViewedQuestions(@RequestParam("pageAt") int pageAt, @RequestParam("pageSize") int pageSize, @RequestParam("numberOfDays") int numberOfDays, HttpServletRequest request, HttpServletResponse response) {
		Page<QuestionIndex> resultsList = questionService.getMostViewedQuestions(pageAt, pageSize, numberOfDays);
		return getPaginatedResults(resultsList, QuestionIndexBean.class, indexMapper);
	}
	
	@RequestMapping(value = URI_QUESTIONS, method = RequestMethod.GET, params={"showUnanswered=true","pageAt","pageSize"})
	public @ResponseBody
	Object getUnansweredQuestions(@RequestParam("pageAt") int pageAt, @RequestParam("pageSize") int pageSize, HttpServletRequest request, HttpServletResponse response) {
		Page<QuestionIndex> resultsList = questionService.getUnansweredQuestions(pageAt, pageSize);
		return getPaginatedResults(resultsList, QuestionIndexBean.class, indexMapper);
	}
	
	@RequestMapping(value = URI_QUESTION_SEARCH, method = GET, params={"keyword","topAnswer=true","pageAt","pageSize"})
	public @ResponseBody
	Object searchQuestionsByKeyword(@RequestParam("keyword") String keyword, @RequestParam("pageAt") int pageAt, @RequestParam("pageSize") int pageSize, HttpServletRequest request, HttpServletResponse response) {
		userSearchHistoryService.addKeywordToUserSearchHistory(keyword);
		Page<QuestionIndex> resultsList = questionService.searchQuestion(keyword, pageAt, pageSize);
		return getPaginatedResults(resultsList, QuestionIndexBean.class, indexMapper);
	}
	
	@RequestMapping(value = URI_QUESTION_GET_ALL, method = RequestMethod.GET, params={"tag", "topAnswer=true","pageAt","pageSize"})
	public @ResponseBody
	Object searchQuestionsByTag(@RequestParam("tag") String tag, @RequestParam("pageAt") int pageAt, @RequestParam("pageSize") int pageSize, HttpServletRequest request, HttpServletResponse response) {
		Page<QuestionIndex> resultsList = questionService.searchQuestionByTag(tag, pageAt, pageSize);
		return getPaginatedResults(resultsList, QuestionIndexBean.class, indexMapper);
	}
	
	@RequestMapping(value = URI_QUESTION_SEARCH, method = GET, params="keyword")
	public @ResponseBody
	Object searchIndexedQuestionsByTitle(@RequestParam("keyword") String keyword, HttpServletRequest request, HttpServletResponse response) {
		Page<QuestionIndex> indexedQuestions = questionService.searchIndexedQuestionByTitle(keyword, 0, 100);
		return getPaginatedResults(indexedQuestions, QuestionIndexBean.class, indexMapper);
	}
	
	@RequestMapping(value = URI_QUESTION_SEARCH, method = GET, params={"keyword","showSimilar=true"})
	public @ResponseBody
	Object searchSimilarIndexedQuestions(@RequestParam("keyword") String keyword, @RequestParam("pageAt") int pageAt, @RequestParam("pageSize") int pageSize, HttpServletRequest request, HttpServletResponse response) {
		Page<QuestionIndex> indexedQuestions = questionService.searchSimilarIndexedQuestion(keyword, pageAt, pageSize);
		return getPaginatedResults(indexedQuestions, QuestionIndexBean.class, indexMapper);
	}
	
	@RequestMapping(value = URI_QUESTION_SEARCH, method = GET, params={"keyword","excludeDocId","showSimilar=true"})
	public @ResponseBody
	Object searchSimilarIndexedQuestions(@RequestParam("keyword") String keyword, @RequestParam("excludeDocId") long excludeDocId, @RequestParam("pageAt") int pageAt, @RequestParam("pageSize") int pageSize, HttpServletRequest request, HttpServletResponse response) {
		Page<QuestionIndex> indexedQuestions = questionService.searchSimilarIndexedQuestion(keyword, excludeDocId, pageAt, pageSize);
		return getPaginatedResults(indexedQuestions, QuestionIndexBean.class, indexMapper);
	}
	
	@RequestMapping(value = URI_QUESTION_TOTAL_VIEW, method = RequestMethod.POST)
	public @ResponseBody
	Object doIncrementTotalViewsForQuestion(@PathVariable long id, HttpServletRequest request, HttpServletResponse response) {
		questionService.incrementTotalViewsCount(id);
		return null;
	}
	
	@RequestMapping(value = URI_QUESTIONS, method = RequestMethod.POST, params="action=reindex")
	public @ResponseBody
	Object reindexQuestions(HttpServletRequest request, HttpServletResponse response) throws SolrServerException, IOException {
		questionService.reIndex();
		return null;
	}
	
	@RequestMapping(value = URI_QUESTION_GET, method = RequestMethod.POST, params="action=flag")
	public @ResponseBody
	Object flagQuestion(@PathVariable long id, HttpServletRequest request, HttpServletResponse response) {
		Question question = questionService.getByDocId(id);
		FlagNotificationRequest flagNotificationRequest = questionService.flagQuestion(question);
		flagNotification.sendNotification(flagNotificationRequest, question);
		return null;
	}
	
	@RequestMapping(value = URI_QUESTION_DELETE, method = RequestMethod.DELETE)
	public @ResponseBody
	Object deleteQuestion(@PathVariable ObjectId id, HttpServletRequest request, HttpServletResponse response) {
		super.deleteResource(id, request, response);
		return null;
	}
	
	@RequestMapping(value = URI_QUESTION_ANONYMOUS_TOGGLE, method = RequestMethod.POST)
	public @ResponseBody
	Object doUpdateQuestionPostType(@PathVariable long id, @RequestParam("anonymous") boolean anonymous,HttpServletRequest request, HttpServletResponse response) {
		Question question = questionService.getByDocId(id);
		question.setAnonymous(anonymous);
		return questionService.saveOrUpdate(question);
	}
	
	@RequestMapping(value = URI_QUESTION_UPDATE_TAGS, method = RequestMethod.POST)
	public @ResponseBody
	Object doAddQuestionTags(@PathVariable long id, @PathVariable String tag, HttpServletRequest request, HttpServletResponse response) {
		Question question = questionService.getByDocId(id);
		question.addTag(tag);
		return questionService.saveOrUpdate(question);
	}
	
	@RequestMapping(value = URI_QUESTION_UPDATE_TAGS, method = RequestMethod.DELETE)
	public @ResponseBody
	Object doRemoveQuestionTags(@PathVariable long id, @PathVariable String tag, HttpServletRequest request, HttpServletResponse response) {
		Question question = questionService.getByDocId(id);
		question.removeTag(tag);
		return questionService.saveOrUpdate(question);
	}
}

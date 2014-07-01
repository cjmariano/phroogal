package com.phroogal.web.controller.service;

import static com.phroogal.web.context.WebApplicationContext.URI_ANSWERS;
import static com.phroogal.web.context.WebApplicationContext.URI_ANSWERS_ID;
import static com.phroogal.web.context.WebApplicationContext.URI_ANSWERS_ID_VOTE;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.phroogal.core.domain.Answer;
import com.phroogal.core.domain.FlagNotificationRequest;
import com.phroogal.core.domain.VoteActionType;
import com.phroogal.core.service.AnswerService;
import com.phroogal.core.service.AuthenticationDetailsService;
import com.phroogal.core.service.Service;
import com.phroogal.web.bean.AnswerBean;
import com.phroogal.web.controller.rest.RestPatchRequest;
import com.phroogal.web.notification.FlagNotification;
import com.wordnik.swagger.annotations.Api;

@Controller
@Api(value="answer", description="Answer Operations", position = 7)
public class AnswerController extends BasicController<Answer, AnswerBean, ObjectId> {
	
	@Autowired
	private AnswerService answerService; 
	
	@Autowired
	private AuthenticationDetailsService<String> authenticationDetailsService;
	
	@Autowired
	@Qualifier(value="flagNotification")
	private FlagNotification flagNotification;
	
	@Override
	protected Service<Answer, ObjectId> returnDomainService() {
		return answerService;
	}
	
	@RequestMapping(value = URI_ANSWERS, method = RequestMethod.POST)
	public @ResponseBody
	Object addUpdateAnswer(HttpServletRequest request, HttpServletResponse response, @RequestBody AnswerBean answerBean) {
		return super.addUpdateResource(request, response, answerBean);
	}
	
	@RequestMapping(value = URI_ANSWERS_ID, method = RequestMethod.GET)
	public @ResponseBody
	Object getAnswer(@PathVariable ObjectId id, HttpServletRequest request, HttpServletResponse response) {
		return super.getResource(id, request, response);
	}
	
	@RequestMapping(value = URI_ANSWERS, method = RequestMethod.GET, params={"postBy","pageAt","pageSize"})
	public @ResponseBody
	Object getAnswersByUser(@RequestParam("postBy") ObjectId userId, @RequestParam("pageAt") int pageAt, @RequestParam("pageSize") int pageSize, HttpServletRequest request, HttpServletResponse response) {
		Page<Answer> resultsList = answerService.getByUserId(userId, pageAt, pageSize);
		return getPaginatedResults(resultsList, AnswerBean.class);
	}
	
	@RequestMapping(value = URI_ANSWERS_ID, method = RequestMethod.PATCH)
	public @ResponseBody Object doPatchResource(@PathVariable ObjectId id, HttpServletRequest request, HttpServletResponse response, @RequestBody List<RestPatchRequest> patchRequest) throws Exception {
		return super.doPatchResource(id, request, response, patchRequest);
	}

	@RequestMapping(value = URI_ANSWERS, method = RequestMethod.GET)
	public @ResponseBody
	Object getAllAnswers(HttpServletRequest request, HttpServletResponse response) {
		return super.getAllResources(request, response);
	}
	
	@RequestMapping(value = URI_ANSWERS, method = RequestMethod.GET, params={"pageAt", "pageSize"})
	public @ResponseBody
	Object getAllAnswers(@RequestParam("pageAt") int pageAt, @RequestParam("pageSize") int pageSize, HttpServletRequest request, HttpServletResponse response) {
		return super.getAllResources(pageAt, pageSize, request, response);
	}
	
	@RequestMapping(value = URI_ANSWERS, method = RequestMethod.GET, params={"showMostRecent=true","pageAt","pageSize"})
	public @ResponseBody
	Object getMostRecentAnswers(@RequestParam("pageAt") int pageAt, @RequestParam("pageSize") int pageSize, HttpServletRequest request, HttpServletResponse response) {
		Page<Answer> resultsList = answerService.getRecentAnswers(pageAt, pageSize);
		return getPaginatedResults(resultsList, AnswerBean.class);
	}
	
	@RequestMapping(value = URI_ANSWERS, method = RequestMethod.GET, params={"showFlagged=true", "pageAt", "pageSize"})
	public @ResponseBody
	Object getFlaggedAnswers(@RequestParam("pageAt") int pageAt, @RequestParam("pageSize") int pageSize, HttpServletRequest request, HttpServletResponse response) {
		Page<Answer> resultsList = answerService.getFlaggedAnswers(pageAt, pageSize);
		return getPaginatedResults(resultsList, AnswerBean.class);
	}
	
	@RequestMapping(value = URI_ANSWERS_ID_VOTE, method = RequestMethod.POST)
	public @ResponseBody
	Object doUpdateAnswersVote(@PathVariable ObjectId id, @RequestParam("action") String action, HttpServletRequest request, HttpServletResponse response) {
		VoteActionType voteAction = VoteActionType.get(action);
		Answer answer = answerService.findById(id);
		answer.applyVote(authenticationDetailsService.getAuthenticatedUser().getId(), voteAction);
		answerService.saveOrUpdate(answer);
		return getObjectMapper().toBean(answer, AnswerBean.class);
	}
	
	@RequestMapping(value = URI_ANSWERS_ID, method = RequestMethod.POST, params="action=flag")
	public @ResponseBody
	Object flagAnswer(@PathVariable ObjectId id, HttpServletRequest request, HttpServletResponse response) {
		Answer answer = answerService.findById(id);
		FlagNotificationRequest flagNotificationRequest = answerService.flagAnswer(answer);
		flagNotification.sendNotification(flagNotificationRequest, answer);
		return null;
	}
	
	@RequestMapping(value = URI_ANSWERS_ID, method = RequestMethod.DELETE)
	public @ResponseBody
	Object deleteAnswer(@PathVariable ObjectId id, HttpServletRequest request, HttpServletResponse response) {
		super.deleteResource(id, request, response);
		return null;
	}
}

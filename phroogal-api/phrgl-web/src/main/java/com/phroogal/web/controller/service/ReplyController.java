package com.phroogal.web.controller.service;

import static com.phroogal.web.context.WebApplicationContext.URI_REPLY_GET;
import static com.phroogal.web.context.WebApplicationContext.URI_REPLY_GET_ALL;
import static com.phroogal.web.context.WebApplicationContext.URI_REPLY_PARTIAL_POST;
import static com.phroogal.web.context.WebApplicationContext.URI_REPLY_POST;
import static com.phroogal.web.context.WebApplicationContext.URI_REPLY_PREFIX;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.phroogal.core.domain.Reply;
import com.phroogal.core.service.ReplyService;
import com.phroogal.core.service.Service;
import com.phroogal.web.bean.ReplyBean;
import com.phroogal.web.controller.rest.RestPatchRequest;
import com.wordnik.swagger.annotations.Api;

@Controller
@Api(value="reply", description="Reply Operations", position = 9)
@RequestMapping(URI_REPLY_PREFIX)
public class ReplyController extends BasicController<Reply, ReplyBean, ObjectId> {
	
	@Autowired
	private ReplyService replyService; 
	
	@Override
	protected Service<Reply, ObjectId> returnDomainService() {
		return replyService;
	}
	
	@RequestMapping(value = URI_REPLY_POST, method = RequestMethod.POST)
	public @ResponseBody
	Object addUpdateReply(HttpServletRequest request, HttpServletResponse response, @RequestBody ReplyBean replyBean) {
		return super.addUpdateResource(request, response, replyBean);
	}
	
	@RequestMapping(value = URI_REPLY_GET, method = RequestMethod.GET)
	public @ResponseBody
	Object getReply(@PathVariable ObjectId id, HttpServletRequest request, HttpServletResponse response) {
		return super.getResource(id, request, response);
	}
	
	@RequestMapping(value = URI_REPLY_PARTIAL_POST, method = RequestMethod.PATCH)
	public @ResponseBody Object doPatchResource(@PathVariable ObjectId id, HttpServletRequest request, HttpServletResponse response, @RequestBody List<RestPatchRequest> patchRequest) throws Exception {
		return super.doPatchResource(id, request, response, patchRequest);
	}
	
	@RequestMapping(value = URI_REPLY_GET_ALL, method = RequestMethod.GET)
	public @ResponseBody
	Object getAllReplies(HttpServletRequest request, HttpServletResponse response) {
		return super.getAllResources(request, response);
	}
}

package com.phroogal.web.controller.service;

import static com.phroogal.web.context.WebApplicationContext.URI_COMMENTS;
import static com.phroogal.web.context.WebApplicationContext.URI_COMMENTS_ID;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.phroogal.core.domain.Comment;
import com.phroogal.core.service.CommentService;
import com.phroogal.core.service.Service;
import com.phroogal.web.bean.CommentBean;
import com.phroogal.web.controller.rest.RestPatchRequest;
import com.wordnik.swagger.annotations.Api;

@Controller
@Api(value="comment", description="Comment Operations", position = 8)
public class CommentController extends BasicController<Comment, CommentBean, ObjectId> {
	
	@Autowired
	private CommentService commentService; 
	
	@Override
	protected Service<Comment, ObjectId> returnDomainService() {
		return commentService;
	}
	
	@RequestMapping(value = URI_COMMENTS, method = RequestMethod.POST)
	public @ResponseBody
	Object addUpdateComment(HttpServletRequest request, HttpServletResponse response, @RequestBody CommentBean commentBean) {
		return super.addUpdateResource(request, response, commentBean);
	}
	
	@RequestMapping(value = URI_COMMENTS_ID, method = RequestMethod.GET)
	public @ResponseBody
	Object getComment(@PathVariable ObjectId id, HttpServletRequest request, HttpServletResponse response) {
		return super.getResource(id, request, response);
	}
	
	@RequestMapping(value = URI_COMMENTS_ID, method = RequestMethod.PATCH)
	public @ResponseBody Object doPatchResource(@PathVariable ObjectId id, HttpServletRequest request, HttpServletResponse response, @RequestBody List<RestPatchRequest> patchRequest) throws Exception {
		return super.doPatchResource(id, request, response, patchRequest);
	}

	@RequestMapping(value = URI_COMMENTS, method = RequestMethod.GET)
	public @ResponseBody
	Object getAllComments(HttpServletRequest request, HttpServletResponse response) {
		return super.getAllResources(request, response);
	}
	
	@RequestMapping(value = URI_COMMENTS, method = RequestMethod.GET, params={"pageAt", "pageSize"})
	public @ResponseBody
	Object getAllComments(@RequestParam("pageAt") int pageAt, @RequestParam("pageSize") int pageSize, HttpServletRequest request, HttpServletResponse response) {
		return super.getAllResources(pageAt, pageSize, request, response);
	}
}

package com.phroogal.web.controller.service;


import static com.phroogal.web.context.WebApplicationContext.URI_TAG_GET_ALL;
import static com.phroogal.web.context.WebApplicationContext.URI_USER_TAG_POST;
import static com.phroogal.web.context.WebApplicationContext.URI_USER_TAG_DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

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

import com.phroogal.core.domain.UserTag;
import com.phroogal.core.service.Service;
import com.phroogal.core.service.UserTagService;
import com.phroogal.web.bean.UserTagBean;

@Controller
public class UserTagController extends BasicController<UserTag, UserTagBean, ObjectId> {

	@Autowired
	private UserTagService userTagService; 
	
	@Override
	protected Service<UserTag, ObjectId> returnDomainService() {
		return userTagService;
	}
	
	@RequestMapping(value = URI_USER_TAG_POST, method = RequestMethod.POST)
	public @ResponseBody
	Object addUpdateUserTag(@PathVariable String id, HttpServletRequest request, HttpServletResponse response, @RequestBody UserTagBean userTagBean) {
		userTagBean.setUserId(id);
		return super.addUpdateResource(request, response, userTagBean);
	}
	
	@RequestMapping(value = URI_TAG_GET_ALL, method = GET, params="userId")
	public @ResponseBody
	Object getUserTagsByUserId(HttpServletRequest request, HttpServletResponse response, @RequestParam String userId) {
		List<UserTag> userTags = userTagService.getByUserId(new ObjectId(userId));
		return getObjectMapper().toBean(userTags, UserTagBean.class);
	}
	
	@RequestMapping(value = URI_USER_TAG_DELETE, method = RequestMethod.DELETE)
	public @ResponseBody
	Object deleteUserTag(@PathVariable ObjectId id, HttpServletRequest request, HttpServletResponse response) {
		super.deleteResource(id, request, response);
		return null;
	}
}

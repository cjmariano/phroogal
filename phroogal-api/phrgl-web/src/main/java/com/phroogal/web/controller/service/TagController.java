package com.phroogal.web.controller.service;


import static com.phroogal.web.context.WebApplicationContext.URI_TAG_GET_ALL;
import static com.phroogal.web.context.WebApplicationContext.URI_TAG_SEARCH;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.phroogal.core.domain.Tag;
import com.phroogal.core.service.Service;
import com.phroogal.core.service.TagService;
import com.phroogal.web.bean.TagBean;
import com.wordnik.swagger.annotations.Api;

@Controller
@Api(value="tag", description="Tag Operations", position = 13)
public class TagController extends BasicController<Tag, TagBean, ObjectId> {

	@Autowired
	private TagService tagService; 
	
	@Override
	protected Service<Tag, ObjectId> returnDomainService() {
		return tagService;
	}
	
	@RequestMapping(value = URI_TAG_GET_ALL, method = RequestMethod.GET)
	public @ResponseBody
	Object getAllTag(HttpServletRequest request, HttpServletResponse response) {
		return super.getAllResources(request, response);
	}
	
	@RequestMapping(value = URI_TAG_GET_ALL, method = GET, params={"pageAt", "pageSize"})
	public @ResponseBody
	Object getAllTags(@RequestParam("pageAt") int pageAt, @RequestParam("pageSize") int pageSize, HttpServletRequest request, HttpServletResponse response) {
		Page<Tag> tags = tagService.getTagsSortByAscName(pageAt, pageSize);
		return getPaginatedResults(tags, TagBean.class);
	}
	
	@RequestMapping(value = URI_TAG_GET_ALL, method = GET, params={"pageAt", "pageSize", "sortByQuestionsTagged=true"})
	public @ResponseBody
	Object getAllTagsSortByDescTotalNumQuestionsTagged(@RequestParam("pageAt") int pageAt, @RequestParam("pageSize") int pageSize, HttpServletRequest request, HttpServletResponse response) {
		Page<Tag> tags = tagService.getTagsSortByDescTotalNumQuestionsTagged (pageAt, pageSize);
		return getPaginatedResults(tags, TagBean.class);
	}
	
	@RequestMapping(value = URI_TAG_GET_ALL, method = RequestMethod.POST)
	public @ResponseBody
	Object addUpdateTagList(HttpServletRequest request, HttpServletResponse response, @RequestBody List<TagBean> tagBeans) {
		return super.addUpdateResourceList(request, response, tagBeans);
	}
	
	@RequestMapping(value = URI_TAG_SEARCH, method = GET, params="keyword")
	public @ResponseBody
	Object searchIndexedTagName(@RequestParam String keyword, HttpServletRequest request, HttpServletResponse response) {
		List<Tag> tags = tagService.findByNameStartingWith(keyword);
		return getObjectMapper().toBean(tags, TagBean.class);
	}
	
	@RequestMapping(value = URI_TAG_GET_ALL, method = GET, params={"pageAt", "pageSize", "showTrending=true"})
	public @ResponseBody
	Object searchTrendingTags(@RequestParam("pageAt") int pageAt, @RequestParam("pageSize") int pageSize, HttpServletRequest request, HttpServletResponse response) {
		Page<Tag> tags = tagService.getTrendingTags(pageAt, pageSize);
		return getPaginatedResults(tags, TagBean.class);
	}
}

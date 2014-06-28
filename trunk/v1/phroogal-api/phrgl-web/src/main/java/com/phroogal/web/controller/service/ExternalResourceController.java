package com.phroogal.web.controller.service;

import static com.phroogal.web.context.WebApplicationContext.URI_EXTERNAL_RESOURCE_SEARCH;
import static com.phroogal.web.context.WebApplicationContext.URI_EXTERNAL_RESOURCE_SEARCH_ALL;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.phroogal.core.domain.ExternalResource;
import com.phroogal.core.service.ExternalResourceService;
import com.phroogal.core.utility.CollectionUtil;
import com.phroogal.web.bean.ExternalResourceBean;
import com.phroogal.web.bean.ExternalResourceIndexBean;
import com.phroogal.web.bean.mapper.MapperService;
import com.wordnik.swagger.annotations.Api;

@Controller
@Api(value="external resource", description="External Resource Operations", position = 15)
public class ExternalResourceController {
	
	private static final int DEFAULT_RESULTS_COUNT = 10;
	
	private static final int START_INDEX_MIN = 1;
	
	private static final int INDEX_MAX_COUNT = 101;
	
	
	@Autowired
	@Qualifier("externalResourceService")
	private ExternalResourceService externalResourceService;
	
	@Autowired
	private MapperService<ExternalResource, ExternalResourceIndexBean> externalResourceMapper;
	
	@RequestMapping(value = URI_EXTERNAL_RESOURCE_SEARCH_ALL, method = RequestMethod.GET)
	public @ResponseBody
	Object searchAllExternalResourceByTitle(@RequestParam("keyword") String keyword, @RequestParam(defaultValue="1") Long start, HttpServletRequest request, HttpServletResponse response) {
		List<ExternalResource> externalResource = externalResourceService.getResourceByKeyword(keyword, start);
		return returnExternalSearchResults(externalResource, start, request);
	}
	
	@RequestMapping(value = URI_EXTERNAL_RESOURCE_SEARCH, method = RequestMethod.GET)
	public @ResponseBody
	Object searchExternalResourceByTitle(@PathVariable("category") String category, @RequestParam("keyword") String keyword, @RequestParam(defaultValue="1") Long start, HttpServletRequest request, HttpServletResponse response) {
		List<ExternalResource> externalResource = externalResourceService.getResourceByKeyword(category, keyword, start);
		return returnExternalSearchResults(externalResource, start, request);
	}

	private Object returnExternalSearchResults(List<ExternalResource> externalResource, Long start, HttpServletRequest request) {
		ExternalResourceBean externalResourceBean = new ExternalResourceBean();
		externalResourceBean.setPreviousPageResoure(generateReferencePageLink(externalResource.size(), start - DEFAULT_RESULTS_COUNT, request));
		externalResourceBean.setNextPageResoure(generateReferencePageLink(externalResource.size(), start + DEFAULT_RESULTS_COUNT, request));
		externalResourceBean.setResults(listResultsoBean(externalResource));
		return externalResourceBean;
	}

	private List<ExternalResourceIndexBean> listResultsoBean( List<ExternalResource> externalResource) {
		List<ExternalResourceIndexBean> results = CollectionUtil.arrayList();
		for (ExternalResource each : externalResource) {
			ExternalResourceIndexBean bean = externalResourceMapper.toBean(each, ExternalResourceIndexBean.class);
			results.add(bean);
		}
		return results;
	}

	private String generateReferencePageLink(int currentResultsSize, Long start, HttpServletRequest request) {
		if (! indexHasExceededAllowableBounds(currentResultsSize, start) ) { 
			return constructPageReferenceLink(start, request); 
		}
		return null;
	}

	private String constructPageReferenceLink(Long start, HttpServletRequest request) {
		StringBuffer sb = request.getRequestURL();
		sb.append("?");
		sb.append(request.getQueryString());
		sb.replace(sb.lastIndexOf("start=") + 6, sb.length(), start.toString());  
		return sb.toString();
	}

	private boolean indexHasExceededAllowableBounds(int currentResultsSize, Long start) {
		return start < START_INDEX_MIN || start > INDEX_MAX_COUNT - DEFAULT_RESULTS_COUNT || currentResultsSize < DEFAULT_RESULTS_COUNT;
	}
}

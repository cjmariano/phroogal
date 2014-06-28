package com.phroogal.web.controller.service;

import static com.phroogal.web.context.WebApplicationContext.URI_LOCATION_SEARCH;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.phroogal.core.domain.LocationIndex;
import com.phroogal.core.service.LocationService;
import com.phroogal.core.utility.CollectionUtil;
import com.phroogal.web.bean.LocationIndexBean;
import com.phroogal.web.bean.mapper.MapperService;
import com.wordnik.swagger.annotations.Api;

@Controller
@Api(value="location", description="Location Operations", position = 10)
public class LocationController  {
	
	@Autowired
	private LocationService locationService;
	
	@Autowired
	private MapperService<LocationIndex, LocationIndexBean> locationMapper;
	
	@RequestMapping(value = URI_LOCATION_SEARCH, method = RequestMethod.GET)
	public @ResponseBody
	Object queryLocationsByKeyword(@RequestParam("keyword") String keyword, HttpServletRequest request, HttpServletResponse response) {
		List<LocationIndex> locations = locationService.queryCitiesByKeyword(keyword);
		return generateResponseList(locations);
	}

	private List<LocationIndexBean> generateResponseList(List<LocationIndex> locations) {
		List<LocationIndexBean> results = CollectionUtil.arrayList();
		for (LocationIndex locationIndex : locations) {
			LocationIndexBean bean = locationMapper.toBean(locationIndex, LocationIndexBean.class);
			results.add(bean);
		}
		return results;
	}
}

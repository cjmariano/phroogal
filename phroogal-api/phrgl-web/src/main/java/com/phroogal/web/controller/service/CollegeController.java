package com.phroogal.web.controller.service;

import static com.phroogal.web.context.WebApplicationContext.URI_COLLEGES;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.phroogal.core.domain.College;
import com.phroogal.core.service.CollegeService;
import com.phroogal.core.service.Service;
import com.phroogal.core.utility.CollectionUtil;
import com.phroogal.web.bean.CollegeBean;
import com.phroogal.web.bean.mapper.MapperService;
import com.wordnik.swagger.annotations.Api;

@Controller
@Api(value="college", description="College Operations", position = 11)
public class CollegeController extends BasicController<College, CollegeBean, ObjectId> {

	@Autowired
	private CollegeService collegeService; 
	
	@Autowired
	private MapperService<College, CollegeBean> indexMapper; 
	
	@Override
	protected Service<College, ObjectId> returnDomainService() {
		return collegeService;
	}
	
	@RequestMapping(value = URI_COLLEGES, method = RequestMethod.GET)
	public @ResponseBody
	Object getAllColleges(HttpServletRequest request, HttpServletResponse response) {
		return super.getAllResources(request, response);
	}
	@RequestMapping(value = URI_COLLEGES, method = RequestMethod.POST)
	public @ResponseBody
	Object addUpdateQuestion(HttpServletRequest request, HttpServletResponse response, @RequestBody CollegeBean collegeBean) {
		return super.addUpdateResource(request, response, collegeBean);
	}
	
	@RequestMapping(value = URI_COLLEGES, method = GET, params="keyword")
	public @ResponseBody
	Object searchIndexedCollegesByTitle(@RequestParam String keyword, HttpServletRequest request, HttpServletResponse response) {
		List<College> indexedColleges = collegeService.findByNameContaining(keyword);
		List<CollegeBean> results = CollectionUtil.arrayList();
		for (College college : indexedColleges) {
			CollegeBean bean = indexMapper.toBean(college, CollegeBean.class);
			results.add(bean);
		}
		return results;
	}
	
}

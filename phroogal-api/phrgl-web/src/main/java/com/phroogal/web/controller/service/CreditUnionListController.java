package com.phroogal.web.controller.service;

import static com.phroogal.web.context.WebApplicationContext.URI_CREDIT_UNION_GET_ALL;
import static com.phroogal.web.context.WebApplicationContext.URI_CREDIT_UNION_PREFIX;
import static com.phroogal.web.context.WebApplicationContext.URI_CREDIT_UNION_SEARCH;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.phroogal.core.domain.CreditUnion;
import com.phroogal.core.service.CreditUnionService;
import com.phroogal.core.service.Service;
import com.phroogal.web.bean.CreditUnionBean;

@Controller
@RequestMapping(URI_CREDIT_UNION_PREFIX)
public class CreditUnionListController extends BasicController<CreditUnion, CreditUnionBean, ObjectId> {

	@Autowired
	private CreditUnionService creditUnionService; 
	
	@Override
	protected Service<CreditUnion, ObjectId> returnDomainService() {
		return creditUnionService;
	}
	
	@RequestMapping(value = URI_CREDIT_UNION_GET_ALL, method = RequestMethod.GET)
	public @ResponseBody
	Object getAllCreditUnion(HttpServletRequest request, HttpServletResponse response) {
		return super.getAllResources(request, response);
	}
	
	@RequestMapping(value = URI_CREDIT_UNION_SEARCH, method = GET, params="keyword")
	public @ResponseBody
	Object searchIndexedCreditUniondsByName(@RequestParam String keyword, HttpServletRequest request, HttpServletResponse response) {
		List<CreditUnion> results = creditUnionService.searchByName(keyword);
		return getObjectMapper().toBean(results, CreditUnionBean.class);
	}		
}

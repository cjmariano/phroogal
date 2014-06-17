package com.phroogal.web.controller.service;

import static com.phroogal.web.context.WebApplicationContext.URI_BRAND_GET;
import static com.phroogal.web.context.WebApplicationContext.URI_BRAND_NAME_SEARCH;
import static com.phroogal.web.context.WebApplicationContext.URI_BRAND_POST;
import static com.phroogal.web.context.WebApplicationContext.URI_BRAND_SEARCH;
import static com.phroogal.web.context.WebApplicationContext.URI_BRAND_URL_SEARCH;
import static com.phroogal.web.context.WebApplicationContext.URI_QUESTION_PREFIX;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.io.IOException;
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

import com.phroogal.core.domain.Brand;
import com.phroogal.core.service.BrandService;
import com.phroogal.core.service.Service;
import com.phroogal.web.bean.BrandBean;
import com.phroogal.web.bean.BrandPreviewBean;
import com.phroogal.web.bean.mapper.MapperService;

@Controller
@RequestMapping(URI_QUESTION_PREFIX)
public class BrandController extends BasicController<Brand, BrandBean, ObjectId> {
	
	@Autowired
	private BrandService brandService;
	
	@Autowired
	private MapperService<Brand, BrandPreviewBean> brandPreviewMapper;
	
	
	@Override
	protected Service<Brand, ObjectId> returnDomainService() {
		return brandService;
	}
	
	@RequestMapping(value = URI_BRAND_POST, method = RequestMethod.POST)
	public @ResponseBody
	Object addBrandProfile(HttpServletRequest request, HttpServletResponse response,@RequestBody BrandBean brandBean) throws IOException {
		return  super.addUpdateResource(request, response, brandBean);
	}
	
	@RequestMapping(value = URI_BRAND_GET, method = RequestMethod.GET)
	public @ResponseBody
	Object getBrand(@PathVariable ObjectId id, HttpServletRequest request, HttpServletResponse response) {
		Brand brand = brandService.findById(id);
		return getObjectMapper().toBean(brand, BrandBean.class);
	}
	
	@RequestMapping(value = URI_BRAND_NAME_SEARCH, method = GET, params="name")
	public @ResponseBody Object searchBrandsByName(@RequestParam("name") String name, HttpServletRequest request, HttpServletResponse response) {
		Brand brand = brandService.getBrandByName(name);
		return getObjectMapper().toBean(brand, BrandBean.class);
	}
     
	@RequestMapping(value = URI_BRAND_URL_SEARCH, method = GET, params="url")
	public @ResponseBody Object searchBrandsByUrl(@RequestParam("url") String url, HttpServletRequest request, HttpServletResponse response) {
		Brand brand = brandService.getBrandByUrl(url);
		return getObjectMapper().toBean(brand, BrandBean.class);
	}
	
	@RequestMapping(value = URI_BRAND_SEARCH, method = GET, params="keyword")
	public @ResponseBody
	Object searchIndexedBrandByName(@RequestParam("keyword") String keyword, HttpServletRequest request, HttpServletResponse response) {
		List<Brand> brands = brandService.searchAutoSuggestBrandByName(keyword, 0, 100);
		return brandPreviewMapper.toBean(brands, BrandPreviewBean.class);
	}
	
	@RequestMapping(value = URI_BRAND_SEARCH, method = GET, params={"keyword","topReview=true"})
	public @ResponseBody
	Object searchBrandByNameKeyword(@RequestParam("keyword") String keyword, @RequestParam("pageAt") int pageAt, @RequestParam("pageSize") int pageSize, HttpServletRequest request, HttpServletResponse response) {
		List<Brand> brands = brandService.searchBrandByNameKeyword(keyword,pageAt, pageSize);
		return brandPreviewMapper.toBean(brands, BrandPreviewBean.class);
		
	}
	
   	
}

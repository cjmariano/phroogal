package com.phroogal.core.service.impl;

import java.util.Arrays;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.phroogal.core.domain.Brand;
import com.phroogal.core.domain.Review;
import com.phroogal.core.repository.BrandRepository;
import com.phroogal.core.service.BrandService;
import com.phroogal.core.service.FileUploadService;
import com.phroogal.core.service.ReviewService;


/**
 * Default implementation of the {@link BrandService} interface
 *
 */
@Service(value="brandService")
public class BrandServiceImpl extends BaseService<Brand, ObjectId, BrandRepository> implements BrandService{

	@Autowired
	private BrandRepository brandRepository;
	
	@Autowired
	private ReviewService reviewService;
	
	
	@Autowired
	private FileUploadService fileUploadService;
	
	@Override
	protected BrandRepository getRepository() {
		
		return brandRepository;
	}
	
	@Override
	protected void onAfterFindById(Brand brand) {
		attachReviewsToBrand(brand);
		List<Review> reviews = brand.getReviews();
		if (reviews != null && reviews.size() > 0) {
			reviewService.sortByReviewHierarchyRules(reviews);
			brand.setTopReview(reviews.get(0));	
		}
	}
	
	@Override
	public Brand getBrandByName(String name) {
		Brand brand = brandRepository.findByName(name);
		attachReviewsToBrand(brand);
		return brand;
	}

	public Brand getBrandByUrl(String url){
		Brand brand = brandRepository.findByUrl(url);
		return brand;
	}

	@Override
	public List<Brand> searchBrandByName(String keyword,int pageAt, int pageSize) {
		Pageable pageRequest = generatePageRequest(pageAt, pageSize);		
		return brandRepository.searchBrands(getSerchRegex(keyword),pageRequest);
	}
	
	@Override	
	public List<Brand> searchAutoSuggestBrandByName(String keyword, int pageAt, int pageSize){		
		Pageable pageRequest = generatePageRequest(pageAt, pageSize);
		List<Brand> brands = brandRepository.searchBrands(getSerchRegex(keyword),pageRequest);
		return brands;
	}
	
	@Override
	public List<Brand> searchBrandByNameKeyword(String keyword, int pageAt, int pageSize){
		Pageable pageRequest = generatePageRequest(pageAt, pageSize);
		List<Brand> brands = brandRepository.searchBrands(getSerchRegex(keyword),pageRequest);
		if (brands!= null && !brands.isEmpty()) {
			for (Brand brand : brands) {
				attachReviewsToBrand(brand);
			}
		}		
		return brands;
	}
	
	private void attachReviewsToBrand(Brand brand) {
		List<Review> reviews = reviewService.getByBrandRef(brand.getId());
		//attachCommentsToReviews(brands);
		if(reviews!=null && !reviews.isEmpty()){
			brand.setReviews(reviews);
			brand.setTotalReviewCount(reviews.size());
			brand.setReviewd(true);
		}
	}
	private String getSerchRegex(String keyword){		
		String[] keywords = keyword.split(" ");
		List<String> keywordList = Arrays.asList(keywords);		
		String regex1 = ".*(";
		String regex3=").*";
		String regex2 =keywordList.get(0);		
		int i = 0;
		for(String s: keywordList)
		{
			if(i > 0){
				regex2=regex2+"|"+s;				
			}
			i++;
		}
		String resRegex = regex1+regex2+regex3;
		return resRegex;
	}
}

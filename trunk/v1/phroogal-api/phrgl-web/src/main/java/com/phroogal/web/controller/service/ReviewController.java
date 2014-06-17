package com.phroogal.web.controller.service;

import static com.phroogal.web.context.WebApplicationContext.URI_REVIEW_BY_BRAND_GET;
import static com.phroogal.web.context.WebApplicationContext.URI_REVIEW_GET;
import static com.phroogal.web.context.WebApplicationContext.URI_REVIEW_GET_ALL;
import static com.phroogal.web.context.WebApplicationContext.URI_REVIEW_POST;
import static com.phroogal.web.context.WebApplicationContext.URI_REVIEW_PREFIX;
import static com.phroogal.web.context.WebApplicationContext.URI_REVIEW_VOTE_POST;

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

import com.phroogal.core.domain.Review;
import com.phroogal.core.domain.VoteActionType;
import com.phroogal.core.service.AuthenticationDetailsService;
import com.phroogal.core.service.ReviewService;
import com.phroogal.core.service.Service;
import com.phroogal.web.bean.ReviewBean;

@Controller
@RequestMapping(URI_REVIEW_PREFIX)
public class ReviewController extends BasicController<Review, ReviewBean, ObjectId> {
	
	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private AuthenticationDetailsService<String> authenticationDetailsService;
	
	@Override
	protected Service<Review, ObjectId> returnDomainService() {
		return reviewService;
	}
	
	@RequestMapping(value = URI_REVIEW_POST, method = RequestMethod.POST)
	public @ResponseBody
	Object addUpdateAnswer(HttpServletRequest request, HttpServletResponse response, @RequestBody ReviewBean reviewBean) {
		return super.addUpdateResource(request, response, reviewBean);
	}
	
	@RequestMapping(value = URI_REVIEW_BY_BRAND_GET, method = RequestMethod.GET)
	public @ResponseBody
	Object getReviews(@PathVariable ObjectId id, HttpServletRequest request, HttpServletResponse response) {
		return reviewService.getByBrandRef(id);
	}
	
	@RequestMapping(value = URI_REVIEW_GET, method = RequestMethod.GET)
	public @ResponseBody
	Object getReview(@PathVariable ObjectId id, HttpServletRequest request, HttpServletResponse response) {
		return reviewService.findById(id);
	}
	
	@RequestMapping(value = URI_REVIEW_GET_ALL, method = RequestMethod.GET, params={"postBy"})
	public @ResponseBody
	Object getReviewsByUser(@RequestParam("postBy") ObjectId userId, HttpServletRequest request, HttpServletResponse response) {
		List<Review> resultsList = reviewService.getByUserId(userId);
		return getObjectMapper().toBean(resultsList, ReviewBean.class);
	}
	@RequestMapping(value = URI_REVIEW_VOTE_POST, method = RequestMethod.POST)
	public @ResponseBody
	Object doUpdateAnswersVote(@PathVariable ObjectId id, @RequestParam("action") String action, HttpServletRequest request, HttpServletResponse response) {
		VoteActionType voteAction = VoteActionType.get(action);
		Review review = reviewService.findById(id);
		review.applyVote(authenticationDetailsService.getAuthenticatedUser().getId(), voteAction);
		reviewService.saveOrUpdate(review);
		return getObjectMapper().toBean(review, ReviewBean.class);
	}
	
}

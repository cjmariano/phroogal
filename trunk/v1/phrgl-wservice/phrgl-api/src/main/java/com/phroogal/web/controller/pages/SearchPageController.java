 
package com.phroogal.web.controller.pages;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.amazonaws.util.json.JSONException;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phroogal.web.bean.QuestionBean;
import com.phroogal.web.controller.rest.HttpRestWebServiceInvoker;
import com.phroogal.web.utility.JsonUtility;

@Controller
public class SearchPageController {
	
	private JsonUtility jsonUtility = new JsonUtility();
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String showSearchPage(Model model, WebRequest request, RedirectAttributes redirectAttributes) {
		return  "search";
	}
	@RequestMapping(value = "/search_home", method = RequestMethod.GET)
	public String showSearchResultPage(Model model, WebRequest request, RedirectAttributes redirectAttributes) {
		return  "search_home";
	}
	@RequestMapping(value = "/search_category", method = RequestMethod.GET)
	public String categorySearchPage(Model model, WebRequest request, RedirectAttributes redirectAttributes) {
		return  "search_category";
	}
	@RequestMapping(value = "/question/tag/{title}", method = RequestMethod.GET)
	public String showQuestionDetailPage(Model model,@PathVariable String title,WebRequest request,HttpServletRequest httpRequest, RedirectAttributes redirectAttributes) {
		model.addAttribute("title", title);
	    model.addAttribute("description",title);
		model.addAttribute("url", httpRequest.getRequestURL());	
		return  "question";
		
	}
	@RequestMapping(value = "/question/{id}/{title}", method = RequestMethod.GET)
	public String showQuestionDetailPage(Model model,@PathVariable long id,@PathVariable String title,WebRequest request,HttpServletRequest httpRequest, RedirectAttributes redirectAttributes) throws JSONException {
		title = title.replace("-"," ");
		title = title+"?";
		title = Character.toUpperCase(title.charAt(0)) + title.substring(1);
		model.addAttribute("id", id);
		model.addAttribute("title", title);
		model.addAttribute("url", httpRequest.getRequestURL());	
		String response = getQuestionByHttpRestService(httpRequest,id);
		String status =  (String) jsonUtility.getValueByKey(response, "status");
		if (response != null && status.equals("SUCCESS")){
			model.addAttribute("description",getPageDescriptionVal(response));
		}
		else{
			model.addAttribute("docFound",false);
		}
		return  "question";
	}
	@RequestMapping(value = "/review_rating_search", method = RequestMethod.GET)
	public String showBrandReviewRatingsPage(Model model, WebRequest request, RedirectAttributes redirectAttributes) {
		return  "review_rating_search";
	}
	@RequestMapping(value = "/trending", method = RequestMethod.GET)
	public String trendingQuestions(Model model, WebRequest request, RedirectAttributes redirectAttributes) {
		return  "search_trending";
	}
	@RequestMapping(value = "/unanswered", method = RequestMethod.GET)
	public String showSnansweredQuestionsPage(Model model, WebRequest request, RedirectAttributes redirectAttributes) {
		return  "search_unanswered";
	}
	@RequestMapping(value = "/discovertags", method = RequestMethod.GET)
	public String showDiscoverTagsPage(Model model, WebRequest request, RedirectAttributes redirectAttributes) {
		return  "discover_tag";
	}
	@RequestMapping(value = "/social", method = RequestMethod.GET)
	public String showSocialPage(Model model, WebRequest request, RedirectAttributes redirectAttributes) {
		return  "search_social";
	}
	@RequestMapping(value = "/addquestion", method = RequestMethod.GET)
	public String showAddNewQuestionPage(Model model, WebRequest request, RedirectAttributes redirectAttributes) {
		return  "search_add_question";
	}
	@RequestMapping(value = "/freetoolapp", method = RequestMethod.GET)
	public String showFreeAppToolPage(Model model, WebRequest request, RedirectAttributes redirectAttributes) {
		return  "free_app_tool";
	}
	@RequestMapping(value = "/recent", method = RequestMethod.GET)
	public String showRecentQuestionsPage(Model model, WebRequest request, RedirectAttributes redirectAttributes) {
		return  "search_recent";
	}
	@RequestMapping(value = "/trendingtags", method = RequestMethod.GET)
	public String showTrendingTagsPage(Model model, WebRequest request, RedirectAttributes redirectAttributes) {
		return  "trending_tags";
	}
	@RequestMapping(value = "/recentanswers", method = RequestMethod.GET)
	public String showRecentAnswersPage(Model model, WebRequest request, RedirectAttributes redirectAttributes) {
		return  "search_recent_answers";
	}
	private String getPageDescriptionVal(String response){
		String result = ((JSONObject) jsonUtility.getValueByKey(response, "response")).toJSONString();
		String resStr="";
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			QuestionBean question = mapper.readValue(result, QuestionBean.class);
			if(question.isAnswered()){
				resStr=question.getAnswers().get(0).getContent().replaceAll("\\<.*?\\>", " ").replaceAll("  "," ");
			}
			else{
				resStr="Be the first to answer";
			}
	    }
		catch (JsonGenerationException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
	    }
		catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	    }
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	    }
		return resStr;
	}
	@SuppressWarnings("serial")
	private String getQuestionByHttpRestService(HttpServletRequest httpRequest,long id){
		String host = httpRequest.getServerName();
		if(host.equalsIgnoreCase("localhost")){
			int serverPort = httpRequest.getServerPort();				
			if ((serverPort != 80) && (serverPort != 443)) {
				host=host+":"+serverPort+httpRequest.getContextPath();
		    }
		}
		String response = HttpRestWebServiceInvoker.doGetRequest(httpRequest.getScheme(),host,"/api/posts/question-"+id, new HashMap<String, String>(){});
		return response;
	}
}

package com.phroogal.web.controller.pages;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProfilePageController {
	
	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	public String userProfilePage(Model model, WebRequest request, RedirectAttributes redirectAttributes) {
		return  "profile";		
	}
	@RequestMapping(value = "/dashboard", method = RequestMethod.GET)
	public String userDashboardPage(Model model, WebRequest request, RedirectAttributes redirectAttributes) {
		return  "dashboard";		
	}
	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public String adminPageContent(Model model, WebRequest request, RedirectAttributes redirectAttributes) {
		return  "admin";		
	}
	@RequestMapping(value = "/settings", method = RequestMethod.GET)
	public String userSettingsPage(Model model, WebRequest request, RedirectAttributes redirectAttributes) {
		return  "settings";
	}
	@RequestMapping(value = "/privacy", method = RequestMethod.GET)
	public String privacyPage(Model model, WebRequest request, RedirectAttributes redirectAttributes) {
		return  "privacy";
	}
	@RequestMapping(value = "/terms", method = RequestMethod.GET)
	public String termsPage(Model model, WebRequest request, RedirectAttributes redirectAttributes) {
		return  "terms";
	}
	@RequestMapping(value = "/about", method = RequestMethod.GET)
	public String aboutPage(Model model, WebRequest request, RedirectAttributes redirectAttributes) {
		return  "about";
	}
	@RequestMapping(value = "/login_about", method = RequestMethod.GET)
	public String loginAboutPage(Model model, WebRequest request, RedirectAttributes redirectAttributes) {
		return  "login_about";
	}
	@RequestMapping(value = "/login_privacy", method = RequestMethod.GET)
	public String loginPrivacy(Model model, WebRequest request, RedirectAttributes redirectAttributes) {
		return  "login_privacy";
	}
	@RequestMapping(value = "/login_terms", method = RequestMethod.GET)
	public String loginTerms(Model model, WebRequest request, RedirectAttributes redirectAttributes) {
		return  "login_terms";
	}
	@RequestMapping(value = "/login_blog", method = RequestMethod.GET)
	public String loginBlog(Model model, WebRequest request, RedirectAttributes redirectAttributes) {
		return  "login_blog";
	}
	@RequestMapping(value = "/blog", method = RequestMethod.GET)
	public String blog(Model model, WebRequest request, RedirectAttributes redirectAttributes) {
		return  "blog";
	}
	@RequestMapping(value = "/profiles/addProfile", method = RequestMethod.GET)
	public String addNewBrandProfile(Model model, WebRequest request, RedirectAttributes redirectAttributes) {
		return  "profiles/brand_profile";
	}
	@RequestMapping(value = "/addreview", method = RequestMethod.GET)
	public String addNewReviewToBrand(Model model, WebRequest request, RedirectAttributes redirectAttributes) {
		return  "add_review";
	}
	@RequestMapping(value = "/brand/{name}", method = RequestMethod.GET)
	public String showBrandDetail(Model model, WebRequest request, RedirectAttributes redirectAttributes) {
		return  "brand_profile";
	}
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public String showHomePage(Model model, WebRequest request, RedirectAttributes redirectAttributes) {
		return  "search";
	}
	@RequestMapping(value = "/personalfinance", method = RequestMethod.GET)
	public String showPersonalFinancePage(Model model, WebRequest request, RedirectAttributes redirectAttributes) {
		return  "personal_finance";
	}
	@RequestMapping(value = "/governmentresources", method = RequestMethod.GET)
	public String showGovernmentResourcePage(Model model, WebRequest request,HttpServletRequest httpRequest, RedirectAttributes redirectAttributes) {
		model.addAttribute("url", httpRequest.getRequestURL());
		String host = httpRequest.getServerName();
		if(host.equalsIgnoreCase("localhost")){
			int serverPort = httpRequest.getServerPort();				
			if ((serverPort != 80) && (serverPort != 443)) {
				host=host+":"+serverPort+httpRequest.getContextPath();
		    }
		}
		String imageUrl=httpRequest.getScheme()+"://"+host+"/img/phrgl-smily-america.png";
		model.addAttribute("imageUrl",imageUrl);
		return  "government_resources";
	}
	
}

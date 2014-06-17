package com.phroogal.web.controller.pages;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class EmailPreferencesPageController{
	
	@RequestMapping(value = "/email_preferences", method = RequestMethod.GET)
	public String userEmailPreferencesPage(Model model, WebRequest request, RedirectAttributes redirectAttributes)
	{
		return  "preferences";		
	}
}

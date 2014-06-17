package com.phroogal.web.controller.pages;

import static com.phroogal.web.context.WebApplicationContext.PAGE_PASSWORD_RESET;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PasswordResetPageController{
	
	@RequestMapping(value = PAGE_PASSWORD_RESET, method = RequestMethod.GET, params="rid")
	public String userEmailPreferencesPage(@RequestParam("rid") String requestId, Model model, WebRequest request, RedirectAttributes redirectAttributes){
		model.addAttribute("rid", requestId);
		return  "password_reset";		
	}
}

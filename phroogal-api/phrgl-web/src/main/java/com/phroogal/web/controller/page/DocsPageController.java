 
package com.phroogal.web.controller.page;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mangofactory.swagger.annotations.ApiIgnore;

@Controller
public class DocsPageController {

	@ApiIgnore
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String showLandingPage(Model model, WebRequest request, RedirectAttributes redirectAttributes) {
		return  "redirect:/docs";
	}
	
	@ApiIgnore
	@RequestMapping(value = "/docs", method = RequestMethod.GET)
	public String showApiDocsPage(Model model, WebRequest request, RedirectAttributes redirectAttributes) {
		return  "docs";
	}
}

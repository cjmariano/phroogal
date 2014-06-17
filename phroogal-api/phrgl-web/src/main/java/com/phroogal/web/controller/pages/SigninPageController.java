package com.phroogal.web.controller.pages;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class SigninPageController {
	
	@RequestMapping(value = "/signin", method = RequestMethod.GET)
	public String signinPage(Model model) {
		return "signin";
	}
}

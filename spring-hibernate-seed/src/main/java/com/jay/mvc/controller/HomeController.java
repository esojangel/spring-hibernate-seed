package com.jay.mvc.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 
 * @author Jay Zhang
 * 
 */
@Controller
public class HomeController extends BaseController {

	@PreAuthorize("hasRole('ROLE_USER')")
	@RequestMapping(value = {"/", "/home"}, method = RequestMethod.GET)
	public String home(Model model) {
		return "home";
	}

	@PreAuthorize("hasRole('ROLE_DDD')")
	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public String admin(Model model) {
		model.addAttribute("title", "Admin - Spring Security Hello World");
		model.addAttribute("message", "This is protected page!");

		return "admin";
	}
}

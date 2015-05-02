package com.jay.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 
 * @author Jay Zhang
 * 
 */
@Controller
public class LoginController extends BaseController {
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(Model model,
			@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout) {
		
		if (error != null) {
			model.addAttribute("error", "用户名或密码不正确!");
		}

		if (logout != null) {
			model.addAttribute("msg", "你已成功退出系统.");
		}

		return "login";
	}
	
	@RequestMapping(value = "/forbidden", method = RequestMethod.GET)
	public String forbidden(Model model) {
		return "forbidden";
	}
}

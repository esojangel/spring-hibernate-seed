package com.jay.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jay.web.service.UserService;

/**
 * 
 * @author Jay Zhang
 * 
 */
@Controller
public class IndexController extends BaseController {
	
	@Autowired
	private UserService service;
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(Model model,
			@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout,
			@RequestParam(value = "expired", required = false) String expired) {
		
		if (error != null) {
			model.addAttribute("error", "用户名或密码不正确!");
		}

		if (logout != null) {
			model.addAttribute("msg", "你已成功退出系统.");
		}
		
		if (expired != null) {
			model.addAttribute("msg", "你的登录信息已过期.");
		}

		return "login";
	}
	
	@RequestMapping(value = "/forbidden", method = RequestMethod.GET)
	public String forbidden(Model model) {
		return "forbidden";
	}
	
	@RequestMapping(value = "/error404", method = RequestMethod.GET)
	public String error404(Model model) {
		return "error404";
	}
	
	@RequestMapping(value = "/exception", method = RequestMethod.GET)
	public String exception(Model model) {
		return "exception";
	}
}

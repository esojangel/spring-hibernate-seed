package com.jay.mvc.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jay.web.security.IChangePassword;
import com.jay.web.security.InMemoryChangePasswordDaoImpl;

/**
 * 
 * @author Jay Zhang
 * 
 */
@Controller
@RequestMapping(value="/account")
public class AccountController extends BaseController {

	private IChangePassword changePasswordDao = new InMemoryChangePasswordDaoImpl();

	@RequestMapping(value = "/changePassword", method = RequestMethod.GET)
	public String showChangePasswordPage() {
		return "changePassword";
	}

	@RequestMapping(value = "/changePassword", method = RequestMethod.POST)
	public String submitChangePasswordPage(@RequestParam("password") String newPassword) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = principal.toString();
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
		}
		changePasswordDao.changePassword(username, newPassword);
		SecurityContextHolder.clearContext();
		return "redirect:welcome";
	}
}

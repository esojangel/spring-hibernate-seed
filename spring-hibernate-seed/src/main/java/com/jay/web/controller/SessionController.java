package com.jay.web.controller;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jay.util.PasswordUtils;
import com.jay.web.domain.User;
import com.jay.web.service.PrivilegeService;
import com.jay.web.service.UserService;

/**
 * 
 * @author Jay Zhang
 * 
 */
@Controller
@RequestMapping(value = "/index")
public class SessionController {

	@Autowired
	private UserService service;

	@Autowired
	private PrivilegeService priviledgeService;

	// ----------------------------------------------------------------------
	// View
	// ----------------------------------------------------------------------

	/**
	 * 用户登录页面
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/login")
	public ModelAndView showLogin(HttpServletRequest request) {
		return new ModelAndView("Login");
	}

	/**
	 * 验证用户登录信息
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/validate", method = RequestMethod.POST)
	@ResponseBody
	public Object login(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String userName = request.getParameter("name");
		String password = request.getParameter("password");
		Map<String, String> map = new HashMap<String, String>();
		if (userName == null || userName.isEmpty() || password == null || password.isEmpty()) {
			map.put("status", "ERROR");
			map.put("errorMsg", "用户名或密码为空!");
		}
		else {
			User user = service.findUserByLoginName(userName);
			if (user == null) {
				map.put("status", "ERROR");
				map.put("errorMsg", "用户名不存在!");
			}
			else if (PasswordUtils.isValid(password, user.getPassword())) {
				if (user.getActive()) {
					boolean isAdmin = user.isAdmin();
					HttpSession session = request.getSession();
					session.setAttribute("login", userName);// 记录用户是否已登录
					session.setAttribute("user", user);
					session.setAttribute("isAdmin", isAdmin);// 记录用户是否已登录
					session.setAttribute("expire", Calendar.getInstance().getTimeInMillis() + 28800000);// 8小时后过期

//					List<Privilege> pgs = null;
//					if (isAdmin) {
//						pgs = priviledgeService.findPrivilegesByCategory("menu");
//					}
//					else {
//						pgs = priviledgeService.findPrivilegesByUser(user);
//					}
//					List<Map<String, Object>> menus = PrivilegeUtil.convertToMenu(PrivilegeUtil.grouping(pgs).values());
//					StringWriter sw = new StringWriter();
//					JsonUtil.serialize(menus, sw);
//					session.setAttribute("menus", sw.toString());
//					if (Detect.notEmpty(pgs)) {
//						map.put("status", "SUCCEED");
//						String redirectUrl = (String) request.getParameter("redirectUrl");
//						if (redirectUrl == null || redirectUrl.isEmpty()) {
//							map.put("viewName", request.getContextPath() + "/");// 指向程序主界面
//						}
//						else {
//							map.put("viewName", redirectUrl);// 指向程序主界面
//						}
//					}
//					else {
//						map.put("status", "ERROR");
//						map.put("errorMsg", "你没有任何权限，请联系管理员!");
//					}
				}
				else {
					map.put("status", "ERROR");
					map.put("errorMsg", "用户账户已被停用，请联系管理员!");
				}
			}
			else {
				map.put("status", "ERROR");
				map.put("errorMsg", "用户名或密码不正确!");
			}
		}
		return map;
	}

	/**
	 * 退出登录
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
//	@RequestMapping(value = "/logout", method = RequestMethod.GET)
//	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
//		HttpServletRequest req = (HttpServletRequest) request;
//		HttpSession session = req.getSession();
//		session.invalidate();
//		if (SessionContext.getContext() != null)
//			SessionContext.removeContext();
//		return new ModelAndView("Login");
//	}

	/**
	 * 修改密码
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/changePsd", method = RequestMethod.POST)
	@ResponseBody
	public Object changePsd(HttpServletRequest request) throws IOException {
		String oldPsd = request.getParameter("oldPsd");
		String newPsd = request.getParameter("newPsd");
		String rePsd = request.getParameter("rePsd");
		Map<String, String> map = new HashMap<String, String>();
		if (oldPsd == null || oldPsd.isEmpty()) {
			map.put("status", "ERROR");
			map.put("errorMsg", "请输入旧密码!");
		}
		else if (newPsd == null || newPsd.isEmpty()) {
			map.put("status", "ERROR");
			map.put("errorMsg", "请输入新密码!");
		}
		else if (rePsd == null || rePsd.isEmpty()) {
			map.put("status", "ERROR");
			map.put("errorMsg", "请确认新密码!");
		}
		else if (!rePsd.equals(newPsd)) {
			map.put("status", "ERROR");
			map.put("errorMsg", "确认密码与新密码不一致！");
		}
		else {
			HttpServletRequest req = (HttpServletRequest) request;
			HttpSession session = req.getSession();
			String userName = (String) session.getAttribute("login");
			if (userName == null || Calendar.getInstance().getTimeInMillis() > (Long) session.getAttribute("expire")) {
				session.invalidate();
				map.put("status", "ERROR");
				map.put("errorMsg", "你的登录已经失效！");
			}
			else {
				User user = service.findUserByLoginName(userName);
				boolean flag = user != null;
				if (flag) {
					flag = PasswordUtils.isValid(oldPsd, user.getPassword());
				}
				if (!flag) {
					map.put("status", "ERROR");
					map.put("errorMsg", "旧密码不正确！");
				}
				else {
					String pwd = PasswordUtils.encode(newPsd);
					user.setPassword(pwd);
					service.create(user);
					map.put("status", "SUCCEED");
					map.put("viewName", "/login");// 登录界面
				}
			}
		}
		return map;
	}
}

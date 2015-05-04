package com.jay.mvc.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jay.mvc.domain.Role;
import com.jay.mvc.domain.User;
import com.jay.mvc.service.RoleService;
import com.jay.mvc.service.SequenceService;
import com.jay.mvc.service.UserService;

/**
 * 
 * @author Jay Zhang
 * 
 */
@Controller
@RequestMapping(value = "/user")
public class UserController {

	@Autowired
	private UserService service;

	@Autowired
	private RoleService roleService;
	
	@Autowired
	private SequenceService seqService;
	// ----------------------------------------------------------------------
	// View
	// ----------------------------------------------------------------------

	/**
	 * 用户列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/list")
	public ModelAndView userList(HttpServletRequest request) {
		return new ModelAndView("maintenance/UserList");
	}

	// ----------------------------------------------------------------------
	// Data
	// ----------------------------------------------------------------------

	/**
	 * 添加用户
	 * 
	 * @param user
	 * @param error
	 * @return
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Object addUser(@Valid @ModelAttribute("user") User user, Errors error, HttpServletResponse response) throws IOException {
		Map<String, String> map = new HashMap<String, String>();
		String status = "OK";
		if (error.hasErrors()) {
			status = "ERROR";
			map.put("errorMsg", "Validate Failed!");
		} else {
			if(user.getName().equals("admin")){
				status = "ERROR";
				map.put("errorMsg", "用户名不符合规定!");
			} else {
				User sameName = service.findUserByLoginName(user.getLoginName());
				if(sameName!=null){
					status = "ERROR";
					map.put("errorMsg", "登录名已存在!");
				} else {
					Integer userId=seqService.getNextSequenceNumber(User.class.getName());
					user.setUserId(userId);
					user.setPassword("123456");
					try {
						user=service.create(user);
					} catch (Exception e) {
						status = "ERROR";
						map.put("errorMsg", "保存失败!");
					}
				}
			}
		}
		map.put("status", status);
		return map;
	}

	/**
	 * 编辑用户
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public Object edit(@Valid @ModelAttribute("user") User user, Errors error, HttpServletResponse response) throws IOException {
		Map<String, String> map = new HashMap<String, String>();
		String status = "OK";
		if (error.hasErrors()) {
			status = "ERROR";
			map.put("errorMsg", "Validate Failed!");
		} else {
			User original = service.findByUUID(user.getUUID());
			if (original == null) {
				map.put("errorMsg", "Not Found This User!");
			} else {
				User sameName = service.findUserByLoginName(user.getLoginName());
				if(sameName!=null && sameName.getUserId()!=original.getUserId()){
					status = "ERROR";
					map.put("errorMsg", "登录名已存在!");
				} else {
					original.setActive(user.getActive());
					original.setLoginName(user.getLoginName());
					original.setName(user.getName());
					original.setAge(user.getAge());
					original.setComment(user.getComment());
					original.setGender(user.getGender());
					original.setMobile(user.getMobile());
//					original.setPassword(user.getPassword());
//					original.updateModifiedBy();
					try {
						service.update(original);
					} catch (Exception e) {
						status = "ERROR";
						map.put("errorMsg", "保存失败!");
					}
				}
			}
		}
		map.put("status", status);
		return map;
	}

	/**
	 * 修改密码
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/changePsd", method = RequestMethod.POST)
	@ResponseBody
	public Object changePsd(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map<String, String> map = new HashMap<String, String>();
		String status = "OK";
		String uuid = request.getParameter("UUID");
		String password = request.getParameter("password");
		User original = service.findByUUID(uuid);
		if (original == null) {
			map.put("errorMsg", "Not Found This User!");
		} else {
			original.setPassword(password);
			try {
				service.update(original);
			} catch (Exception e) {
				status = "ERROR";
				map.put("errorMsg", "保存失败!");
			}
		}
		map.put("status", status);
		return map;
	}
	
	@RequestMapping(value = "/updateRoles", method = RequestMethod.POST)
	@ResponseBody
	public Object updateRoles(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map<String, String> map = new HashMap<String, String>();
		String status = "OK";
		String uuid = request.getParameter("uuid");
		User original = service.findByUUID(uuid);
		if (original != null) {
			List<Role> roles = original.getRoles();
			boolean isEmpty = roles == null;
			if (isEmpty) {
				roles = new ArrayList<Role>();
			}
			String count = request.getParameter("count");
			if (count != null) {
				Integer len = Integer.valueOf(count);
				if (len == 0) {
					if (!isEmpty) {
						roles.clear();
					}
				}
				else {
					roles.clear();
					for (int i = 0; i < len; i++) {
						String roleUuid = request.getParameter("roleUuid" + i);
						Role role = roleService.find(roleUuid);
						if (role != null) {
							roles.add(role);
						}
					}
					if (isEmpty) {
						original.setRoles(roles);
					}
				}
			}
			else {
				if (!isEmpty) {
					roles.clear();
				}
			}

			service.create(original);
		}
		else {
			status = "ERROR";
			map.put("errorMsg", "无效的主键!");
		}

		map.put("status", status);
		return map;
	}
	
	@RequestMapping(value = "/delete/{uuid}", method = RequestMethod.POST)
	@ResponseBody
	public Object delete(@PathVariable String uuid, HttpServletResponse response) throws IOException {
		Map<String, String> map = new HashMap<String, String>();
		User user = service.findByUUID(uuid);
		String status = "OK";
		if (user == null) {
			status = "ERROR";
			map.put("errorMsg", "Not Found This User!");
		} else {
			service.delete(uuid);
		}
		map.put("status", status);
		return map;
	}

	@RequestMapping(value = "/get/users")
	@ResponseBody
	public Object getUserList(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Integer pageNumber = 1;
		Integer pageSize = 30;
		if (request.getParameter("page") != null)
			pageNumber = Integer.valueOf(request.getParameter("page"));

		if (request.getParameter("rows") != null)
			pageSize = Integer.valueOf(request.getParameter("rows"));

		Map<String, Object> map = new HashMap<String, Object>();
		Page<User> page = service.getUsers(pageNumber, pageSize);
		map.put("total", page.getTotalElements());
		map.put("rows", page.getContent());
		return map;
	}
	
	@RequestMapping(value = "/get/queryUsers")
	@ResponseBody
	public Object queryUsers(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String name = request.getParameter("name");
		if(name==null || name.trim().isEmpty()){
			return getUserList(request, response);
		}
		
		Integer pageNumber = 1;
		Integer pageSize = 30;
		if (request.getParameter("page") != null)
			pageNumber = Integer.valueOf(request.getParameter("page"));

		if (request.getParameter("rows") != null)
			pageSize = Integer.valueOf(request.getParameter("rows"));

		Map<String, Object> map = new HashMap<String, Object>();

		Page<User> page = service.getUsers(name, pageNumber, pageSize);
		map.put("total", page.getTotalElements());
		map.put("rows", page.getContent());
		return map;
	}
	
	@RequestMapping(value = "/get/allUserNames")
	@ResponseBody
	public Object getAllUserNames(HttpServletRequest request, HttpServletResponse response) throws IOException {
		List<Map<String, String>> maps = new ArrayList<Map<String, String>>();
		Iterable<User> users = service.getAllUsers();
		Map<String, String> map=null;
		for(Iterator<User> it=users.iterator();it.hasNext();){
			User user=it.next();
			map = new HashMap<String, String>();
			map.put("uuid", user.getUUID());
			map.put("name", user.getName());
			maps.add(map);
		}
		return maps;
	}
	
	@RequestMapping(value = "/userRoles/{uuid}")
	@ResponseBody
	public List<Role> getRolesByUser(@PathVariable String uuid, HttpServletRequest request) {
		User user = service.findByUUID(uuid);
		return user == null ? null : user.getRoles();
	}
}

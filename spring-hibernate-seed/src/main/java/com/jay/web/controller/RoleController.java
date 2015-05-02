package com.jay.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jay.web.domain.Privilege;
import com.jay.web.domain.Role;
import com.jay.web.service.PrivilegeService;
import com.jay.web.service.RoleService;
import com.jay.web.service.SequenceService;

/**
 * 
 * @author Jay Zhang
 * 
 */
@Controller
@RequestMapping(value = "/role")
public class RoleController {
	@Autowired
	private RoleService service;

	@Autowired
	private PrivilegeService privilegeService;
	
	@Autowired
	private SequenceService seqService;

	// ----------------------------------------------------------------------
	// View
	// ----------------------------------------------------------------------

	/**
	 * 角色列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/list")
	public ModelAndView roleList() {
		return new ModelAndView("maintenance/RoleList");
	}

	/**
	 * 更新角色
	 * 
	 * @param role
	 * @param error
	 * @return
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Object addRole(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map<String, String> map = new HashMap<String, String>();
		String status = "OK";
		
		Role role=new Role();
		String count = request.getParameter("count");
		if (count != null) {
			List<Privilege> pgs = new ArrayList<Privilege>();
			Integer len = Integer.valueOf(count);
			for (int i = 0; i < len; i++) {
				String privilegeUuid = request.getParameter("privilegeUuid" + i);
				Privilege privilege = privilegeService.find(privilegeUuid);
				if (privilege != null) {
					pgs.add(privilege);
				}
			}
			role.setPrivileges(pgs);
		}

		String name=request.getParameter("name");
		if(name!=null){
			role.setName(name);
		}
		if(role.getName()==null)
		{
			status = "ERROR";
			map.put("errorMsg", "名称不能为空!");
		}
		else
		{
			role.setRoleId(seqService.getNextSequenceNumber(Role.class.getName()));
			try {
				service.create(role);
			} catch (Exception e) {
				status = "ERROR";
				map.put("errorMsg", "保存失败!");
			}
		}

		map.put("status", status);
		return map;
	}

	/**
	 * 编辑角色
	 * 
	 * @param uuid
	 * @return
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public Object edit(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map<String, String> map = new HashMap<String, String>();
		String status = "OK";
		
		String uuid = request.getParameter("uuid");
		Role original = service.find(uuid);
		if (original != null) {
			String count = request.getParameter("count");
			if (count != null) {
				List<Privilege> pgs = new ArrayList<Privilege>();
				Integer len = Integer.valueOf(count);
				for (int i = 0; i < len; i++) {
					String privilegeUuid = request.getParameter("privilegeUuid" + i);
					Privilege privilege = privilegeService.find(privilegeUuid);
					if (privilege != null) {
						pgs.add(privilege);
					}
				}
				original.setPrivileges(pgs);
			}

			original.setName(request.getParameter("name"));
			if(original.getName()==null)
			{
				status = "ERROR";
				map.put("errorMsg", "名称不能为空!");
			}
			else
			{
				try {
					service.create(original);
				} catch (Exception e) {
					status = "ERROR";
					map.put("errorMsg", "保存失败!");
				}
			}
		}
		else {
			status = "ERROR";
			map.put("errorMsg", "无效的主键!");
		}

		map.put("status", status);
		return map;
	}

	@RequestMapping(value = "/updatePrivileges", method = RequestMethod.POST)
	@ResponseBody
	public Object updatePrivileges(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map<String, String> map = new HashMap<String, String>();
		String status = "OK";

		String[] roles = request.getParameterValues("roles[]");
		String[] privileges = request.getParameterValues("privileges[]");
		String[] operations=request.getParameterValues("operations[]");
		if (roles == null || roles.length == 0) {
			status = "ERROR";
			map.put("errorMsg", "角色不能为空！");
		} else if (!service.batchUpdate(roles, privileges,operations)) {
			status = "ERROR";
			map.put("errorMsg", "保存失败！");
		}
			
		map.put("status", status);
		return map;
	}

	@RequestMapping(value = "/delete/{uuid}", method = RequestMethod.POST)
	@ResponseBody
	public Object delete(@PathVariable String uuid, HttpServletResponse response) throws IOException {
		Map<String, String> map = new HashMap<String, String>();
		Role role = service.find(uuid);
		String status = "OK";
		if (role == null) {
			status = "ERROR";
			map.put("errorMsg", "Not Found This Role!");
		}
		else {
			service.delete(uuid);
		}
		map.put("status", status);
		return map;
	}

	@RequestMapping(value = "/get/roles")
	@ResponseBody
	public Object getRoleList(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Integer pageNumber = 1;
		Integer pageSize = 30;
		if (request.getParameter("page") != null)
			pageNumber = Integer.valueOf(request.getParameter("page"));

		if (request.getParameter("rows") != null)
			pageSize = Integer.valueOf(request.getParameter("rows"));

		Page<Role> page = service.getRoles(pageNumber, pageSize);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("total", page.getTotalElements());
		map.put("rows", page.getContent());
		return map;
	}
	
	@RequestMapping(value = "/rolePrivileges/{uuid}")
	@ResponseBody
	public List<Privilege> getPrivilegesByRole(@PathVariable String uuid, HttpServletRequest request) {
		Role role = service.find(uuid);
		return role == null ? null : role.getPrivileges();
	}
}
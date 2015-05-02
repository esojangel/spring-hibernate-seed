package com.jay.web.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jay.web.domain.Privilege;
import com.jay.web.service.PrivilegeService;
import com.jay.web.service.SequenceService;

/**
 * 
 * @author Jay Zhang
 * 
 */
@Controller
@RequestMapping(value = "/privilege")
public class PrivilegeController {
	@Autowired
	private PrivilegeService service;

	@Autowired
	private SequenceService seqService;

	// ----------------------------------------------------------------------
	// View
	// ----------------------------------------------------------------------

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Object addPrivilege(@Valid @ModelAttribute("privilege") Privilege privilege, Errors error, HttpServletResponse response) throws IOException {
		Map<String, String> map = new HashMap<String, String>();
		String status = "OK";
		if (error.hasErrors()) {
			status = "ERROR";
			map.put("errorMsg", "Validate Failed!");
		}
		else {
			privilege.setPrivilegeId(seqService.getNextSequenceNumber(Privilege.class.getName()));
			try {
				service.create(privilege);
			}
			catch (Exception e) {
				status = "ERROR";
				map.put("errorMsg", "保存失败!");
			}
		}
		map.put("status", status);
		return map;
	}

	/**
	 * 编辑权限
	 * 
	 * @param uuid
	 * @return
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public Object edit(@Valid @ModelAttribute("privilege") Privilege privilege, Errors error, HttpServletResponse response) throws IOException {
		Map<String, String> map = new HashMap<String, String>();
		String status = "OK";
		if (error.hasErrors()) {
			status = "ERROR";
			map.put("errorMsg", "Validate Failed!");
		}
		else {
			Privilege original = service.find(privilege.getUUID());
			if (original == null) {
				map.put("errorMsg", "Not Found This Privilege!");
			}
			else {
				original.setName(privilege.getName());
				original.setCategory(privilege.getCategory());
				original.setShape(privilege.getShape());
				original.setPoint(privilege.getPoint());
				original.setRemarks(privilege.getRemarks());
				try {
					service.update(original);
				}
				catch (Exception e) {
					status = "ERROR";
					map.put("errorMsg", "保存失败!");
				}
			}
		}
		map.put("status", status);
		return map;
	}

	@RequestMapping(value = "/delete/{uuid}", method = RequestMethod.POST)
	@ResponseBody
	public Object delete(@PathVariable String uuid, HttpServletResponse response) throws IOException {
		Map<String, String> map = new HashMap<String, String>();
		Privilege privilege = service.find(uuid);
		String status = "OK";
		if (privilege == null) {
			status = "ERROR";
			map.put("errorMsg", "Not Found This Privilege!");
		}
		else {
			try {
				service.delete(uuid);
			}
			catch (Exception e) {
				status = "ERROR";
				map.put("errorMsg", "Delete failed!");
			}
		}
		map.put("status", status);
		return map;
	}
}
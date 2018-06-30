package com.cas.sim.tis.web.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cas.sim.tis.entity.ElecComp;
import com.cas.sim.tis.web.services.ElecCompWebService;

@Controller
@RequestMapping("/recongnize")
public class RecongnizeController {

	@Resource
	private ElecCompWebService elecCompService;

	@RequestMapping("/preview/{id}/{role}")
	public String preview(@PathVariable Integer id, @PathVariable Integer role, ModelMap map) {
		// 获得元器件信息
		ElecComp comp = elecCompService.selectByKey(id);
		map.put("comp", comp);
		map.put("role", role);
		return "recongnize/preview";
	}

	@RequestMapping("/edit/{id}/{role}")
	public String edit(@PathVariable Integer id, @PathVariable Integer role, ModelMap map) {
		// 获得元器件信息
		ElecComp comp = elecCompService.selectByKey(id);
		map.put("comp", comp);
		map.put("role", role);
		return "recongnize/edit";
	}

	@RequestMapping("/update")
	@ResponseBody
	public boolean update(ElecComp comp) {
		return elecCompService.updateNotNull(comp) > 0;
	}
}

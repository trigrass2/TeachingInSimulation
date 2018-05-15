package com.cas.sim.tis.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.Draw;
import com.cas.sim.tis.services.DrawService;

@Component
public class DrawAction extends BaseAction {

	@Resource(name = "drawService")
	private DrawService service;

	public List<Draw> getDrawListAll() {
		return service.findAll();
	}

	public List<Draw> getDrawBySystem() {
		return service.findByCreatorId(1);
	}

	public List<Draw> getDrawByMine() {
		return service.findByCreatorId(Session.get(Session.KEY_LOGIN_ID));
	}
}

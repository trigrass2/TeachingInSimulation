package com.cas.sim.tis.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.Draw;
import com.cas.sim.tis.services.DrawService;
import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;

@Component
public class DrawAction extends BaseAction {

	@Resource
	private DrawService service;

	public List<Draw> getDrawListAll() {
		ResponseEntity resp = service.findDraws(new RequestEntity());
		return JSON.parseArray(resp.data, Draw.class);
	}

	public List<Draw> getDrawBySystem() {
		RequestEntity req = new RequestEntity();
		req.set("creator", 1).end();
		ResponseEntity resp = service.findDraws(req);
		return JSON.parseArray(resp.data, Draw.class);
	}

	public List<Draw> getDrawByMine() {
		RequestEntity req = new RequestEntity();
		req.set("creator", Session.get(Session.KEY_LOGIN_ID)).end();
		ResponseEntity resp = service.findDraws(req);
		return JSON.parseArray(resp.data, Draw.class);
	}
}

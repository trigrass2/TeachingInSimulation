package com.cas.sim.tis.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.Draw;
import com.cas.sim.tis.services.DrawService;
import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.RequestEntityBuilder;
import com.cas.sim.tis.thrift.ResponseEntity;

@Component
public class DrawAction extends BaseAction {

	@Resource
	private DrawService service;

	/**
	 * 无条件查询所有图纸信息
	 * @return 返回图纸信息集合(id, name, paths, createDate)
	 */
	public List<Draw> getDrawListAll() {
		ResponseEntity resp = service.findDraws(new RequestEntityBuilder().build());
		return JSON.parseArray(resp.data, Draw.class);
	}

	/**
	 * 查询系统图纸（即管理员提交图纸creator=1）
	 * @return 返回图纸信息集合(id, name, paths, createDate)
	 */
	public List<Draw> getDrawBySystem() {
		RequestEntity req = new RequestEntityBuilder()//
				.set("creator", 1)//
				.build();
		ResponseEntity resp = service.findDraws(req);
		return JSON.parseArray(resp.data, Draw.class);
	}

	/**
	 * 查询用户上传图纸
	 * @return 返回图纸信息集合(id, name, paths, createDate)
	 */
	public List<Draw> getDrawByMine() {
		RequestEntity req = new RequestEntityBuilder()//
				.set("creator", Session.get(Session.KEY_LOGIN_ID))//
				.build();
		ResponseEntity resp = service.findDraws(req);
		return JSON.parseArray(resp.data, Draw.class);
	}
}

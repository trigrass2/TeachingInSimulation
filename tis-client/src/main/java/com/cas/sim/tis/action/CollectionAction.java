package com.cas.sim.tis.action;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.services.CollectionService;
import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.RequestEntityBuilder;
import com.cas.sim.tis.thrift.ResponseEntity;

@Component
public class CollectionAction extends BaseAction {
	@Resource
	private CollectionService service;

	/**
	 * 验证指定资源是否被当前用户收藏
	 * @param rid 资源编号
	 * @return 返回boolean是否收藏
	 */
	public boolean checkCollected(Integer rid) {
		if (rid == null) {
			return false;
		}
		RequestEntity req = new RequestEntityBuilder()//
				.set("rid", rid)//
				.set("creator", Session.get(Session.KEY_LOGIN_ID))//
				.build();

		ResponseEntity resp = service.checkCollected(req);
		return JSON.parseObject(resp.data, Boolean.class);
	}

}

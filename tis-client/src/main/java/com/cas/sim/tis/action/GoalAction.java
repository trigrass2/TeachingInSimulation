package com.cas.sim.tis.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.cas.sim.tis.entity.Goal;
import com.cas.sim.tis.services.GoalService;
import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.RequestEntityBuilder;
import com.cas.sim.tis.thrift.ResponseEntity;

@Component
public class GoalAction extends BaseAction {

	@Resource
	private GoalService service;

	/**
	 * 根据ASK关联标号查询ASK目标对象
	 * @param rid ASK关联编号
	 * @param type ASK类型
	 * @return ASK目标集合
	 */
	public List<Goal> findGoalsByRid(Integer rid, int type) {
		RequestEntity req = new RequestEntityBuilder()//
				.set("rid", rid)//
				.set("type", type)//
				.build();

		ResponseEntity resp = service.findGoalsByRid(req);
		return JSON.parseArray(resp.data, Goal.class);
	}
}

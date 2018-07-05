package com.cas.sim.tis.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.GoalCoverage;
import com.cas.sim.tis.services.GoalCoverageService;
import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.RequestEntityBuilder;
import com.cas.sim.tis.thrift.ResponseEntity;

@Component
public class GoalCoverageAction extends BaseAction {

	@Resource
	private GoalCoverageService service;

	/**
	 * 根据关联编号与关联类型获得相关的目标ASK编号集合
	 * @param rid 关联编号
	 * @param type 关联类型
	 * @return 目标ASK编号集合(id)
	 */
	public List<GoalCoverage> findGoalIdsByRid(String rid, int type) {
		RequestEntity req = new RequestEntityBuilder()//
				.set("rid", rid)//
				.set("type", type)//
				.build();
		ResponseEntity resp = service.findGoalIdsByRid(req);
		return JSON.parseArray(resp.data, GoalCoverage.class);
	}

	/**
	 * 保存目标ASK覆盖关系
	 * @param coverage 目标ASK覆盖关系对象
	 */
	public void insertRelationship(Integer gid, String rid, int type) {
		GoalCoverage coverage = new GoalCoverage();
		coverage.setGoalId(gid);
		coverage.setRelationId(rid);
		coverage.setType(type);
		coverage.setCreator(Session.get(Session.KEY_LOGIN_ID));
		RequestEntity req = new RequestEntityBuilder()//
				.set("coverage", coverage)//
				.build();
		service.saveGoalCoverage(req);
	}

	/**
	 * 根据条件删除目标ASK关系表信息（物理删除）
	 * @param gid 目标ASK编号
	 * @param rid 关联编号
	 * @param type 关联类型
	 * @param creator 关系创建人
	 */
	public void deleteRelationship(Integer gid, String rid, int type) {
		RequestEntity req = new RequestEntityBuilder()//
				.set("gid", gid)//
				.set("rid", rid)//
				.set("type", type)//
				.set("creator", Session.get(Session.KEY_LOGIN_ID))//
				.build();
		service.deleteRelationship(req);
	}

	/**
	 * 验证目标O是否被任务ASK覆盖
	 * @param oid 目标O编号
	 * @param tid 任务编号
	 * @return 返回boolean是否覆盖
	 */
	public boolean checkObjectiveCoverage(Integer oid, Integer tid) {
		RequestEntity req = new RequestEntityBuilder()//
				.set("oid", oid)//
				.set("tid", tid)//
				.build();
		ResponseEntity resp = service.checkObjectiveCoverage(req);
		return JSON.parseObject(resp.data, Boolean.class);
	}
}

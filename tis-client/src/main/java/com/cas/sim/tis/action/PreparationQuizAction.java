package com.cas.sim.tis.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.PreparationQuiz;
import com.cas.sim.tis.services.PreparationQuizService;
import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.RequestEntityBuilder;
import com.cas.sim.tis.thrift.ResponseEntity;
import com.cas.sim.tis.vo.PreparationInfo;

@Component
public class PreparationQuizAction extends BaseAction {
	@Resource
	private PreparationQuizService service;

	/**
	 * 通过备课编号获得备课试题集合
	 * @param pid 备课编号
	 * @return List PreparationInfo集合
	 */
	public List<PreparationInfo> findQuizsByPreparationId(Integer pid) {
		RequestEntity req = new RequestEntityBuilder()//
				.set("pid", pid)//
				.build();
		ResponseEntity resp = service.findQuizsByPreparationId(req);
		return JSON.parseArray(resp.data, PreparationInfo.class);
	}

	/**
	 * 根据备课试题编号获得备课试题对象
	 * @param id 备课试题编号
	 * @return PreparationQuiz 备课试题对象
	 */
	public PreparationQuiz findQuizById(Integer id) {
		RequestEntity req = new RequestEntityBuilder()//
				.set("id", id)//
				.build();
		ResponseEntity resp = service.findPreparationQuizById(req);
		return JSON.parseObject(resp.data, PreparationQuiz.class);
	}

	/**
	 * 保存备课试题对象
	 * @param quiz 备课试题对象
	 */
	public void addQuiz(PreparationQuiz quiz) {
		quiz.setCreator(Session.get(Session.KEY_LOGIN_ID));

		RequestEntity req = new RequestEntityBuilder()//
				.set("quiz", quiz)//
				.build();
		service.savePreparationQuiz(req);
	}

	/**
	 * 根据备课编号统计备课自由接线案例的数量
	 * @param pid 备课编号
	 * @return int 自由接线案例数量
	 */
	public boolean checkFreeQuiz(Integer pid) {
		RequestEntity req = new RequestEntityBuilder()//
				.set("pid", pid)//
				.build();

		ResponseEntity resp = service.countFreeQuizByPreparationId(req);
		return JSON.parseObject(resp.data, Integer.class) > 0;
	}

}

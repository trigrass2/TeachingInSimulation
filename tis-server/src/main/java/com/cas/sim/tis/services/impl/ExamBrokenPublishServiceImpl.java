package com.cas.sim.tis.services.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cas.sim.tis.config.ServerConfig;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.ExamPublish;
import com.cas.sim.tis.mapper.ExamBrokenPublishMapper;
import com.cas.sim.tis.mapper.ExamPublishMapper;
import com.cas.sim.tis.message.ExamMessage;
import com.cas.sim.tis.services.ExamBrokenPublishService;
import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;
import com.cas.sim.tis.vo.SubmitInfo;
import com.jme3.network.Filters;
import com.jme3.network.HostedConnection;

@Service
public class ExamBrokenPublishServiceImpl implements ExamBrokenPublishService {
	@Resource
	private ExamPublishMapper mapper;
	@Resource
	private ExamBrokenPublishMapper brokenPublishMapper;

	@Resource
	private ServerConfig serverConfig;

	@Override
	public ResponseEntity publishBrokenCases(RequestEntity entity) {
		ExamPublish publish = entity.getObject("publish", ExamPublish.class);
		// 记录考核发布记录
		mapper.insertUseGeneratedKeys(publish);

		// 广播开始考核
		ExamMessage message = new ExamMessage();
		message.setPid(publish.getId());
		message.setMessageType(ExamMessage.MESSAGE_TYPE_START);
		message.setExamType(ExamMessage.EXAM_TYPE_BROKEN);

		// 筛选当前登陆人员
		List<HostedConnection> collection = new ArrayList<>();
		for (HostedConnection hostedConnection : serverConfig.getClients()) {
			if (publish.getClassId().equals(hostedConnection.getAttribute(Session.KEY_LOGIN_CLASSID.name()))) {
				collection.add(hostedConnection);
			}
		}
		serverConfig.getServer().broadcast(Filters.in(collection), message);

		return ResponseEntity.success(publish.getId());
	}

	@Override
	public ResponseEntity findBrokenPublishById(RequestEntity entity) {
		ExamPublish publish = brokenPublishMapper.findPublishById(entity.getInt("id"));
		return ResponseEntity.success(publish);
	}

	@Override
	public ResponseEntity findSubmitStateByBrokenPublishId(RequestEntity entity) {
		List<SubmitInfo> infos = brokenPublishMapper.findSubmitStateById(entity.getInt("id"));
		return ResponseEntity.success(infos);
	}

	@Override
	public ResponseEntity updateBrokenPublish(RequestEntity entity) {
		Integer id = entity.getInt("id");
		ExamPublish publish = new ExamPublish();
		publish.setId(id);
		publish.setState(true);
		// 记录考核发布记录
		mapper.updateByPrimaryKeySelective(publish);

		// 将修改后的实体返回
		publish = mapper.selectByPrimaryKey(id);
		return ResponseEntity.success(publish);
	}

}

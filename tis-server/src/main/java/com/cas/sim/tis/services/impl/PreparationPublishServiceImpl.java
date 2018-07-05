package com.cas.sim.tis.services.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cas.sim.tis.config.ServerConfig;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.PreparationPublish;
import com.cas.sim.tis.mapper.PreparationPublishMapper;
import com.cas.sim.tis.message.ExamMessage;
import com.cas.sim.tis.services.PreparationPublishService;
import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;
import com.cas.sim.tis.vo.SubmitInfo;
import com.jme3.network.Filters;
import com.jme3.network.HostedConnection;

@Service
public class PreparationPublishServiceImpl implements PreparationPublishService {

	@Resource
	private PreparationPublishMapper mapper;
	@Resource
	private ServerConfig serverConfig;

	@Override
	public ResponseEntity publishPreparationLibrary(RequestEntity entity) {
		PreparationPublish publish = entity.getObject("publish", PreparationPublish.class);
		// 记录考核发布记录
		mapper.insertUseGeneratedKeys(publish);

		// 广播开始考核
		ExamMessage message = new ExamMessage();
		message.setPid(publish.getId());
		message.setMessageType(ExamMessage.MESSAGE_TYPE_START);
		message.setExamType(ExamMessage.EXAM_TYPE_PREPARATION);

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
	public ResponseEntity findPreparationPublishById(RequestEntity entity) {
		PreparationPublish publish = mapper.findPublishById(entity.getInt("id"));
		return ResponseEntity.success(publish);
	}

	@Override
	public ResponseEntity findSubmitStateByPreparationPublishId(RequestEntity entity) {
		List<SubmitInfo> infos = mapper.findSubmitStateById(entity.getInt("id"));
		return ResponseEntity.success(infos);
	}

	@Override
	public ResponseEntity updatePreparationPublish(RequestEntity entity) {
		PreparationPublish publish = new PreparationPublish();
		publish.setId(entity.getInt("id"));
		publish.setState(true);
		// 记录考核发布记录
		mapper.updateByPrimaryKeySelective(publish);

//		将修改后的实体返回
		publish = mapper.selectByPrimaryKey(entity.getInt("id"));
		return ResponseEntity.success(publish);
	}

}

package com.cas.sim.tis.message.handler;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.cas.sim.tis.config.ServerConfig;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.LibraryPublish;
import com.cas.sim.tis.entity.PreparationPublish;
import com.cas.sim.tis.message.ExamMessage;
import com.cas.sim.tis.services.LibraryPublishService;
import com.cas.sim.tis.services.PreparationPublishService;
import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.RequestEntityBuilder;
import com.cas.sim.tis.thrift.ResponseEntity;
import com.jme3.network.Filters;
import com.jme3.network.HostedConnection;

@Component
public class ExamMessageHandler implements ServerHandler<ExamMessage> {
	@Resource
	private ServerConfig serverConfig;
	@Resource
	private LibraryPublishService libraryPublishService;
	@Resource
	private PreparationPublishService preparationPublishService;

	@Override
	public void execute(HostedConnection source, ExamMessage m) throws Exception {
		int examType = m.getExamType();
		if (ExamMessage.EXAM_TYPE_LIBRARY == examType) {
			libraryExam(m);
		} else if (ExamMessage.EXAM_TYPE_PREPARATION == examType) {
			preparationExam(m);
		}
	}

	private void libraryExam(ExamMessage m) {
		if (ExamMessage.MESSAGE_TYPE_OVER == m.getMessageType()) {
//			获得发布记录对象
			RequestEntity req = new RequestEntityBuilder()//
					.set("id", m.getPid())//
					.build();
			ResponseEntity resp = libraryPublishService.updatePublishLibrary(req);
			LibraryPublish publish = JSON.parseObject(resp.data, LibraryPublish.class);
//			通知当前考试学生考试结束
			List<HostedConnection> collection = new ArrayList<>();
			for (HostedConnection hostedConnection : serverConfig.getClients()) {
				if (publish.getClassId().equals(hostedConnection.getAttribute(Session.KEY_LOGIN_CLASSID.name()))) {
					collection.add(hostedConnection);
				}
			}
			serverConfig.getServer().broadcast(Filters.in(collection), m);
		}
	}

	private void preparationExam(ExamMessage m) {
		if (ExamMessage.MESSAGE_TYPE_OVER == m.getMessageType()) {
//			获得发布记录对象
			RequestEntity req = new RequestEntityBuilder()//
					.set("id", m.getPid())//
					.build();
			ResponseEntity resp = preparationPublishService.updatePreparationPublish(req);
			PreparationPublish publish = JSON.parseObject(resp.data, PreparationPublish.class);
//			通知当前考试学生考试结束
			List<HostedConnection> collection = new ArrayList<>();
			for (HostedConnection hostedConnection : serverConfig.getClients()) {
				if (publish.getClassId().equals(hostedConnection.getAttribute(Session.KEY_LOGIN_CLASSID.name()))) {
					collection.add(hostedConnection);
				}
			}
			serverConfig.getServer().broadcast(Filters.in(collection), m);
		}
	}
}

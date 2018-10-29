package com.cas.sim.tis.message.handler;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.cas.sim.tis.config.ServerConfig;
import com.cas.sim.tis.consts.LibraryRecordType;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.ExamPublish;
import com.cas.sim.tis.entity.ExamPublish.Type;
import com.cas.sim.tis.message.ExamMessage;
import com.cas.sim.tis.services.ExamBrokenPublishService;
import com.cas.sim.tis.services.ExamBrokenRecordService;
import com.cas.sim.tis.services.ExamLibraryPublishService;
import com.cas.sim.tis.services.ExamLibraryRecordService;
import com.cas.sim.tis.services.ExamPreparationPublishService;
import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.RequestEntityBuilder;
import com.cas.sim.tis.thrift.ResponseEntity;
import com.cas.sim.tis.vo.ExamBrokenPublish;
import com.cas.sim.tis.vo.ExamPreparationPublish;
import com.cas.sim.tis.web.services.ExamPublishService;
import com.jme3.network.Filters;
import com.jme3.network.HostedConnection;

@Component
public class ExamMessageHandler implements ServerHandler<ExamMessage> {
	@Resource
	private ServerConfig serverConfig;
	@Resource
	private ExamLibraryPublishService libraryPublishService;
	@Resource
	private ExamLibraryRecordService libraryRecordService;
	@Resource
	private ExamPreparationPublishService preparationPublishService;
	@Resource
	private ExamBrokenPublishService brokenPublishService;
	@Resource
	private ExamBrokenRecordService brokenRecordService;
	@Resource
	private ExamPublishService examPublishService;

	@Override
	public void execute(HostedConnection source, ExamMessage m) throws Exception {
		int messageType = m.getMessageType();
		Integer sid = m.getSid();
		HostedConnection conn = serverConfig.getClients().stream().filter(c -> sid.equals(c.getAttribute(Session.KEY_LOGIN_ID.name()))).findAny().orElse(null);
		if (ExamMessage.MESSAGE_TYPE_QUERY_BY_TEACHER == messageType) {
			// 根据班级查询当前是否有正在考核的项目
			ExamPublish publish = examPublishService.findExamingByCreator(sid);
			if (publish == null) {
				return;
			}
			m.setPid(publish.getId());
			m.setMessageType(ExamMessage.MESSAGE_TYPE_START);

			int type = publish.getType();
			if (Type.LIBRARY_EXAM.getType() == type) {
				m.setExamType(ExamMessage.EXAM_TYPE_LIBRARY);
				conn.send(m);
			} else if (Type.PREPARATION_EXAM.getType() == type) {
				m.setExamType(ExamMessage.EXAM_TYPE_PREPARATION);
				conn.send(m);
			} else if (Type.BROKEN_EXAM.getType() == type) {
				m.setExamType(ExamMessage.EXAM_TYPE_BROKEN);
				conn.send(m);
			}
			return;
		} else if (ExamMessage.MESSAGE_TYPE_QUERY_BY_STUDENT == messageType) {
			Integer classId = conn.getAttribute(Session.KEY_LOGIN_CLASSID.name());
			// 根据班级查询当前是否有正在考核的项目
			ExamPublish publish = examPublishService.findExamingByClassId(classId);
			if (publish == null) {
				return;
			}
			m.setPid(publish.getId());
			m.setMessageType(ExamMessage.MESSAGE_TYPE_START);
			int type = publish.getType();
			Integer publishId = publish.getId();
			if (Type.LIBRARY_EXAM.getType() == type) {
				int count = examPublishService.conutLibraryRecordByPidAndSid(publishId, sid, LibraryRecordType.LIBRARY);
				if (count > 0) {
					return;
				}
				m.setExamType(ExamMessage.EXAM_TYPE_LIBRARY);
				conn.send(m);
			} else if (Type.PREPARATION_EXAM.getType() == type) {
				int count = examPublishService.conutLibraryRecordByPidAndSid(publishId, sid, LibraryRecordType.PREPARATION);
				if (count > 0) {
					return;
				}
				m.setExamType(ExamMessage.EXAM_TYPE_PREPARATION);
				conn.send(m);
			} else if (Type.BROKEN_EXAM.getType() == type) {
				int count = examPublishService.conutBrokenRecordByPidAndSid(publishId, sid);
				if (count > 0) {
					return;
				}
				m.setExamType(ExamMessage.EXAM_TYPE_BROKEN);
				conn.send(m);
			}
			return;
		}

		int examType = m.getExamType();
		if (ExamMessage.EXAM_TYPE_LIBRARY == examType) {
			libraryExam(m);
		} else if (ExamMessage.EXAM_TYPE_PREPARATION == examType) {
			preparationExam(m);
		} else if (ExamMessage.EXAM_TYPE_BROKEN == examType) {
			repairExam(m);
		}
	}

	private void libraryExam(ExamMessage m) {
		if (ExamMessage.MESSAGE_TYPE_OVER == m.getMessageType()) {
//			获得发布记录对象
			RequestEntity req = new RequestEntityBuilder()//
					.set("id", m.getPid())//
					.build();
			ResponseEntity resp = libraryPublishService.updatePublishLibrary(req);
			ExamPublish publish = JSON.parseObject(resp.data, ExamPublish.class);
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
			ExamPreparationPublish publish = JSON.parseObject(resp.data, ExamPreparationPublish.class);
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

	private void repairExam(ExamMessage m) {
		if (ExamMessage.MESSAGE_TYPE_OVER == m.getMessageType()) {
//			获得发布记录对象
			RequestEntity req = new RequestEntityBuilder()//
					.set("id", m.getPid())//
					.build();
			ResponseEntity resp = brokenPublishService.updateBrokenPublish(req);
			ExamBrokenPublish publish = JSON.parseObject(resp.data, ExamBrokenPublish.class);
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

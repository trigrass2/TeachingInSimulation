package com.cas.sim.tis.message.handler;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.cas.sim.tis.config.ServerConfig;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.LibraryPublish;
import com.cas.sim.tis.message.ExamMessage;
import com.cas.sim.tis.services.LibraryPublishService;
import com.jme3.network.Filters;
import com.jme3.network.HostedConnection;

@Component
public class ExamMessageHandler implements ServerHandler<ExamMessage> {
	@Resource
	private ServerConfig serverConfig;
	@Resource
	private LibraryPublishService libraryPublishService;
	
	@Override
	public void execute(HostedConnection source, ExamMessage m) throws Exception {
		if (ExamMessage.EXAM_OVER == m.getType()) {
			LibraryPublish publish = libraryPublishService.findById(m.getPid());
			// 通知当前考试学生考试结束
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

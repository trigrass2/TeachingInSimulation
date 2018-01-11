package com.cas.sim.tis.socket.message.handler;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.cas.sim.tis.consts.RoleConst;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.User;
import com.cas.sim.tis.services.UserService;
import com.cas.sim.tis.socket.CoreServer;
import com.cas.sim.tis.socket.message.LoginMessage;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;

@Component
public class LoginMessageHandler implements ServerHandler {

	@Resource(name = "studentService")
	private UserService studentService;
	@Resource(name = "teacherService")
	private UserService teacherService;

	@Override
	public void execute(HostedConnection source, Message m) throws Exception {
//		1、验证登录信息
		LoginMessage reqMsg = (LoginMessage) m;
//		接收客户端的请求信息
		int userType = reqMsg.getUserType();
		String code = reqMsg.getUserCode();
		String passwd = reqMsg.getUserPwd();
		if (code == null || passwd == null) {
			return;
		}
//		验证用户信息
//		准备一个消息用作服务器的响应消息
		LoginMessage respMsg = (LoginMessage) m;

		try {
			final User user;
			if (userType == RoleConst.STUDENT) {
//				检查学生的登录信息
				user = studentService.login(code, passwd);
//				登录成功
			} else if (userType == RoleConst.TEACHER || userType == RoleConst.ADMIN) {
//				检查老师或管理员的登录信息
				user = teacherService.login(code, passwd);
			} else {
//				服务器就当做没看到。
				return;
			}
			List<HostedConnection> clients = CoreServer.getIns().getClients();
//			进一步验证
			if (clients.size() >= CoreServer.getIns().getMaxClientNum()) {
				LOG.warn("客户端数量已经达到上限{}", clients.size());
//				告诉这个用户，当前用户已满，
				respMsg.setReason(LoginMessage.RESULT_MAX_SIZE);
				source.send(respMsg);
			} else {
//				检查用户是否已经登录了
				boolean exist = clients.stream().filter(c -> user.getId() == c.getAttribute(Session.KEY_LOGIN_USER_ID)).findAny().isPresent();
				if (exist) {
//					用户已经登录了
					respMsg.setReason(LoginMessage.RESULT_DUPLICATE);
					source.send(respMsg);
				} else {
					source.setAttribute(Session.KEY_LOGIN_USER_ID, user.getId());
					source.setAttribute(Session.KEY_LOGIN_USER, user);
					clients.add(source);
					LOG.info("客户端登录成功，当前客户端数量{}", clients.size());
//					用户成功连接
					respMsg.setReason(LoginMessage.RESULT_SUCCESS);
					source.send(respMsg);
				}
			}
		} catch (Exception e) {
			LOG.warn("用户登录失败{}", e.getMessage());
//			登录失败：原因是登录信息错误
			respMsg.setReason(LoginMessage.RESULT_FAILURE);
			source.send(respMsg);
		}
	}

}

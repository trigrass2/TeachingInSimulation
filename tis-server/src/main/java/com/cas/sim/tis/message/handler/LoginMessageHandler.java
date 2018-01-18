package com.cas.sim.tis.message.handler;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.cas.sim.tis.config.ServerConfig;
import com.cas.sim.tis.consts.RoleConst;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.User;
import com.cas.sim.tis.message.LoginMessage;
import com.cas.sim.tis.services.UserService;
import com.jme3.network.HostedConnection;

@Component
public class LoginMessageHandler implements ServerHandler<LoginMessage> {

	@Resource
	private ServerConfig serverConfig;

	@Resource(name = "studentService")
	private UserService studentService;
	@Resource(name = "teacherService")
	private UserService teacherService;

	@Override
	public void execute(HostedConnection source, LoginMessage m) throws Exception {
//		接收客户端的请求信息
		int userType = m.getUserType();
		String code = m.getUserCode();
		String passwd = m.getUserPwd();
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
//				服务器就当做没看到，转手甩个异常出去
				throw new RuntimeException("这不是一个有效的用户");
			}
			List<HostedConnection> clients = serverConfig.getClients();
//			进一步验证
			if (clients.size() >= serverConfig.getMaxLogin()) {
//				告诉这个用户，当前用户已满，
				respMsg.setResult(LoginMessage.RESULT_MAX_SIZE);
				source.send(respMsg);
				throw new RuntimeException("客户端数量已经达到上限");
			} else {
//				检查用户是否已经登录了
				boolean exist = clients.stream().filter(c -> user.getId().equals(c.getAttribute(Session.KEY_LOGIN_USER_ID.name()))).findAny().isPresent();
				if (exist) {
//					用户已经登录了
					respMsg.setResult(LoginMessage.RESULT_DUPLICATE);
					source.send(respMsg);
				} else {
					source.setAttribute(Session.KEY_LOGIN_USER_ID.name(), user.getId());
					source.setAttribute(Session.KEY_LOGIN_USER.name(), user);
					clients.add(source);
					LOG.info("客户端登录成功，当前客户端数量{}", clients.size());
//					用户成功连接
					respMsg.setResult(LoginMessage.RESULT_SUCCESS);
					respMsg.setUserid(user.getId());
					source.send(respMsg);
				}
			}
		} catch (Exception e) {
//			登录失败：原因是登录信息错误
			respMsg.setResult(LoginMessage.RESULT_FAILURE);
			source.send(respMsg);

			throw new RuntimeException("用户登录失败", e);
		}
	}

}

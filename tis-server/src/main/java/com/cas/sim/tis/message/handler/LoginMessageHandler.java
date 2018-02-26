package com.cas.sim.tis.message.handler;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.cas.sim.tis.config.ServerConfig;
import com.cas.sim.tis.consts.LoginResult;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.User;
import com.cas.sim.tis.message.LoginMessage;
import com.cas.sim.tis.services.UserService;
import com.cas.sim.tis.services.exception.ServiceException;
import com.jme3.network.HostedConnection;

@Component
public class LoginMessageHandler implements ServerHandler<LoginMessage> {

	@Resource
	private ServerConfig serverConfig;

	@Resource(name = "userService")
	private UserService userService;

	@Override
	public void execute(HostedConnection source, LoginMessage m) throws Exception {
//		接收客户端的请求信息
		String code = m.getUserCode();
		String passwd = m.getUserPwd();
		if (code == null || passwd == null) {
			return;
		}
//		验证用户信息
//		准备一个消息用作服务器的响应消息
		LoginMessage respMsg = (LoginMessage) m;
		try {
			final User user = userService.login(code, passwd);
			List<HostedConnection> clients = serverConfig.getClients();
//			进一步验证
			if (clients.size() >= serverConfig.getMaxLogin()) {
//				告诉这个用户，当前用户已满，
				respMsg.setResult(LoginResult.MAX_SIZE);
				source.send(respMsg);
				throw new RuntimeException("客户端数量已经达到上限");
			} else {
//				检查用户是否已经登录了
				boolean exist = clients.stream().filter(c -> user.getId().equals(c.getAttribute(Session.KEY_LOGIN_ID.name()))).findAny().isPresent();
				if (exist) {
//					用户已经登录了
					respMsg.setResult(LoginResult.DUPLICATE);
					source.send(respMsg);
				} else {
					source.setAttribute(Session.KEY_LOGIN_ID.name(), user.getId());
					source.setAttribute(Session.KEY_LOGIN_CLASSID.name(), user.getClassId());
					source.setAttribute(Session.KEY_LOGIN_ACCOUNT.name(), user.getCode());
					clients.add(source);
					LOG.info("客户端登录成功，当前客户端数量{}", clients.size());
//					用户成功连接
					respMsg.setResult(LoginResult.SUCCESS);
					respMsg.setUserId(user.getId());
					respMsg.setUserType(user.getRole());
					source.send(respMsg);
				}
			}
		} catch (ServiceException e) {
//			登录失败：原因是登录信息错误
			respMsg.setResult(LoginResult.FAILURE);
			source.send(respMsg);
			throw e;
		} catch (Exception e) {
//			登录失败：服务器出现异常
			respMsg.setResult(LoginResult.SERVER_EXCE);
			source.send(respMsg);
			throw e;
		}
	}

}

package com.cas.sim.tis.message.handler;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cas.sim.tis.config.ServerConfig;
import com.cas.sim.tis.consts.LoginResult;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.User;
import com.cas.sim.tis.message.LoginMessage;
import com.cas.sim.tis.services.UserService;
import com.cas.sim.tis.services.exception.ServiceException;
import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;
import com.jme3.network.HostedConnection;
import com.jme3.network.message.DisconnectMessage;

@Component
public class LoginMessageHandler implements ServerHandler<LoginMessage> {

	@Resource
	private ServerConfig serverConfig;

	@Resource
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
			JSONObject obj = new JSONObject();
			obj.put("usercode ", code);
			obj.put("password  ", passwd);
			RequestEntity entity = new RequestEntity();
			entity.data = JSON.toJSONString(entity);
			ResponseEntity resp = userService.login(entity);
			final User user = JSON.parseObject(resp.data, User.class);

			List<HostedConnection> clients = serverConfig.getClients();
//			进一步验证
			if (clients.size() >= serverConfig.getMaxLogin()) {
//				告诉这个用户，当前用户已满，
				respMsg.setResult(LoginResult.MAX_SIZE);
				source.send(respMsg);
				LOG.warn("客户端数量已经达到上限:{}", clients.size());
				return;
			}
//			检查用户是否已经登录了
			HostedConnection existConn = clients.stream().filter(c -> user.getId().equals(c.getAttribute(Session.KEY_LOGIN_ID.name()))).findAny().orElse(null);
			if (existConn == null) {
				source.setAttribute(Session.KEY_LOGIN_ID.name(), user.getId());
				source.setAttribute(Session.KEY_LOGIN_CLASSID.name(), user.getClassId());
				source.setAttribute(Session.KEY_LOGIN_ACCOUNT.name(), user.getCode());
				clients.add(source);
				LOG.info("客户端登录成功，当前客户端数量{}", clients.size());
//				用户成功连接
				respMsg.setResult(LoginResult.SUCCESS);
				respMsg.setUserId(user.getId());
				respMsg.setUserType(user.getRole());
				respMsg.setUser(resp.data);
				source.send(respMsg);
			} else if (m.isFocus()) {
				DisconnectMessage disconnect = new DisconnectMessage();
				disconnect.setReason(DisconnectMessage.KICK);
				disconnect.setType(DisconnectMessage.KICK);
				existConn.send(disconnect);

				source.setAttribute(Session.KEY_LOGIN_ID.name(), user.getId());
				source.setAttribute(Session.KEY_LOGIN_CLASSID.name(), user.getClassId());
				source.setAttribute(Session.KEY_LOGIN_ACCOUNT.name(), user.getCode());
				clients.add(source);
				LOG.info("客户端登录成功，当前客户端数量{}", clients.size());
//				用户成功连接
				respMsg.setResult(LoginResult.SUCCESS);
				respMsg.setUserId(user.getId());
				respMsg.setUserType(user.getRole());
				source.send(respMsg);
			} else {
//				用户已经登录了
				respMsg.setResult(LoginResult.DUPLICATE);
				source.send(respMsg);
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

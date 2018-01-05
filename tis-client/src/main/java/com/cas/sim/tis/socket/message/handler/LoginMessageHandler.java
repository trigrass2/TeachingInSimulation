package com.cas.sim.tis.socket.message.handler;

import org.springframework.stereotype.Component;

import com.cas.sim.tis.socket.message.LoginMessage;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.controller.LoginController;
import com.jme3.network.Client;
import com.jme3.network.Message;

import javafx.application.Platform;

@Component
public class LoginMessageHandler implements ClientHandler {

	@Override
	public void execute(Client client, Message m) throws Exception {
		LoginMessage msg = (LoginMessage) m;
		if (LoginMessage.RESULT_SUCCESS == msg.getReason()) {
//			登录成功
//			页面跳转
		} else {
			Platform.runLater(() -> {
				LoginController loginController = SpringUtil.getBean(LoginController.class);
				if (LoginMessage.RESULT_FAILURE == msg.getReason()) {
//					用户名或密码错误
					loginController.setErrorMsg("用户名或密码错误");
				} else if (LoginMessage.RESULT_MAX_SIZE == msg.getReason()) {
//					客户端人员已满
					loginController.setErrorMsg("客户端连接数量已满");
				} else if (LoginMessage.RESULT_DUPLICATE == msg.getReason()) {
//					用户已登录
					loginController.setErrorMsg("用户已登录");
				}
			});
		}
	}

}

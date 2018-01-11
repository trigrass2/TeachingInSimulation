package com.cas.sim.tis.socket.message.handler;

import java.util.Locale;

import javax.annotation.Resource;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import com.cas.sim.tis.socket.message.LoginMessage;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.controller.LoginController;
import com.jme3.network.Client;
import com.jme3.network.Message;

import javafx.application.Platform;

@Component
public class LoginMessageHandler implements ClientHandler {

	@Resource
	private MessageSource messageSource; // 自动注入对象
	
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
					loginController.setErrorMsg(messageSource.getMessage("login.failure", null, Locale.getDefault()));
				} else if (LoginMessage.RESULT_MAX_SIZE == msg.getReason()) {
//					客户端连接数量已满
					loginController.setErrorMsg(messageSource.getMessage("login.failure.max_size", null, Locale.getDefault()));
				} else if (LoginMessage.RESULT_DUPLICATE == msg.getReason()) {
//					用户已登录
					loginController.setErrorMsg(messageSource.getMessage("login.failure.duplicate", null, Locale.getDefault()));
				}
			});
		}
	}

}

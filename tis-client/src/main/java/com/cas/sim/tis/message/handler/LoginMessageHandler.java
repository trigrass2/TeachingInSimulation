package com.cas.sim.tis.message.handler;

import org.springframework.boot.SpringApplication;

import com.alibaba.fastjson.JSON;
import com.cas.sim.tis.Application;
import com.cas.sim.tis.consts.LoginResult;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.User;
import com.cas.sim.tis.message.LoginMessage;
import com.cas.sim.tis.util.AppPropertiesUtil;
import com.cas.sim.tis.view.controller.LoginController;
import com.jme3.network.Client;

import javafx.application.Platform;

public class LoginMessageHandler implements ClientHandler<LoginMessage> {
	private LoginController loginController;

	@Override
	public void execute(Client client, LoginMessage m) throws Exception {
		if (LoginResult.SUCCESS == m.getResult()) {
//			登录成功
//			1、记录Session
			Session.set(Session.KEY_LOGIN_ID, m.getUserId());
			Session.set(Session.KEY_LOGIN_ROLE, m.getUserType());
			Session.set(Session.KEY_LOGIN_ACCOUNT, m.getUserCode());
			Session.set(Session.KEY_OBJECT, JSON.parseObject(m.getUser(), User.class));
//			2、记录当前登录用户
			AppPropertiesUtil.set("login.account", m.getUserCode());
			AppPropertiesUtil.store();

			Platform.runLater(() -> {
				loginController.setStatusMsgKey("login.success");
			});

//			3、启动客户端程序
			SpringApplication.run(Application.class, new String[] {});
//			4、关闭登录界面
			Platform.runLater(() -> {
				loginController.close();
			});
		} else {
			Platform.runLater(() -> {
				loginController.failure(m);
			});
		}
	}

	public void setLoginUIController(LoginController loginController) {
		this.loginController = loginController;
	}
}

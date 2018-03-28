package com.cas.sim.tis.message.handler;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import org.springframework.boot.SpringApplication;

import com.cas.sim.tis.Application;
import com.cas.sim.tis.consts.LoginResult;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.message.LoginMessage;
import com.cas.sim.tis.view.controller.LoginController;
import com.jme3.network.Client;

import javafx.application.Platform;

public class LoginMessageHandler implements ClientHandler<LoginMessage> {
	private LoginController loginController;
	private Properties properties = new Properties();

	@Override
	public void execute(Client client, LoginMessage m) throws Exception {
		if (LoginResult.SUCCESS == m.getResult()) {
//			登录成功
//			1、记录Session
			Session.set(Session.KEY_LOGIN_ID, m.getUserId());
			Session.set(Session.KEY_LOGIN_ROLE, m.getUserType());
			Session.set(Session.KEY_LOGIN_ACCOUNT, m.getUserCode());
//			2、记录当前登录用户
			properties.load(new FileInputStream("application.properties"));
			properties.setProperty("login.account", m.getUserCode());
			FileOutputStream fos = new FileOutputStream("application.properties");
			properties.store(fos, null);
			fos.close();

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

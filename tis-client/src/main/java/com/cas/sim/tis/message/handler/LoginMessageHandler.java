package com.cas.sim.tis.message.handler;

import java.util.Locale;

import javax.annotation.Resource;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import com.cas.sim.tis.Application;
import com.cas.sim.tis.message.LoginMessage;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.HomeView;
import com.cas.sim.tis.view.controller.LoginController;
import com.jme3.network.Client;

import de.felixroske.jfxsupport.GUIState;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Region;

@Component
public class LoginMessageHandler implements ClientHandler<LoginMessage> {

	@Resource
	private MessageSource messageSource; // 自动注入对象

	@Override
	public void execute(Client client, LoginMessage m) throws Exception {
		if (LoginMessage.RESULT_SUCCESS == m.getResult()) {
//			登录成功
//			TODO 登陆后页面跳转
			Platform.runLater(() -> {
				GUIState.setScene(new Scene(new Region()));
				GUIState.getScene().setFill(null);
				Application.showView(HomeView.class);
			});
		} else {
			Platform.runLater(() -> {
				LoginController loginController = SpringUtil.getBean(LoginController.class);
				if (LoginMessage.RESULT_FAILURE == m.getResult()) {
//					用户名或密码错误
					loginController.setErrorMsg(messageSource.getMessage("login.failure", null, Locale.getDefault()));
				} else if (LoginMessage.RESULT_MAX_SIZE == m.getResult()) {
//					客户端连接数量已满
					loginController.setErrorMsg(messageSource.getMessage("login.failure.max_size", null, Locale.getDefault()));
				} else if (LoginMessage.RESULT_DUPLICATE == m.getResult()) {
//					用户已登录
					loginController.setErrorMsg(messageSource.getMessage("login.failure.duplicate", null, Locale.getDefault()));
				}
			});
		}
	}
}

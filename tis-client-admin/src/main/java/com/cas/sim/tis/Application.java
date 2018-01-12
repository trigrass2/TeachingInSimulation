package com.cas.sim.tis;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.cas.sim.tis.consts.RoleConst;
import com.cas.sim.tis.socket.ClientT;
import com.cas.sim.tis.socket.message.LoginMessage;
import com.cas.sim.tis.socket.message.handler.LoginMessageHandler;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.LoginView;
import com.cas.sim.tis.view.controller.LoginController;

import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;

@SpringBootApplication
public class Application extends AbstractJavaFxApplicationSupport implements ApplicationRunner {

	public static void main(String[] args) {
		ClientT.getIns().connect();

//		明确登录用的身份
		LoginController.USER_ROLE = RoleConst.ADMIN;

		launch(Application.class, LoginView.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		ClientT.getIns().registerMessageHandler(LoginMessage.class, SpringUtil.getBean(LoginMessageHandler.class));
	}

}

package com.cas.sim.tis;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.annotation.Resource;

import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.cas.sim.tis.config.ClientConfig;
import com.cas.sim.tis.consts.RoleConst;
import com.cas.sim.tis.socket.message.LoginMessage;
import com.cas.sim.tis.socket.message.handler.LoginMessageHandler;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.LoginView;
import com.cas.sim.tis.view.controller.LoginController;
import com.jme3.network.Client;

import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;

@SpringBootApplication
public class Application extends AbstractJavaFxApplicationSupport implements ApplicationRunner {

	@Resource
	private ClientConfig clientConfig;
	@Resource(name = "jmeClient")
	private Client client;

	public static void main(String[] args) {
		LogManager.getLogManager().reset();
		SLF4JBridgeHandler.removeHandlersForRootLogger();
		Logger.getLogger("").setLevel(Level.FINEST);
		SLF4JBridgeHandler.install();

//		明确登录用的身份
		LoginController.USER_ROLE = RoleConst.TEACHER;

		launch(Application.class, LoginView.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		clientConfig.registerMessageHandler(LoginMessage.class, SpringUtil.getBean(LoginMessageHandler.class));

		client.start();
	}

}

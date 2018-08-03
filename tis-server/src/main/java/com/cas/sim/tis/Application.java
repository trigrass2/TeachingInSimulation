package com.cas.sim.tis;

import javax.annotation.Resource;

import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.cas.sim.tis.config.ServerConfig;
import com.cas.sim.tis.message.ExamMessage;
import com.cas.sim.tis.message.LoginMessage;
import com.cas.sim.tis.message.handler.ExamMessageHandler;
import com.cas.sim.tis.message.handler.LoginMessageHandler;
import com.cas.sim.tis.util.SpringUtil;
import com.jme3.network.Server;

import io.airlift.drift.server.DriftServer;

@SpringBootApplication
//开始事物
//@EnableTransactionManagement
public class Application implements CommandLineRunner {

	@Resource
	private ServerConfig serverConfig;
	@Resource
	private Server coreServer;
	@Resource
	private DriftServer driftServer;

	public static void jul2slf4j() {
		java.util.logging.LogManager.getLogManager().reset();
		SLF4JBridgeHandler.removeHandlersForRootLogger();
//		设置JUL的日志级别
		java.util.logging.Logger.getLogger("").setLevel(java.util.logging.Level.FINEST);
//		设置日志转换（桥接）
		SLF4JBridgeHandler.install();
	}

	public static void main(String[] args) {
//		将JUL（Java-Util-Logging）的日志转接给slf4j
		jul2slf4j();

//		ValidateThread t = new ValidateThread(SystemInfo.APP_ID);
//		t.validate((result)->{
			SpringApplication.run(Application.class);
//		});
	}

	@Override
	public void run(String... args) throws Exception {
//		配置消息
		serverConfig.registerMessageHandler(LoginMessage.class, SpringUtil.getBean(LoginMessageHandler.class));
		serverConfig.registerMessageHandler(ExamMessage.class, SpringUtil.getBean(ExamMessageHandler.class));
//		启动
		coreServer.start();
		driftServer.start();
		LoggerFactory.getLogger(Application.class).info("服务器已启动");
	}
}

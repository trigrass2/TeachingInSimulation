package com.cas.sim.tis;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.FutureTask;

import javax.annotation.Resource;

import org.apache.ftpserver.FtpServer;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.cas.authority.Consts;
import com.cas.authority.validate.ValidateThread;
import com.cas.sim.tis.config.ServerConfig;
import com.cas.sim.tis.consts.SystemInfo;
import com.cas.sim.tis.message.ExamMessage;
import com.cas.sim.tis.message.LoginMessage;
import com.cas.sim.tis.message.handler.ExamMessageHandler;
import com.cas.sim.tis.message.handler.LoginMessageHandler;
import com.cas.sim.tis.util.SpringUtil;
import com.jme3.network.Server;
import com.softkey.SoftKey;

@SpringBootApplication
//开始事物
//@EnableTransactionManagement
// 在类中用注解@Mapper明确标出
//@MapperScan("com.cas.sim.tis.mapper")
public class Application implements CommandLineRunner {

	@Resource
	private ServerConfig serverConfig;

	@Resource
	private FtpServer ftpServer;

	@Resource
	private Server coreServer;

	private static void jul2slf4j() {
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

////		加密验证
//		SoftKey sk = new SoftKey(SoftKey.KEY_FOR_SALER);
//		boolean success = sk.CheckKeyByReadEprom() == 0 && sk.CheckKeyByEncstring() == 0;
//		if (success) {
//			ValidateThread validation = new ValidateThread(SystemInfo.APP_ID);
//			// 加密锁验证通过
//			FutureTask<Integer> task = new FutureTask<>(validation);
//			new Thread(task).start();
//
//			try {
//				Integer result = task.get();
////				证书没问题
//				if (Consts.AUTHORITY_FILE_AVAILABLE == result) {
////					创建服务器
//					try {
						SpringApplication.run(Application.class);
//					} catch (Exception e) {
//						LoggerFactory.getLogger(Application.class).error("服务器启动失败", e);
//						e.printStackTrace();
//					}
//
////					启动时间验证定时器
//					Timer timer = new Timer();
//					timer.scheduleAtFixedRate(new TimerTask() {
//						@Override
//						public void run() {
//							try {
//								validation.getTimerClock().validate();
//							} catch (Exception e) {
//								LoggerFactory.getLogger(Application.class).error("错误代码:{}", result);
//								timer.cancel();
////								停止一切服务
//								System.exit(0);
//							}
//						}
//					}, 1000 * 60 * 10, 1000 * 60 * 10); // 10分钟后开始验证，然后每10分钟验证1次
//				} else {
//					LoggerFactory.getLogger(Application.class).error("错误代码:{}", result);
//				}
//			} catch (Exception e) {
//				LoggerFactory.getLogger(Application.class).error("加密锁验证过程出现错误。{}", e.getMessage());
//			}
//		} else {
////			告知用户加密锁错误。
//			LoggerFactory.getLogger(Application.class).error("程序启动失败:{}", "未找到合适的加密锁设备");
////			点击对话框中的确定按钮后，立即退出系统。
//			System.exit(0);
//		}
	}

	@Override
	public void run(String... args) throws Exception {
//		配置消息
		serverConfig.registerMessageHandler(LoginMessage.class, SpringUtil.getBean(LoginMessageHandler.class));
		serverConfig.registerMessageHandler(ExamMessage.class, SpringUtil.getBean(ExamMessageHandler.class));

//		启动
		coreServer.start();
		LoggerFactory.getLogger(Application.class).info("主服务器已启动");
		ftpServer.start();
		LoggerFactory.getLogger(Application.class).info("文件服务器已启动");
	}
}

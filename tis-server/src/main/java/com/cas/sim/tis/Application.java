package com.cas.sim.tis;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.FutureTask;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.cas.authority.Consts;
import com.cas.authority.validate.ValidateThread;
import com.cas.sim.tis.consts.SystemInfo;
import com.cas.sim.tis.entity.Resource;
import com.cas.sim.tis.socket.CoreServer;
import com.cas.sim.tis.socket.FileServer;
import com.cas.sim.tis.socket.message.LoginMessage;
import com.cas.sim.tis.socket.message.ResourcesMessage;
import com.cas.sim.tis.socket.message.handler.LoginMessageHandler;
import com.cas.sim.tis.socket.message.handler.ResourcesMessageHandler;
import com.cas.sim.tis.util.SpringUtil;
import com.jme3.network.serializing.Serializer;
import com.softkey.SoftKey;

import cas.lock.ILockResult;
import cas.lock.LockThread;

@SpringBootApplication
//开始事物
@EnableTransactionManagement
// 在类中用注解@Mapper明确标出
//@MapperScan("com.cas.sim.tis.mapper")
public class Application implements CommandLineRunner {
	public static void main(String[] args) {
//		将JUL（Java-Util-Logging）的日志转接给slf4j
		jul2slf4j();

		try {
//			UIManager.setLookAndFeel(com.sun.java.swing.plaf.windows.WindowsLookAndFeel.class.getName());
			UIManager.setLookAndFeel(javax.swing.plaf.nimbus.NimbusLookAndFeel.class.getName());
		} catch (Exception e) {
		}

//		加密验证
		LockThread lt = new LockThread(new ILockResult() {
			@Override
			public void doLockWrong(String cause) {
//				告知用户加密锁错误。
				showErrorMsg("错误原因：" + cause);
//				点击对话框中的确定按钮后，立即退出系统。
				System.exit(0);
			}

			@Override
			public void doLockRight() {
				ValidateThread validation = new ValidateThread(SystemInfo.APP_ID);
				// 加密锁验证通过
				FutureTask<Integer> task = new FutureTask<>(validation);
				new Thread(task).start();

				try {
					Integer result = task.get();
//					证书没问题
					if (Consts.AUTHORITY_FILE_AVAILABLE == result) {
//						创建服务器
						CoreServer.getIns().setMaxClientNum(validation.getEntity().getNode());
						Application.start();

//						启动时间验证定时器
						Timer timer = new Timer();
						timer.scheduleAtFixedRate(new TimerTask() {
							@Override
							public void run() {
								try {
									validation.getTimerClock().validate();
								} catch (Exception e) {
									timer.cancel();
//									停止一切服务
									Application.stop();
									showErrorMsg(e.getMessage());
									System.exit(0);
								}
							}
						}, 1000, 1000); // 每一分钟验证一次
					} else {
						showErrorMsg("错误代码：" + result);
						System.exit(0);
					}
				} catch (Exception e) {
					LoggerFactory.getLogger(Application.class).error("加密锁验证过程出现错误。{}", e.getMessage());
				}
			}
		}, SoftKey.KEY_FOR_SALER);
//		启动加密锁验证线程
		lt.start();
	}

	private static void jul2slf4j() {
		java.util.logging.LogManager.getLogManager().reset();
		SLF4JBridgeHandler.removeHandlersForRootLogger();
//		设置JUL的日志级别
		java.util.logging.Logger.getLogger("").setLevel(java.util.logging.Level.FINEST);
//		设置日志转换（桥接）
		SLF4JBridgeHandler.install();
	}

	protected static void stop() {
//		停止SpringBoot服务
		SpringApplication.exit(SpringUtil.getApplicationContext());

		CoreServer.getIns().stop();
		FileServer.getIns().stop();
	}

	protected static void start() {
		SpringApplication.run(Application.class);

		CoreServer.getIns().start();
		FileServer.getIns().start();
	}

	private static void showErrorMsg(String msg) {
//		PlatformImpl.runAndWait(() -> {
//			Alert alert = new Alert(Alert.AlertType.ERROR);
//			alert.setTitle("程序启动失败");
//			alert.setHeaderText(msg);
////			alert.setContentText("请联系我们");
//			alert.showAndWait();
//		});
		LoggerFactory.getLogger(Application.class).warn("程序启动失败,{}", msg);
		JOptionPane.showMessageDialog(null, msg, "程序启动失败", JOptionPane.ERROR_MESSAGE);
	}

	@Override
	public void run(String... args) throws Exception {
		Serializer.registerClass(Resource.class);
//		登录消息
		CoreServer.getIns().registerMessageHandler(LoginMessage.class, SpringUtil.getBean(LoginMessageHandler.class));
//		请求资源消息
		CoreServer.getIns().registerMessageHandler(ResourcesMessage.class, SpringUtil.getBean(ResourcesMessageHandler.class));
	}
}

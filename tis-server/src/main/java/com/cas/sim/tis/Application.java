package com.cas.sim.tis;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.cas.authority.Consts;
import com.cas.authority.validate.ValidateThread;
import com.cas.sim.tis.consts.SystemInfo;
import com.cas.sim.tis.socket.CoreServer;
import com.cas.sim.tis.socket.FileServer;
import com.cas.sim.tis.socket.message.LoginMessage;
import com.cas.sim.tis.socket.message.ResourcesMessage;
import com.cas.sim.tis.socket.message.handler.LoginMessageHandler;
import com.cas.sim.tis.socket.message.handler.ResourcesMessageHandler;
import com.cas.sim.tis.util.SpringUtil;
import com.softkey.SoftKey;

import cas.lock.ILockResult;
import cas.lock.LockThread;

@SpringBootApplication
public class Application implements CommandLineRunner {

	public static void main(String[] args) {
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
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			}
		}, SoftKey.KEY_FOR_SALER);
//		启动加密锁验证线程
		lt.start();
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

		JOptionPane.showMessageDialog(null, msg, "程序启动失败", JOptionPane.ERROR_MESSAGE);
	}

	@Override
	public void run(String... args) throws Exception {
//		注册消息

//		登录消息
		CoreServer.getIns().registerMessageHandler(LoginMessage.class, SpringUtil.getBean(LoginMessageHandler.class));
//		资源消息
		CoreServer.getIns().registerMessageHandler(ResourcesMessage.class, SpringUtil.getBean(ResourcesMessageHandler.class));
	}
}

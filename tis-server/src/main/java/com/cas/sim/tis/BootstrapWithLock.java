package com.cas.sim.tis;

import java.util.ResourceBundle;

import org.springframework.boot.SpringApplication;

import com.cas.sim.tis.config.ServerConfig;
import com.cas.sim.tis.util.SpringUtil;
import com.softkey.SoftKey;
import com.sun.javafx.application.PlatformImpl;

import cas.lock.ILockResult;
import cas.lock.LockThread;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

/**
 * 硬加密验证【locker-1.0.0】
 * @功能 BootstrapWithLock.java
 * @作者 Administrator
 * @创建日期 2017年10月26日
 * @修改人 Administrator
 */
@SuppressWarnings("restriction")
@Deprecated
public class BootstrapWithLock {
	public static void main(String[] args) {
		BootstrapWithLock app = new BootstrapWithLock();
		app.validateLock(args);
	}

	public void validateLock(final String[] args) {
		ILockResult lr = new ILockResult() {
			@Override
			public void doLockWrong(String errorCode) {
				PlatformImpl.startup(() -> {
					Alert alert = new Alert(AlertType.ERROR, null, ButtonType.OK);
					alert.setHeaderText(null);
					alert.setContentText(ResourceBundle.getBundle("i18n/messages").getString("unavaliable.locker"));
					alert.showAndWait();
					Platform.exit();
					System.exit(0);
				});
			}

			@Override
			public void doLockRight() {
				Application.jul2slf4j();
				SpringApplication.run(Application.class);

				// FIXME int maxLogin = LockManager.getMaxClientSize(SoftKey.KEY_FOR_SALER);
				int maxLogin = 50;
				SpringUtil.getBean(ServerConfig.class).setMaxLogin(maxLogin);
			}
		};
		LockThread lt = new LockThread(lr, SoftKey.KEY_FOR_SALER);
		lt.start();
	}
}

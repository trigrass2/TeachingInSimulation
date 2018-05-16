package com.cas.sim.tis;

import java.util.prefs.Preferences;

import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

import com.alibaba.dubbo.config.spring.context.annotation.DubboComponentScan;
import com.cas.sim.tis.consts.SettingConsts;
import com.cas.sim.tis.util.AlertUtil;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.view.HomeView;
import com.teamdev.jxbrowser.chromium.Refine;

import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import de.felixroske.jfxsupport.GUIState;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

@SpringBootApplication
@DubboComponentScan(basePackages = "com.cas.sim.tis.services")
public class Application extends AbstractJavaFxApplicationSupport implements ApplicationRunner, ApplicationContextAware {

	private ApplicationContext applicationContext;

	@Override
	public void run(ApplicationArguments args) throws Exception {
//		初始化JxBrowser信息
		Refine.init();

		launchApplicationView((ConfigurableApplicationContext) applicationContext);

		Platform.runLater(() -> {
			Stage stage = new Stage(StageStyle.TRANSPARENT);
			try {
				start(stage);
			} catch (Exception e) {
				e.printStackTrace();
			}
			Scene scene = new Scene(new Region());

			Preferences prefs = Preferences.userRoot().node(SettingConsts.REG_APP_PATH);
//			分辨率
			int width = prefs.getInt(SettingConsts.RESOLUTION_WIDTH, 1366);
			int height = prefs.getInt(SettingConsts.RESOLUTION_HEIGHT, 768);

			stage.setWidth(width);
			stage.setHeight(height);
			LoggerFactory.getLogger(getClass()).info("窗口大小：{}x{}", width, height);
			stage.setFullScreen(prefs.getBoolean(SettingConsts.SCREEN_MODE, false));
			LoggerFactory.getLogger(getClass()).info("窗口全屏：{}", stage.isFullScreen());
			stage.setOnCloseRequest(value -> {
				AlertUtil.showConfirm(MsgUtil.getMessage("alert.confirmation.exit"), resp -> {
					if (resp == ButtonType.YES) {
						Platform.exit();
						System.exit(0);
					} else {
						value.consume();
					}
				});
			});

			scene.setFill(null);
			GUIState.setScene(scene);

			showView(HomeView.class);
		});
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}

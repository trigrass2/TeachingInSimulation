package com.cas.sim.tis;

import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

import com.cas.sim.tis.util.AlertUtil;
import com.cas.sim.tis.util.AppPropertiesUtil;
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

			int width = AppPropertiesUtil.getIntValue("setting.resolution.width", 1366);
			int height = AppPropertiesUtil.getIntValue("setting.resolution.height", 768);
			boolean fullscreen = AppPropertiesUtil.getIntValue("setting.fullscreen.mode", 0) > 0;
			stage.setWidth(width);
			stage.setHeight(height);
			LoggerFactory.getLogger(getClass()).info("窗口大小：{}x{}", width, height);
			stage.setFullScreen(fullscreen);
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

package com.cas.sim.tis;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.BeansException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

import com.cas.sim.tis.util.FTPUtils;
import com.cas.sim.tis.view.HomeView;
import com.teamdev.jxbrowser.chromium.Refine;

import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import de.felixroske.jfxsupport.GUIState;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

@SpringBootApplication
public class Application extends AbstractJavaFxApplicationSupport implements ApplicationRunner, ApplicationContextAware {

	@Resource
	private FTPUtils ftpUtils;

	private ApplicationContext applicationContext;

	@Override
	public void run(ApplicationArguments args) throws Exception {
//		初始化JxBrowser信息
		Refine.init();

//		同步模型文件
		new Thread(() -> {
			List<String> pathArray = new ArrayList<>();
			try {
				ftpUtils.connect("/assets");
				ftpUtils.getPath("/assets", pathArray);
				ftpUtils.download(pathArray, "./");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				ftpUtils.disconnect();
			}
		}).start();

		launchApplicationView((ConfigurableApplicationContext) applicationContext);

		Platform.runLater(() -> {
			try {
				start(new Stage(StageStyle.TRANSPARENT));
			} catch (Exception e) {
				e.printStackTrace();
			}
			Scene scene = new Scene(new Region());
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

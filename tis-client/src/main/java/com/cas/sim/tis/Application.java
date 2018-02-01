package com.cas.sim.tis;

import org.springframework.beans.BeansException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

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

	private ApplicationContext applicationContext;

	@Override
	public void run(ApplicationArguments args) throws Exception {
//		初始化JxBrowser信息
		Refine.init();

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

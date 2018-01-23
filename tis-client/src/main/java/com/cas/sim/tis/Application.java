package com.cas.sim.tis;

import org.springframework.beans.BeansException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

import com.cas.sim.tis.view.HomeView;

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
	public void beforeInitialView(Stage stage, ConfigurableApplicationContext ctx) {
		GUIState.setScene(new Scene(new Region()));
		GUIState.getScene().setFill(null);

		stage.initStyle(StageStyle.TRANSPARENT);
		super.beforeInitialView(stage, ctx);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		
		launchApplicationView((ConfigurableApplicationContext) applicationContext);
		
		Platform.runLater(()->{
			try {
				start(new Stage(StageStyle.TRANSPARENT));
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			showView(HomeView.class);
		});
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}

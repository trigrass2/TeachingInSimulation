package com.cas.sim.tis;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.annotation.Resource;

import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.cas.sim.tis.config.ClientConfig;
import com.cas.sim.tis.consts.RoleConst;
import com.cas.sim.tis.message.LoginMessage;
import com.cas.sim.tis.message.handler.LoginMessageHandler;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.LoginView;
import com.cas.sim.tis.view.controller.LoginController;

import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import de.felixroske.jfxsupport.GUIState;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

@SpringBootApplication
public class Application extends AbstractJavaFxApplicationSupport implements ApplicationRunner {

	@Resource
	private ClientConfig clientConfig;
	private double xOffset;
	private double yOffset;

	public static void main(String[] args) {
		jul2slf4j();

//		文件同步
        // JavaFX
//        System.setProperty("prism.lcdtext", "false");
//        System.setProperty("prism.text", "t2k");
//        System.setProperty("javafx.animation.fullspeed", "false");

//        if(System.getProperty("jfx.background.render") == null) {
//            System.setProperty("jfx.background.render", "LWJGL-OpenGL3");
//        }

//		明确登录用的身份
		LoginController.USER_ROLE = RoleConst.TEACHER;

		launch(Application.class, LoginView.class, args);
	}

	private static void jul2slf4j() {
		LogManager.getLogManager().reset();
		SLF4JBridgeHandler.removeHandlersForRootLogger();
		Logger.getLogger("").setLevel(Level.FINEST);
		SLF4JBridgeHandler.install();
	}

	@Override
	public void beforeInitialView(Stage stage, ConfigurableApplicationContext ctx) {
		GUIState.setScene(new Scene(new Region()));
		GUIState.getScene().setFill(null);
		
		GUIState.getScene().setOnMouseDragged(e->{
			stage.setX(e.getScreenX() + xOffset);
            stage.setY(e.getScreenY() + yOffset);
		});
		GUIState.getScene().setOnMousePressed(e->{
			//按下鼠标后，记录当前鼠标的坐标
            xOffset = stage.getX() - e.getScreenX();
            yOffset = stage.getY() - e.getScreenY();
		});
		stage.initStyle(StageStyle.TRANSPARENT);
		super.beforeInitialView(stage, ctx);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		clientConfig.registerMessageHandler(LoginMessage.class, SpringUtil.getBean(LoginMessageHandler.class));
	}

}

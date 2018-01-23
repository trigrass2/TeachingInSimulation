package com.cas.sim.tis;

import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.slf4j.bridge.SLF4JBridgeHandler;

import com.cas.sim.tis.consts.SystemInfo;
import com.cas.sim.tis.message.LoginMessage;
import com.cas.sim.tis.message.handler.LoginMessageHandler;
import com.cas.sim.tis.message.listener.ClientMessageListener;
import com.cas.sim.tis.view.controller.LoginController;
import com.cas.sim.tis.view.controller.NetworkController;
import com.jme3.network.Network;
import com.jme3.network.NetworkClient;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LoginApp extends javafx.application.Application {
	private double xOffset;
	private double yOffset;
	private NetworkClient client;

	public static void main(String[] args) {
		jul2slf4j();
		
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
//		窗口无装饰
		primaryStage.initStyle(StageStyle.TRANSPARENT);

		FXMLLoader loader = new FXMLLoader();
		loader.setResources(ResourceBundle.getBundle("i18n/messages"));
		Region loginView = loader.load(LoginApp.class.getResourceAsStream("/view/Login.fxml"));
		LoginController loginController = loader.getController();

		loader = new FXMLLoader();
		loader.setResources(ResourceBundle.getBundle("i18n/messages"));
		Region settingView = loader.load(LoginApp.class.getResourceAsStream("/view/Network.fxml"));
		NetworkController settingController = loader.getController();
		
		client = Network.createClient(SystemInfo.APP_NAME, SystemInfo.APP_VERSION);
		client.addMessageListener(ClientMessageListener.INSTENCE);
		LoginMessageHandler loginMessageHandler = new LoginMessageHandler();
		ClientMessageListener.INSTENCE.registerMessageHandler(LoginMessage.class, loginMessageHandler);
		
//		手动注入
//		
		loginController.setClient(client);
//		
		loginMessageHandler.setLoginUIController(loginController);
		loginMessageHandler.setResourceBundle(ResourceBundle.getBundle("i18n/messages"));
//		
		settingController.setLoginView(loginView);
//		
		loginController.setSettingView(settingView);

//		背景无填充
		Scene scene = new Scene(loginView);
		scene.setOnMouseDragged(e -> {
			primaryStage.setX(e.getScreenX() + xOffset);
			primaryStage.setY(e.getScreenY() + yOffset);
		});
		scene.setOnMousePressed(e -> {
//			按下鼠标后，记录当前鼠标的坐标
			xOffset = primaryStage.getX() - e.getScreenX();
			yOffset = primaryStage.getY() - e.getScreenY();
		});
		scene.setFill(null);
		primaryStage.setScene(scene);

		primaryStage.show();
	}

	private static void jul2slf4j() {
		LogManager.getLogManager().reset();
		SLF4JBridgeHandler.removeHandlersForRootLogger();
		Logger.getLogger("").setLevel(Level.FINEST);
		SLF4JBridgeHandler.install();
	}
	
}
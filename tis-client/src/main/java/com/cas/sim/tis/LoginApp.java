package com.cas.sim.tis;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

import org.slf4j.bridge.SLF4JBridgeHandler;

import com.cas.sim.tis.consts.SettingConsts;
import com.cas.sim.tis.svg.SVGHelper;
import com.cas.sim.tis.view.controller.LoginController;
import com.cas.sim.tis.view.controller.NetworkController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LoginApp extends javafx.application.Application {
	private double xOffset;
	private double yOffset;

	public static void main(String[] args) {
		loadConfiguration();
		
		initSVG();

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

//		手动注入
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

	private static void initSVG() {
		try {
			// 加载svg图标
			SVGHelper.loadGlyphsFont(Application.class.getResource("/svg/iconfont.svg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void jul2slf4j() {
		LogManager.getLogManager().reset();
		SLF4JBridgeHandler.removeHandlersForRootLogger();
		Logger.getLogger("").setLevel(Level.FINEST);
		SLF4JBridgeHandler.install();
	}
	
	private static void loadConfiguration() {
// 		设置使JavaFx中可以使用GraphicsEnvironment功能
		System.setProperty("javafx.macosx.embedded", "true");
        java.awt.Toolkit.getDefaultToolkit();
		
		Preferences prefs = Preferences.userRoot().node(SettingConsts.REG_APP_PATH);
//		设置语言下拉框的默认值
		String userLang = prefs.get(SettingConsts.LANGUAGE, Locale.CHINA.toString());
		String[] arr = userLang.split("_");
		Locale userLocale = new Locale(arr[0], arr[1]);
		Locale.setDefault(userLocale);
	}

}

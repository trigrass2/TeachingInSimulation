package com.cas.sim.tis.test.jxbrowser;

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.BrowserType;
import com.teamdev.jxbrowser.chromium.Refine;
import com.teamdev.jxbrowser.chromium.javafx.BrowserView;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class TestJxBrowserJfx extends Application {
	public static void main(String[] args) {
		Refine.init();
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.initStyle(StageStyle.DECORATED);
		Browser browser = new Browser(BrowserType.LIGHTWEIGHT);
		BrowserView view = new BrowserView(browser);
		primaryStage.setScene(new Scene(view));
		
		browser.loadURL("http://file.keking.cn/");
//		browser.loadURL("http://58.214.15.134:8945/DigitalCampus_v3.0_nj/loginInit!init.action");
//		browser.setContextMenuHandler(new ContextMenuHandler() {
//			@Override
//			public void showContextMenu(ContextMenuParams params) {
//				System.out.println(params.getPageURL());
//			}
//		});

		primaryStage.show();
	}
}

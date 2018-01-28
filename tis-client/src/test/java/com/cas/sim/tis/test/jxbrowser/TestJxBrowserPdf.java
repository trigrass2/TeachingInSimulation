package com.cas.sim.tis.test.jxbrowser;

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.Refine;
import com.teamdev.jxbrowser.chromium.javafx.BrowserView;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class TestJxBrowserPdf extends Application {

	public static void main(String[] args) {
		Refine.init();
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.initStyle(StageStyle.DECORATED);
		Browser browser = new Browser();
		BrowserView view = new BrowserView(browser);
		primaryStage.setScene(new Scene(view));
		browser.loadURL("http://192.168.1.19:8082/Test/Fanuc0i参数说明书.pdf");
		primaryStage.show();
	}
}

package com.cas.sim.tis.test.jxbrowser;

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.BrowserType;
import com.teamdev.jxbrowser.chromium.Refine;
import com.teamdev.jxbrowser.chromium.javafx.BrowserView;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class TestJxBrowserJfx extends Application {
	public static void main(String[] args) {
		Refine.init(); // 破解
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		Browser browser = new Browser(BrowserType.LIGHTWEIGHT);
		BrowserView view = new BrowserView(browser);
		view.setPrefSize(500, 500);
		BrowserView view2 = new BrowserView(browser);
		view2.setPrefSize(500, 500);
		VBox vb = new VBox(view2,view);
		primaryStage.setScene(new Scene(vb));
		browser.loadURL(-1, ("http://www.baidu.com"));
		primaryStage.show();
	}
}

package com.cas.sim.tis.test.jxbrowser;

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.BrowserType;
import com.teamdev.jxbrowser.chromium.DownloadHandler;
import com.teamdev.jxbrowser.chromium.DownloadItem;
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
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		Browser browser = new Browser(BrowserType.LIGHTWEIGHT);
		BrowserView view = new BrowserView(browser);
		primaryStage.setScene(new Scene(view));
		browser.loadURL("http://112.35.87.233/Test/Fanuc0i参数说明书.pdf");
		// browser.executeJavaScript("var child=document.getElementById(\\\"download\\\");child.parentNode.removeChild(child);");
//		browser.executeJavaScript("alert(document.getElementById('download'))");
		browser.setDownloadHandler(new DownloadHandler() {
			@Override
			public boolean allowDownload(DownloadItem download) {
				System.out.println(download.getURL());
				return false;
			}
		});
		primaryStage.show();
	}
}

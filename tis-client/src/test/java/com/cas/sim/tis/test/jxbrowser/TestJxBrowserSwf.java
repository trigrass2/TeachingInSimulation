package com.cas.sim.tis.test.jxbrowser;

import java.util.TimerTask;

import javax.swing.Timer;

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.Refine;
import com.teamdev.jxbrowser.chromium.javafx.BrowserView;

import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class TestJxBrowserSwf extends Application {
	private static final String HTML_SWF_HEAD = //
			"<html xmlns='http://www.w3.org/1999/xhtml'>" + //
					"<head>" + //
					"<meta http-equiv='Content-Type' content='text/html; charset=utf-8' />" + //
					"<style type='text/css'>" + //
					"html,body{  " + //
					"height:98%;  " + //
					"margin:0px;  " + //
					"}" + //
					"#navigation {" + //
					"height:100%;" + //
					"}" + //
					"</style>" + //
					"</head>" + //
					"<body>" + //
					"<div id='navigation'>" + //
					"<embed align=center src='";
	private static final String HTML_SWF_END = //
			"' width='100%' height='100%' type=application/x-shockwave-flash wmode='transparent' quality='high' />" + //
					"</div>" + //
					"</body>" + //
					"</html>"; //

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

		primaryStage.show();

		Timer t = new Timer(3000, (e)-> {
			browser.loadHTML(HTML_SWF_HEAD + "http://192.168.1.19:8082/Test/teachResources.swf" + HTML_SWF_END);
		});
		
		t.start();
		
	}
}

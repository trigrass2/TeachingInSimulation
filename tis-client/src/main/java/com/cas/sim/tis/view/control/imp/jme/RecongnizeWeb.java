package com.cas.sim.tis.view.control.imp.jme;

import com.cas.sim.tis.util.MsgUtil;
import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.BrowserPreferences;
import com.teamdev.jxbrowser.chromium.BrowserType;
import com.teamdev.jxbrowser.chromium.DownloadHandler;
import com.teamdev.jxbrowser.chromium.DownloadItem;
import com.teamdev.jxbrowser.chromium.javafx.BrowserView;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class RecongnizeWeb extends VBox{

	private Button save = new Button(MsgUtil.getMessage("button.save"));
	private Button close = new Button(MsgUtil.getMessage("button.close"));

	private Browser browser = new Browser(BrowserType.LIGHTWEIGHT);

	public RecongnizeWeb() {
		HBox btns = new HBox(10);

		save.setOnAction(e -> {
			save();
		});

		close.setOnAction(e -> {
			close();
		});
		btns.getChildren().addAll(save, close);

		// 禁止下载文件
		browser.setDownloadHandler(new DownloadHandler() {
			@Override
			public boolean allowDownload(DownloadItem download) {
				return false;
			}
		});
		BrowserView view = new BrowserView(browser);
		BrowserPreferences preferences = browser.getPreferences();
		preferences.setTransparentBackground(true);
		browser.setPreferences(preferences);
		VBox.setVgrow(view, Priority.ALWAYS);
		
		this.getChildren().addAll(btns, view);
	}

	private void save() {
		browser.executeJavaScript("save()");
	}
	
	private void close() {
		browser.dispose(true);
		this.getParent().setMouseTransparent(true);
	}

	public void loadHTML(String html) {
		browser.loadHTML(html);
	}

	public void loadURL(String url) {
		browser.loadURL(url);
	}
}

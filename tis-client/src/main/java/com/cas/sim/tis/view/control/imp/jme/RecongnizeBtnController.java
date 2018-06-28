package com.cas.sim.tis.view.control.imp.jme;

import java.net.URL;
import java.util.ResourceBundle;

import com.cas.sim.tis.app.state.ElecCompState;
import com.sun.tools.internal.jxc.api.JXC;
import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.BrowserPreferences;
import com.teamdev.jxbrowser.chromium.BrowserType;
import com.teamdev.jxbrowser.chromium.DownloadHandler;
import com.teamdev.jxbrowser.chromium.DownloadItem;
import com.teamdev.jxbrowser.chromium.javafx.BrowserView;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.layout.VBox;

public class RecongnizeBtnController implements Initializable {
	@FXML
	private CheckBox intro;
	@FXML
	private CheckBox explode;
	@FXML
	private CheckBox transparent;
	@FXML
	private CheckBox autoRoate;
	@FXML
	private Control reset;
	@FXML
	private Control move;
	@FXML
	private Control rotate;
	@FXML
	private Control zoomIn;
	@FXML
	private Control zoomOut;
	@FXML
	private VBox area;

	private ElecCompState compState;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Browser browser = new Browser(BrowserType.LIGHTWEIGHT);
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
		view.setPrefSize(500, 800);
		area.getChildren().add(view);

		intro.selectedProperty().addListener((s, o, n) -> {
//			browser.loadHTML(html);
			area.setMouseTransparent(o);
		});
		explode.selectedProperty().addListener((s, o, n) -> {
			compState.explode(n);
		});
		transparent.selectedProperty().addListener((s, o, n) -> {
			compState.transparent(n);
		});
		autoRoate.selectedProperty().addListener((s, o, n) -> {
			compState.autoRotate(n);
		});
		reset.setOnMousePressed(e -> {
			compState.reset();
		});
		zoomIn.setOnMousePressed(e -> {
			compState.zoomIn();
		});
		zoomOut.setOnMousePressed(e -> {
			compState.zoomOut();
		});
	}

	public void setState(ElecCompState compState) {
		this.compState = compState;
	}

}

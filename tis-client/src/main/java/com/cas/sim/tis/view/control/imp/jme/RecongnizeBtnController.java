package com.cas.sim.tis.view.control.imp.jme;

import java.net.URL;
import java.util.ResourceBundle;

import com.cas.sim.tis.app.state.ElecCompState;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.ElecComp;
import com.cas.sim.tis.util.AppPropertiesUtil;
import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.BrowserPreferences;
import com.teamdev.jxbrowser.chromium.BrowserType;
import com.teamdev.jxbrowser.chromium.DownloadHandler;
import com.teamdev.jxbrowser.chromium.DownloadItem;
import com.teamdev.jxbrowser.chromium.JSValue;
import com.teamdev.jxbrowser.chromium.events.ScriptContextAdapter;
import com.teamdev.jxbrowser.chromium.events.ScriptContextEvent;
import com.teamdev.jxbrowser.chromium.javafx.BrowserView;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.layout.Priority;
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
	private ElecComp elecComp;
	private Browser browser;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		browser = new Browser(BrowserType.LIGHTWEIGHT);
		// 禁止下载文件
		browser.setDownloadHandler(new DownloadHandler() {
			@Override
			public boolean allowDownload(DownloadItem download) {
				return false;
			}
		});
		// 让JS可以调用browser对象方法
		browser.addScriptContextListener(new ScriptContextAdapter() {
			@Override
			public void onScriptContextCreated(ScriptContextEvent event) {
				Browser browser = event.getBrowser();
				JSValue window = browser.executeJavaScriptAndReturnValue("window");
				window.asObject().setProperty("java", RecongnizeBtnController.this);
			}
		});
		BrowserView view = new BrowserView(browser);
		BrowserPreferences preferences = browser.getPreferences();
		preferences.setTransparentBackground(true);
		browser.setPreferences(preferences);
		VBox.setVgrow(view, Priority.ALWAYS);
		area.getChildren().add(view);

		String address = AppPropertiesUtil.getStringValue("server.base.address");
		int port = AppPropertiesUtil.getIntValue("server.web.port", 0);
		int role = Session.get(Session.KEY_LOGIN_ROLE);
		intro.selectedProperty().addListener((s, o, n) -> {
			if (n) {
				if (elecComp == null) {
					return;
				}
				String path = String.format("http://%s:%d/recongnize/preview/%d/%d", address, port, elecComp.getId(), role);
				area.setMouseTransparent(false);
				browser.loadURL(path);
			} else {
				closeBrowser();
			}
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

	public void setElecComp(ElecComp elecComp) {
		this.elecComp = elecComp;
	}

	public void preview(String html) {
		browser.loadHTML(html);
	}

	public void closeBrowser() {
		browser.loadURL("about:blank");
		area.setMouseTransparent(true);
	}
}

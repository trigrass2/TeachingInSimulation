package com.cas.sim.tis.view.control.imp;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.cas.sim.tis.Application;
import com.cas.sim.tis.svg.SVGGlyph;
import com.cas.sim.tis.util.AlertUtil;
import com.cas.sim.tis.util.MsgUtil;

import de.felixroske.jfxsupport.GUIState;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * 放大、缩小、关闭按钮条
 * @功能 Decoration.java
 * @作者 Caowj
 * @创建日期 2017年12月29日
 * @修改人 Caowj
 */
public class Decoration extends HBox {
	@FXML
	private Button max;
	@FXML
	private Tooltip maxTip;

	private static boolean maximized;
	private static Bounds original;

	public Decoration() {
		FXMLLoader loader = new FXMLLoader();
		URL fxmlUrl = this.getClass().getResource("/view/Decoration.fxml");
		loader.setLocation(fxmlUrl);
		loader.setController(this);
		loader.setRoot(this);
		loader.setResources(ResourceBundle.getBundle("i18n/messages"));
		try {
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 窗口最小化
	 */
	@FXML
	private void min() {
		Application.getStage().setIconified(true);
	}

	/**
	 * 窗口最大化
	 */
	@FXML
	private void max() {
		maximized = !maximized;
		maximize();
	}

	public void maximize() {
		Scene scene = GUIState.getScene();
		Stage stage = GUIState.getStage();
		if (maximized) {
			if (original == null) {
				original = scene.getRoot().getLayoutBounds();
			}
			Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
			stage.setX(primaryScreenBounds.getMinX());
			stage.setY(primaryScreenBounds.getMinY());
			stage.setWidth(primaryScreenBounds.getWidth());
			stage.setHeight(primaryScreenBounds.getHeight());
			max.setGraphic(new SVGGlyph("iconfont.svg.revert", Color.web("#A2CBF3"), 10));
			maxTip.setText(MsgUtil.getMessage("button.revert"));
		} else {
			if (original == null) {
				return;
			}
			stage.setWidth(original.getWidth());
			stage.setHeight(original.getHeight());
			scene.getWindow().centerOnScreen();
			max.setGraphic(new SVGGlyph("iconfont.svg.max", Color.web("#A2CBF3"), 10));
			maxTip.setText(MsgUtil.getMessage("button.maximize"));
		}
	}

	/**
	 * 退出软件
	 */
	@FXML
	private void close() {
		AlertUtil.showConfirm(MsgUtil.getMessage("alert.confirmation.exit"), resp -> {
			if (resp == ButtonType.YES) {
				Platform.exit();
				System.exit(0);
			}
		});
	}
}

package com.cas.sim.tis.view.control.imp;

import java.io.IOException;
import java.net.URL;

import com.cas.sim.tis.Application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;

/**
 * 放大、缩小、关闭按钮条
 * @功能 TopBtns.java
 * @作者 caowj
 * @创建日期 2017年12月29日
 * @修改人 caowj
 */
public class Decoration extends HBox {

	@FXML
	private Button max;

	private boolean maximized;
	private Bounds original;

	public Decoration() {
		FXMLLoader loader = new FXMLLoader();
		URL fxmlUrl = this.getClass().getResource("/view/Decoration.fxml");
		loader.setLocation(fxmlUrl);
		loader.setController(this);
		loader.setRoot(this);
		try {
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void min() {
		// TODO 最小化
		Application.getStage().setIconified(true);
	}

	@FXML
	private void max() {
		// TODO 最大化
		if (!maximized) {
			original = Application.getScene().getRoot().getLayoutBounds();
			Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
			Application.getStage().setX(primaryScreenBounds.getMinX());
			Application.getStage().setY(primaryScreenBounds.getMinY());
			Application.getStage().setWidth(primaryScreenBounds.getWidth());
			Application.getStage().setHeight(primaryScreenBounds.getHeight());
			maximized = true;
			max.setGraphic(new ImageView("/static/images/basic/revert.png"));
		} else {
			Application.getStage().setX(original.getMinX());
			Application.getStage().setY(original.getMinY());
			Application.getStage().setWidth(original.getWidth());
			Application.getStage().setHeight(original.getHeight());
			maximized = false;
			max.setGraphic(new ImageView("/static/images/basic/max.png"));
		}
	}

	@FXML
	private void close() {
		// FIXME
		System.exit(0);
	}
}

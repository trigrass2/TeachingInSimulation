package com.cas.sim.tis.view.control.imp;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.cas.sim.tis.Application;
import com.cas.sim.tis.util.MsgUtil;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;

/**
 * 放大、缩小、关闭按钮条
 * @功能 Decoration.java
 * @作者 Caowj
 * @创建日期 2017年12月29日
 * @修改人 Caowj
 */
public class Decoration extends HBox {
//	@Resource
//	private MessageSource messageSource; // 自动注入对象
	@FXML
	private Button max;
	@FXML
	private Tooltip maxTip;

	private boolean maximized;
	private Bounds original;

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
		if (!maximized) {
			original = Application.getScene().getRoot().getLayoutBounds();
			Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
			Application.getStage().setX(primaryScreenBounds.getMinX());
			Application.getStage().setY(primaryScreenBounds.getMinY());
			Application.getStage().setWidth(primaryScreenBounds.getWidth());
			Application.getStage().setHeight(primaryScreenBounds.getHeight());
			maximized = true;
			max.setGraphic(new ImageView("/static/images/basic/revert.png"));
			maxTip.setText(MsgUtil.getMessage("button.revert"));
		} else {
			Application.getStage().setWidth(original.getWidth());
			Application.getStage().setHeight(original.getHeight());
			Application.getScene().getWindow().centerOnScreen();
			maximized = false;
			max.setGraphic(new ImageView("/static/images/basic/max.png"));
			maxTip.setText(MsgUtil.getMessage("button.maximize"));
		}
	}

	/**
	 * 退出软件
	 */
	@FXML
	private void close() {
		// FIXME
		System.exit(0);
	}
}

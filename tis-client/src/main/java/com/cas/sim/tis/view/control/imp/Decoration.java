package com.cas.sim.tis.view.control.imp;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.cas.sim.tis.svg.SVGGlyph;
import com.cas.sim.tis.util.AlertUtil;
import com.cas.sim.tis.util.MsgUtil;

import de.felixroske.jfxsupport.GUIState;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

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
		maximize();
	}

	/**
	 * 窗口最小化
	 */
	@FXML
	private void min() {
		GUIState.getStage().setIconified(true);
	}

	/**
	 * 窗口最大化
	 */
	@FXML
	private void max() {
		GUIState.getStage().setMaximized(!GUIState.getStage().isMaximized());
		maximize();
	}

	public void maximize() {
		if (GUIState.getStage().isMaximized()) {
			max.setGraphic(new SVGGlyph("iconfont.svg.revert", Color.web("#A2CBF3"), 10));
			maxTip.setText(MsgUtil.getMessage("button.revert"));
		} else {
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

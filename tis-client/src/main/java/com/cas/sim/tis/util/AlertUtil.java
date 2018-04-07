package com.cas.sim.tis.util;

import java.util.function.Consumer;

import de.felixroske.jfxsupport.GUIState;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Window;
/**
 * 弹出窗口工具类
 * @功能 AlertUtil.java
 * @作者 Caowj
 * @创建日期 2018年2月26日
 * @修改人 Caowj
 */
public class AlertUtil {
	public static final void showAlert(AlertType type, String reason) {
		Alert alert = new Alert(type, reason);
		alert.initOwner(GUIState.getStage());
		alert.setHeaderText(null);
		alert.showAndWait();
	}

	public static final void showConfirm(String confirm, Consumer<ButtonType> consumer) {
		showConfirm(GUIState.getStage(), confirm, consumer);
	}

	public static void showConfirm(Window win, String confirm, Consumer<ButtonType> consumer) {
		Alert alert = new Alert(AlertType.CONFIRMATION, confirm, ButtonType.YES, ButtonType.NO);
		alert.initOwner(win);
		alert.setHeaderText(null);
		alert.showAndWait().ifPresent(consumer);
	}
}

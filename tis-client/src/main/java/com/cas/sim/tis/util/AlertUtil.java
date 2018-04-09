package com.cas.sim.tis.util;

import java.util.function.Consumer;

import com.cas.sim.tis.view.control.imp.dialog.Tip;
import com.cas.sim.tis.view.control.imp.dialog.Tip.TipType;
import com.cas.sim.tis.view.control.imp.dialog.TipDialog;

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
	private static TipDialog dialog;

	public static void showAlert(AlertType type, String reason) {
		Alert alert = new Alert(type, reason);
		alert.initOwner(GUIState.getStage());
		alert.setHeaderText(null);
		alert.showAndWait();
	}

	public static void showConfirm(String confirm, Consumer<ButtonType> consumer) {
		showConfirm(GUIState.getStage(), confirm, consumer);
	}

	public static void showConfirm(Window win, String confirm, Consumer<ButtonType> consumer) {
		Alert alert = new Alert(AlertType.CONFIRMATION, confirm, ButtonType.YES, ButtonType.NO);
		alert.initOwner(win);
		alert.setHeaderText(null);
		alert.showAndWait().ifPresent(consumer);
	}

	public static void showTip(TipType type, String msg) {
		if (dialog != null) {
			dialog.close();
		}
		dialog = new TipDialog();
		dialog.setDialogPane(new Tip(type, msg));
		dialog.setPrefSize(msg.length() * 14 + 115, 65);
		dialog.show();
	}
}

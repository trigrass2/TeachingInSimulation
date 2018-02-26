package com.cas.sim.tis.util;

import java.util.function.Consumer;

import de.felixroske.jfxsupport.GUIState;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

public class AlertUtil {
	public static final void showAlert(AlertType type, String reason) {
		Alert alert = new Alert(type, reason);
		alert.initOwner(GUIState.getStage());
		alert.setHeaderText(null);
		alert.showAndWait();
	}

	public static final void showConfirm(String confirm, Consumer<ButtonType> consumer) {
		Alert alert = new Alert(AlertType.CONFIRMATION, confirm, ButtonType.YES, ButtonType.NO);
		alert.initOwner(GUIState.getStage());
		alert.setHeaderText(null);
		alert.showAndWait().ifPresent(consumer);
	}
}

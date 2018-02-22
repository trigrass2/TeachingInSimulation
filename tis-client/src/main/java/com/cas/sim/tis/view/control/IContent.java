package com.cas.sim.tis.view.control;

import java.util.function.Consumer;

import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.action.LibraryAction;

import de.felixroske.jfxsupport.GUIState;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

public interface IContent extends IDistory {
//	StackPane的content
	Node[] getContent();

	default void showAlert(AlertType type, String reason) {
		Alert alert = new Alert(type, reason);
		alert.initOwner(GUIState.getStage());
		alert.setHeaderText(null);
		alert.showAndWait();
	}

	default void showConfirm(String confirm, Consumer<ButtonType> consumer) {
		Alert alert = new Alert(AlertType.CONFIRMATION, confirm, ButtonType.YES, ButtonType.NO);
		alert.initOwner(GUIState.getStage());
		alert.setHeaderText(null);
		alert.showAndWait().ifPresent(consumer);
	}
}

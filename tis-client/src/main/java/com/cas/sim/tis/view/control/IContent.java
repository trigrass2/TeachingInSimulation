package com.cas.sim.tis.view.control;

import de.felixroske.jfxsupport.GUIState;
import javafx.scene.Node;
import javafx.scene.control.Alert;
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
}

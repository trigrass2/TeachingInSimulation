package com.cas.sim.tis.view.control.imp.dialog;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class DialogPane<R>extends VBox {
	@FXML
	private HBox header;
	@FXML
	private Label title;

	protected Dialog<R> dialog;
	private double xOffset;
	private double yOffset;

	public DialogPane() {
		FXMLLoader loader = new FXMLLoader();
		URL fxmlUrl = this.getClass().getResource("/view/Dialog.fxml");
		loader.setLocation(fxmlUrl);
		loader.setController(this);
		loader.setRoot(this);
		loader.setResources(ResourceBundle.getBundle("i18n/messages"));
		try {
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.getStylesheets().add(getClass().getResource("/static/css/basic.css").toExternalForm());
		this.header.setOnMouseDragged(e -> {
			if (dialog == null) {
				return;
			}
			dialog.getWindow().setX(e.getScreenX() + xOffset);
			dialog.getWindow().setY(e.getScreenY() + yOffset);
		});
		this.header.setOnMousePressed(e -> {
			if (dialog == null) {
				return;
			}
			xOffset = dialog.getWindow().getX() - e.getScreenX();
			yOffset = dialog.getWindow().getY() - e.getScreenY();
		});
	}

	public void setTitle(String title) {
		this.title.setText(title);
	}

	public void setDialog(Dialog<R> dialog) {
		this.dialog = dialog;
	}

	public void setResult(R value) {
		this.dialog.setResult(value);
	}

	@FXML
	private void close() {
		dialog.close();
	}
}

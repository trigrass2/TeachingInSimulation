package com.cas.sim.tis.view.control.imp.dialog;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class DialogPane<R>extends VBox {
	private static final Logger LOG = LoggerFactory.getLogger(DialogPane.class);
	@FXML
	private HBox header;
	@FXML
	private Label title;
	@FXML
	private Button close;

	protected Dialog<R> dialog;
	private double xOffset;
	private double yOffset;

	private BooleanProperty closeable = new SimpleBooleanProperty(true) {
		public void set(boolean newValue) {
			close.setVisible(newValue);
		};
	};

	public DialogPane() {
		loadFXML();
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

	private void loadFXML() {
		FXMLLoader loader = new FXMLLoader();
		URL fxmlUrl = this.getClass().getResource("/view/Dialog.fxml");
		loader.setLocation(fxmlUrl);
		loader.setController(this);
		loader.setRoot(this);
		loader.setResources(ResourceBundle.getBundle("i18n/messages"));
		try {
			loader.load();
			LOG.debug("加载FXML界面{}完成", fxmlUrl);
		} catch (IOException e) {
			e.printStackTrace();
			LOG.error("加载FXML界面{}失败，错误信息：{}", fxmlUrl, e.getMessage());
		}
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

	public BooleanProperty closeableProperty() {
		return closeable;
	}

	public void setCloseable(boolean closeable) {
		closeableProperty().set(closeable);
	}
}

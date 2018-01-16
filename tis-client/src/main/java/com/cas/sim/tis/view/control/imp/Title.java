package com.cas.sim.tis.view.control.imp;

import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class Title extends HBox {
	@FXML
	private Label title;
	
	public Title() {
		FXMLLoader loader = new FXMLLoader();
		URL fxmlUrl = this.getClass().getResource("/view/Title.fxml");
		loader.setLocation(fxmlUrl);
		loader.setController(this);
		loader.setRoot(this);
		try {
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getTitle() {
		return title.getText();
	}

	public void setTitle(String title) {
		this.title.setText(title);
	}
}

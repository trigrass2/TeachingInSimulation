package com.cas.sim.tis.view.control.imp;

import java.io.IOException;
import java.net.URL;

import com.cas.sim.tis.view.control.IContent;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

public class Preparation extends HBox implements IContent{
	public Preparation() {
		FXMLLoader loader = new FXMLLoader();
		URL fxmlUrl = this.getClass().getResource("/view/Preparation.fxml");
		loader.setLocation(fxmlUrl);
		loader.setController(this);
		loader.setRoot(this);
		try {
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Region getContent() {
		return this;
	}
}

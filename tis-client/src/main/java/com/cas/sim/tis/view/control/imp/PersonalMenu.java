package com.cas.sim.tis.view.control.imp;

import java.io.IOException;
import java.net.URL;

import com.cas.sim.tis.view.control.ILeftMenu;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class PersonalMenu extends VBox implements ILeftMenu {
	
	public PersonalMenu() {
		FXMLLoader loader = new FXMLLoader();
		URL fxmlUrl = this.getClass().getResource("/view/PersonalMenu.fxml");
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
	public Region getLeftContent() {
		return this;
	}

}

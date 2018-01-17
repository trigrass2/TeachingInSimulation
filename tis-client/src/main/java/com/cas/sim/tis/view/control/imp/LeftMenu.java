package com.cas.sim.tis.view.control.imp;

import java.io.IOException;
import java.net.URL;

import com.cas.sim.tis.view.control.ILeftContent;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public abstract class LeftMenu extends VBox implements ILeftContent {

	@FXML
	private VBox menu;
	
	public LeftMenu() {
		FXMLLoader loader = new FXMLLoader();
		URL fxmlUrl = this.getClass().getResource("/view/LeftMenu.fxml");
		loader.setLocation(fxmlUrl);
		loader.setController(this);
		loader.setRoot(this);
		try {
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected abstract void initMenu();
	
	protected void addMenuItem(String name, String icon, EventHandler<ActionEvent> e) {
		Button button = new Button(name, new ImageView(new Image(icon)));
		button.setOnAction(e);
		button.getStyleClass().add("left-menu");
		menu.getChildren().add(button);
	}

	@Override
	public Region getLeftContent() {
		return this;
	}

}

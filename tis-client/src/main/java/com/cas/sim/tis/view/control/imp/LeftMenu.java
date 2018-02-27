package com.cas.sim.tis.view.control.imp;

import java.io.IOException;
import java.net.URL;

import com.cas.sim.tis.action.UserAction;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.User;
import com.cas.sim.tis.svg.SVGGlyph;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.control.ILeftContent;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public abstract class LeftMenu extends VBox implements ILeftContent {

	@FXML
	private Label name;
	@FXML
	private Label code;
	@FXML
	protected VBox menu;

	private ToggleGroup items = new ToggleGroup();

	/**
	 * 
	 */
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
		items.selectedToggleProperty().addListener((b, o, n) -> {
			if (n == null) {
				items.selectToggle(o);
			}
		});
		User user = SpringUtil.getBean(UserAction.class).findUserByID(Session.get(Session.KEY_LOGIN_ID));
		name.setText(user.getName());
		code.setText(user.getCode());
		initMenu();
	}

	protected abstract void initMenu();

	protected void addMenuItem(String name, String svg, EventHandler<ActionEvent> e) {
		SVGGlyph glyph = new SVGGlyph(svg, Color.WHITE, 22);
		ToggleButton button = new ToggleButton(name, glyph);
		button.setOnAction(e);
		button.getStyleClass().add("left-menu");
		menu.getChildren().add(button);
		items.getToggles().add(button);
	}

	@Override
	public Region getLeftContent() {
		return this;
	}

}

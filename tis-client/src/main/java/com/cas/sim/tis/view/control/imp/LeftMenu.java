package com.cas.sim.tis.view.control.imp;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cas.sim.tis.action.UserAction;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.User;
import com.cas.sim.tis.svg.SVGGlyph;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.control.ILeftContent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public abstract class LeftMenu extends VBox implements ILeftContent, Initializable {
	private static final Logger LOG = LoggerFactory.getLogger(LeftMenu.class);
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
		loadFXML();
		items.selectedToggleProperty().addListener((b, o, n) -> {
			if (n == null) {
				items.selectToggle(o);
			}
		});
		initMenu();
	}

	private void loadFXML() {
		FXMLLoader loader = new FXMLLoader();
		URL fxmlUrl = this.getClass().getResource("/view/LeftMenu.fxml");
		loader.setLocation(fxmlUrl);
		loader.setController(this);
		loader.setRoot(this);
		try {
			loader.load();
			LOG.debug("加载FXML界面{}完成", fxmlUrl);
		} catch (IOException e) {
			e.printStackTrace();
			LOG.error("加载FXML界面{}失败，错误信息：{}", fxmlUrl, e.getMessage());
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		User user = SpringUtil.getBean(UserAction.class).findUserByID(Session.get(Session.KEY_LOGIN_ID));
		name.setText(user.getName());
		code.setText(user.getCode());
	}

	protected abstract void initMenu();

	protected void addMenuItem(String name, String svg, Consumer<Void> event) {
		SVGGlyph glyph = new SVGGlyph(svg, Color.WHITE, 22);
		ToggleButton button = new ToggleButton(name, glyph);
		button.selectedProperty().addListener((b, o, n) -> {
			event.accept(null);
		});
		button.getStyleClass().add("left-menu");
		menu.getChildren().add(button);
		items.getToggles().add(button);
	}

	@Override
	public Region getLeftContent() {
		return this;
	}

}

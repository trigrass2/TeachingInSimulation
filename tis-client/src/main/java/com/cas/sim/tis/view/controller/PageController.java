package com.cas.sim.tis.view.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.cas.sim.tis.view.control.IContent;
import com.cas.sim.tis.view.control.ILeftContent;

import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

@FXMLController
public class PageController implements Initializable {

	private boolean visible = true;

	@FXML
	private Pane leftBlock;

	@FXML
	private HBox leftMenu;

	@FXML
	private StackPane leftContent;

	@FXML
	private StackPane content;

	@FXML
	private Region arrow;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

	@FXML
	private void back() {
		show();
	}

	@FXML
	private void toggle() {
		if (visible) {
			hide();
		} else {
			show();
		}
	}

	public void loadLeftMenu(ILeftContent leftMenu) {
		this.leftContent.getChildren().clear();
		if (leftMenu != null) {
			this.leftContent.getChildren().add(leftMenu.getLeftContent());
		}
	}

	public void loadContent(IContent content) {
		this.content.getChildren().clear();
		if (content != null) {
			this.content.getChildren().add(content.getContent());
		}
	}

	private void hide() {
		// 隐藏左侧菜单
		leftMenu.setTranslateX(-230);
		leftBlock.setMaxWidth(0);
		leftBlock.setMinWidth(0);
		arrow.getStyleClass().clear();
		arrow.getStyleClass().add("show");
		visible = false;
	}

	private void show() {
		// 显示左侧菜单
		leftMenu.setTranslateX(0);
		leftBlock.setMaxWidth(230);
		leftBlock.setMinWidth(230);
		arrow.getStyleClass().clear();
		arrow.getStyleClass().add("hide");
		visible = true;
	}
}

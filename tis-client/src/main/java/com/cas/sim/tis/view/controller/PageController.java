package com.cas.sim.tis.view.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.cas.sim.tis.Application;
import com.cas.sim.tis.view.HomeView;
import com.cas.sim.tis.view.control.IContent;
import com.cas.sim.tis.view.control.ILeftContent;

import de.felixroske.jfxsupport.FXMLController;
import de.felixroske.jfxsupport.GUIState;
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
	private Pane handle;

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

	/**
	 * 上一层内容
	 */
	private IContent level2Content;
	/**
	 * 当前层内容
	 */
	private IContent level1Content;

	private PageLevel level;

	private double xOffset;

	private double yOffset;

	public enum PageLevel {
		Level1, Level2;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		handle.setOnMouseDragged(e -> {
			GUIState.getStage().setX(e.getScreenX() + xOffset);
			GUIState.getStage().setY(e.getScreenY() + yOffset);
		});
		handle.setOnMousePressed(e -> {
//			按下鼠标后，记录当前鼠标的坐标
			xOffset = GUIState.getStage().getX() - e.getScreenX();
			yOffset = GUIState.getStage().getY() - e.getScreenY();
		});
	}

	@FXML
	private void back() {
		// 返回上一个界面
		if (this.level == PageLevel.Level2) {
			this.level = PageLevel.Level1;
			this.level1Content.distroy();
			this.level1Content = level2Content;
			this.level2Content = null;
			this.content.getChildren().add(level1Content.getContent());
		} else {
			clear();
			Application.showView(HomeView.class);
		}
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

	public void loadContent(IContent content, PageLevel level) {
		this.content.getChildren().clear();
		if (content == null) {
			this.level1Content = null;
			this.level2Content = null;
			return;
		}
		if (PageLevel.Level1 == level || level == null) {
			this.level = PageLevel.Level1;
			this.level1Content = content;
			this.level2Content = null;
		} else if (PageLevel.Level2 == level) {
			this.level = PageLevel.Level2;
			if (this.level2Content != null) {
				this.level2Content.distroy();
			}
			this.level2Content = this.level1Content;
			this.level1Content = content;
		}
		this.content.getChildren().add(content.getContent());
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
		this.leftMenu.layout();
		this.leftBlock.getParent().layout();
	}

	private void clear() {
		if (level1Content != null) {
			this.level1Content.distroy();
			this.level1Content = null;
		}
		if (level2Content != null) {
			this.level2Content.distroy();
			this.level2Content = null;
		}
		this.content.getChildren().clear();
		this.leftContent.getChildren().clear();
	}
}

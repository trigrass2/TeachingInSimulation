package com.cas.sim.tis.view.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.cas.sim.tis.Application;
import com.cas.sim.tis.consts.RoleConst;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.PageView;
import com.cas.sim.tis.view.control.imp.HomeMenu;
import com.cas.sim.tis.view.control.imp.jme.Recongnize3D;
import com.cas.sim.tis.view.control.imp.jme.RecongnizeMenu;
import com.cas.sim.tis.view.control.imp.resource.ResourceMenu;
import com.cas.sim.tis.view.controller.PageController.PageLevel;

import de.felixroske.jfxsupport.FXMLController;
import de.felixroske.jfxsupport.GUIState;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

@FXMLController
public class HomeController implements Initializable {
	@FXML
	private HBox menu;
	@FXML
	private Pane handle;
	private List<HomeMenu> homeMenus = new ArrayList<HomeMenu>();
	private double xOffset;
	private double yOffset;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// 判断当前登录人角色，加载对应菜单
		int role = Session.get(Session.KEY_LOGIN_ROLE);
		if (RoleConst.ADMIN == role) {
			resourceMenuItem();
		} else if (RoleConst.TEACHER == role) {
			resourceMenuItem();
		} else if (RoleConst.STUDENT == role) {

		}
		menu.getChildren().addAll(homeMenus);
		
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

	private void resourceMenuItem() {
		HomeMenu resource = new HomeMenu();
		resource.setMenuName("资源库");
		resource.setMenuIcon("static/images/menu/resource.png");
		resource.setOnMouseClicked(e -> {
			// 跳转到资源库页面
			Application.showView(PageView.class);
			PageController controller = SpringUtil.getBean(PageController.class);
			ResourceMenu menu = new ResourceMenu();
			controller.loadLeftMenu(menu);
			controller.loadContent(null, null);
		});
		homeMenus.add(resource);
	}

	/**
	 * 认知
	 */
	public void recongize() {
		// 跳转到认知页面
		Application.showView(PageView.class);

		PageController controller = SpringUtil.getBean(PageController.class);
		Recongnize3D content = new Recongnize3D();
		
		controller.loadLeftMenu(new RecongnizeMenu(content));
		controller.loadContent(content, PageLevel.Level1);
	}

	/**
	 * 典型案例
	 */
	public void typicalCase() {
	}

	/**
	 * 接线练习
	 */
	public void exersize() {
	}

	/**
	 * 电路维修
	 */
	public void repair() {
	}

}

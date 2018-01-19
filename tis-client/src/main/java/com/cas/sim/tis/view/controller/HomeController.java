package com.cas.sim.tis.view.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.Resource;

import com.cas.sim.tis.Application;
import com.cas.sim.tis.app.JmeApplication;
import com.cas.sim.tis.consts.RoleConst;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.PageView;
import com.cas.sim.tis.view.control.imp.HomeMenu;
import com.cas.sim.tis.view.control.imp.jme.Recongnize3D;
import com.cas.sim.tis.view.control.imp.jme.RecongnizeMenu;
import com.cas.sim.tis.view.control.imp.resource.AdminResourceList;
import com.cas.sim.tis.view.control.imp.resource.ResourceMenu;
import com.cas.sim.tis.view.control.imp.resource.StudentResourceList;
import com.jme3.system.AppSettings;
import com.jme3x.jfx.injfx.JmeToJFXIntegrator;

import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;

@FXMLController
public class HomeController implements Initializable {
	@FXML
	private HBox menu;

	@Resource
	private JmeApplication jmeApp;

	private List<HomeMenu> homeMenus = new ArrayList<HomeMenu>();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// 判断当前登录人角色，加载对应菜单
		if (RoleConst.ADMIN == LoginController.USER_ROLE) {
			resourceMenuItem();
		} else if (RoleConst.TEACHER == LoginController.USER_ROLE) {
			resourceMenuItem();
		} else if (RoleConst.STUDENT == LoginController.USER_ROLE) {

		}
		menu.getChildren().addAll(homeMenus);
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
			if (RoleConst.ADMIN == LoginController.USER_ROLE) {
				controller.loadContent(new AdminResourceList(true));
			} else if (RoleConst.TEACHER == LoginController.USER_ROLE) {
				controller.loadContent(new AdminResourceList(false));
			} else if (RoleConst.STUDENT == LoginController.USER_ROLE) {
				controller.loadContent(new StudentResourceList());
			}
		});
		homeMenus.add(resource);
	}

	public void recongize() {
		// 跳转到认知页面
		Application.showView(PageView.class);
		PageController controller = SpringUtil.getBean(PageController.class);
		controller.loadLeftMenu(new RecongnizeMenu());

		Recongnize3D content = new Recongnize3D();
		controller.loadContent(content);

		final AppSettings settings = JmeToJFXIntegrator.prepareSettings(new AppSettings(true), 60);
		jmeApp.setSettings(settings);
		jmeApp.setShowSettings(false);
		JmeToJFXIntegrator.startAndBindMainViewPort(jmeApp, content.getCanvas(), Thread::new);
	}

}

package com.cas.sim.tis.view.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.cas.sim.tis.Application;
import com.cas.sim.tis.app.state.ElecCaseState.CaseMode;
import com.cas.sim.tis.app.state.broken.BrokenCaseState;
import com.cas.sim.tis.app.state.free.FreeCaseState;
import com.cas.sim.tis.app.state.typical.TypicalCaseState;
import com.cas.sim.tis.consts.MenuEnum;
import com.cas.sim.tis.consts.RoleConst;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.User;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.PageView;
import com.cas.sim.tis.view.control.imp.Decoration;
import com.cas.sim.tis.view.control.imp.HomeMenu;
import com.cas.sim.tis.view.control.imp.broken.BrokenCase3D;
import com.cas.sim.tis.view.control.imp.broken.BrokenCaseBtnController;
import com.cas.sim.tis.view.control.imp.broken.BrokenCaseMenu;
import com.cas.sim.tis.view.control.imp.dialog.Dialog;
import com.cas.sim.tis.view.control.imp.free.FreeCase3D;
import com.cas.sim.tis.view.control.imp.free.FreeCaseBtnController;
import com.cas.sim.tis.view.control.imp.free.FreeCaseMenu;
import com.cas.sim.tis.view.control.imp.jme.Recongnize3D;
import com.cas.sim.tis.view.control.imp.jme.RecongnizeMenu;
import com.cas.sim.tis.view.control.imp.jme.TypicalCase3D;
import com.cas.sim.tis.view.control.imp.jme.TypicalCaseBtnController;
import com.cas.sim.tis.view.control.imp.jme.TypicalCaseMenu;
import com.cas.sim.tis.view.control.imp.setting.SettingDialog;
import com.cas.sim.tis.view.controller.PageController.PageLevel;

import de.felixroske.jfxsupport.FXMLController;
import de.felixroske.jfxsupport.GUIState;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

@FXMLController
public class HomeController implements Initializable {
	@FXML
	private HBox menu;
	@FXML
	private Pane handle;
	@FXML
	private Label info;
	@FXML
	private Decoration decoration;

	private List<HomeMenu> homeMenus = new ArrayList<HomeMenu>();
	private double xOffset;
	private double yOffset;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// 判断当前登录人角色，加载对应菜单
		User user = Session.get(Session.KEY_OBJECT);
		int role = user.getRole();

		info.setText(user.getName() + "\r\n" + user.getCode());

		if (RoleConst.ADMIN == role) {
//			管理员的菜单
			buildMenu(MenuEnum.Information, //
					MenuEnum.Resource, //
					MenuEnum.Questions//
			);
		} else if (RoleConst.TEACHER == role) {
//			教师的菜单
			buildMenu(MenuEnum.Resource, //
					MenuEnum.Questions, //
					MenuEnum.Preparation //
			);
		} else if (RoleConst.STUDENT == role) {
//			学生的菜单
			buildMenu(MenuEnum.Resource, //
					MenuEnum.Questions, //
					MenuEnum.Lessones //
			);
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

	private void buildMenu(MenuEnum... menuItems) {
		if (menuItems == null) {
			return;
		}
		for (int i = 0; i < menuItems.length; i++) {
			HomeMenu homeMenu = new HomeMenu();
			homeMenu.setMenuName(MsgUtil.getMessage(menuItems[i].getMsgKey()));
			homeMenu.setMenuIcon(menuItems[i].getImagePath());
			homeMenu.setOnMouseClicked(menuItems[i]);
			homeMenus.add(homeMenu);
		}
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

		SpringUtil.getBean(PageController.class).showLoading();
	}

	/**
	 * 典型案例
	 */
	public void typicalCase() {
		// 跳转到典型案例页面
		Application.showView(PageView.class);

		PageController controller = SpringUtil.getBean(PageController.class);
		TypicalCase3D content = null;
		int role = Session.get(Session.KEY_LOGIN_ROLE);
		if (role >= RoleConst.TEACHER) {
			content = new TypicalCase3D(new TypicalCaseState(), new TypicalCaseBtnController(CaseMode.VIEW_MODE, CaseMode.TYPICAL_TRAIN_MODE, CaseMode.EDIT_MODE));
		} else {
			content = new TypicalCase3D(new TypicalCaseState(), new TypicalCaseBtnController(CaseMode.VIEW_MODE, CaseMode.TYPICAL_TRAIN_MODE));
		}

		TypicalCaseMenu menu = new TypicalCaseMenu(content);
		menu.setName(MsgUtil.getMessage("menu.item.typical"));

		controller.loadLeftMenu(menu);
		controller.loadContent(content, PageLevel.Level1);

//		SpringUtil.getBean(PageController.class).showLoading();
	}

	/**
	 * 接线练习
	 */
	public void exersize() {
		// 跳转到自由接线页面
		Application.showView(PageView.class);
		
		PageController controller = SpringUtil.getBean(PageController.class);
		FreeCase3D content = new FreeCase3D(new FreeCaseState(), new FreeCaseBtnController(CaseMode.EDIT_MODE));
	
		FreeCaseMenu menu = new FreeCaseMenu(content);
		menu.setName(MsgUtil.getMessage("menu.item.free"));

		controller.loadLeftMenu(menu);
		controller.loadContent(content, PageLevel.Level1);
	}

	/**
	 * 电路维修
	 */
	public void repair() {
		// 跳转到维修案例页面
		Application.showView(PageView.class);

		PageController controller = SpringUtil.getBean(PageController.class);
		BrokenCase3D content = null;
		int role = Session.get(Session.KEY_LOGIN_ROLE);
		if (role >= RoleConst.TEACHER) {
			content = new BrokenCase3D(new BrokenCaseState(), new BrokenCaseBtnController(CaseMode.BROKEN_TRAIN_MODE, CaseMode.EDIT_MODE));
		} else {
			content = new BrokenCase3D(new BrokenCaseState(), new BrokenCaseBtnController(CaseMode.BROKEN_TRAIN_MODE));
		}

		BrokenCaseMenu menu = new BrokenCaseMenu(content);
		menu.setName(MsgUtil.getMessage("menu.item.repair"));

		controller.loadLeftMenu(menu);
		controller.loadContent(content, PageLevel.Level1);
	}

	@FXML
	private void setting() {
		Dialog<Boolean> dialog = new Dialog<>();
		dialog.setDialogPane(new SettingDialog());
		dialog.setTitle(MsgUtil.getMessage("main.menu.setting"));
		dialog.setPrefSize(400, 650);
		dialog.show();
	}

	public void refresh() {
		if (decoration != null) {
			decoration.maximize();
		}
	}
}

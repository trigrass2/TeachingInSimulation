package com.cas.sim.tis.consts;

import com.cas.sim.tis.Application;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.PageView;
import com.cas.sim.tis.view.control.imp.library.LibraryMenu;
import com.cas.sim.tis.view.control.imp.resource.ResourceMenu;
import com.cas.sim.tis.view.controller.PageController;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public enum MenuEnum implements EventHandler<MouseEvent> {
	Resource("main.menu.resource", "static/images/menu/resource.png") {
		@Override
		public void handle(MouseEvent event) {
			// 跳转到资源库页面
			Application.showView(PageView.class);
			PageController controller = SpringUtil.getBean(PageController.class);
			ResourceMenu menu = new ResourceMenu();
			controller.loadLeftMenu(menu);
			controller.loadContent(null, null);
		}
	},
	Questions("main.menu.library", "static/images/menu/library.png") {
		@Override
		public void handle(MouseEvent event) {
			Application.showView(PageView.class);
			PageController controller = SpringUtil.getBean(PageController.class);
			LibraryMenu menu = new LibraryMenu();
			controller.loadLeftMenu(menu);
			controller.loadContent(null, null);
		}
	},
	Preparation("main.menu.preparation", "static/images/menu/preparation.png") {
		@Override
		public void handle(MouseEvent event) {

		}
	};

	private String msgKey;
	private String imagePath;

	private MenuEnum(String msgKey, String imagePath) {
		this.msgKey = msgKey;
		this.imagePath = imagePath;
	}

	public String getMsgKey() {
		return msgKey;
	}

	public String getImagePath() {
		return imagePath;
	}

}

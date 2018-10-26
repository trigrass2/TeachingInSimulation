package com.cas.sim.tis.consts;

import java.util.List;

import com.cas.sim.tis.Application;
import com.cas.sim.tis.action.CatalogAction;
import com.cas.sim.tis.entity.Catalog;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.PageView;
import com.cas.sim.tis.view.control.imp.dialog.Dialog;
import com.cas.sim.tis.view.control.imp.info.InformationMenu;
import com.cas.sim.tis.view.control.imp.library.LibraryMenu;
import com.cas.sim.tis.view.control.imp.preparation.CatalogSelectDialog;
import com.cas.sim.tis.view.control.imp.preparation.PreparationMenu;
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
			List<Catalog> catalogs = SpringUtil.getBean(CatalogAction.class).findCatalogsByParentId(0);

			Dialog<Catalog> dialog = new Dialog<>();
			dialog.setDialogPane(new CatalogSelectDialog(catalogs));
			dialog.setTitle(MsgUtil.getMessage("preparation.dialog.select"));
			dialog.setPrefSize(652, 420);
			dialog.showAndWait().ifPresent(subject -> {
				if (subject == null) {
					return;
				}
				Application.showView(PageView.class);
				PageController controller = SpringUtil.getBean(PageController.class);
				PreparationMenu menu = new PreparationMenu(subject);
				controller.loadLeftMenu(menu);
				controller.loadContent(null, null);
			});
		}
	},
	Lessones("main.menu.lessones", "static/images/menu/preparation.png") {
		@Override
		public void handle(MouseEvent event) {
			List<Catalog> catalogs = SpringUtil.getBean(CatalogAction.class).findCatalogsByParentId(0);

			Dialog<Catalog> dialog = new Dialog<>();
			dialog.setDialogPane(new CatalogSelectDialog(catalogs));
			dialog.setTitle(MsgUtil.getMessage("preparation.dialog.select"));
			dialog.setPrefSize(652, 420);
			dialog.showAndWait().ifPresent(subject -> {
				if (subject == null) {
					return;
				}
				Application.showView(PageView.class);
				PageController controller = SpringUtil.getBean(PageController.class);
				PreparationMenu menu = new PreparationMenu(subject);
				controller.loadLeftMenu(menu);
				controller.loadContent(null, null);
			});
		}
	},
	Information("main.menu.info", "static/images/menu/class.png") {

		@Override
		public void handle(MouseEvent event) {
			Application.showView(PageView.class);
			PageController controller = SpringUtil.getBean(PageController.class);
			InformationMenu menu = new InformationMenu();
			controller.loadLeftMenu(menu);
			controller.loadContent(null, null);
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

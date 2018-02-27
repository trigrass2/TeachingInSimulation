package com.cas.sim.tis.view.control.imp.library;

import com.cas.sim.tis.consts.RoleConst;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.control.IDistory;
import com.cas.sim.tis.view.control.IPublish;
import com.cas.sim.tis.view.control.imp.LeftMenu;
import com.cas.sim.tis.view.control.imp.library.LibraryList.LibraryMenuType;
import com.cas.sim.tis.view.control.imp.question.ExamingMenuItem;
import com.cas.sim.tis.view.controller.PageController;
import com.cas.sim.tis.view.controller.PageController.PageLevel;

public class LibraryMenu extends LeftMenu implements IPublish, IDistory {

	private ExamingMenuItem item;

	@Override
	protected void initMenu() {
		int role = Session.get(Session.KEY_LOGIN_ROLE);
		if (RoleConst.ADMIN == role) {
			addMenuItem(MsgUtil.getMessage("library.menu.mock"), "iconfont.svg.exam", e -> {
				PageController controller = SpringUtil.getBean(PageController.class);
				controller.loadContent(new LibraryList(LibraryMenuType.ADMIN_MOCK), PageLevel.Level1);
			});
			addMenuItem(MsgUtil.getMessage("library.menu.old"), "iconfont.svg.exam", e -> {
				PageController controller = SpringUtil.getBean(PageController.class);
				controller.loadContent(new LibraryList(LibraryMenuType.ADMIN_OLD), PageLevel.Level1);
			});
		} else if (RoleConst.TEACHER == role) {
			addMenuItem(MsgUtil.getMessage("library.menu.mock"), "iconfont.svg.exam", e -> {
				PageController controller = SpringUtil.getBean(PageController.class);
				controller.loadContent(new LibraryList(LibraryMenuType.TEACHER_MOCK), PageLevel.Level1);
			});
			addMenuItem(MsgUtil.getMessage("library.menu.old"), "iconfont.svg.exam", e -> {
				PageController controller = SpringUtil.getBean(PageController.class);
				controller.loadContent(new LibraryList(LibraryMenuType.TEACHER_OLD), PageLevel.Level1);
			});
			addMenuItem(MsgUtil.getMessage("library.menu.teacher"), "iconfont.svg.more", e -> {
				PageController controller = SpringUtil.getBean(PageController.class);
				controller.loadContent(new LibraryList(LibraryMenuType.TEACHER_MINE), PageLevel.Level1);
			});
			addMenuItem(MsgUtil.getMessage("library.menu.exam"), "iconfont.svg.record", e -> {
				PageController controller = SpringUtil.getBean(PageController.class);
			});
			Integer publishId = Session.get(Session.KEY_LIBRARY_PUBLISH_ID);
			if (publishId != null) {
				publish(publishId);
			}
		} else if (RoleConst.STUDENT == role) {
			addMenuItem(MsgUtil.getMessage("library.menu.mock"), "iconfont.svg.exam", e -> {
				PageController controller = SpringUtil.getBean(PageController.class);
				controller.loadContent(new LibraryList(LibraryMenuType.STUDENT_MOCK), PageLevel.Level1);
			});
			addMenuItem(MsgUtil.getMessage("library.menu.old"), "iconfont.svg.exam", e -> {
				PageController controller = SpringUtil.getBean(PageController.class);
				controller.loadContent(new LibraryList(LibraryMenuType.STUDENT_OLD), PageLevel.Level1);
			});
			addMenuItem(MsgUtil.getMessage("library.menu.teacher"), "iconfont.svg.more", e -> {
				PageController controller = SpringUtil.getBean(PageController.class);
				controller.loadContent(new LibraryList(LibraryMenuType.STUDENT_MINE), PageLevel.Level1);
			});
			addMenuItem(MsgUtil.getMessage("library.menu.exam"), "iconfont.svg.record", e -> {

			});
			addMenuItem(MsgUtil.getMessage("library.menu.train"), "iconfont.svg.record", e -> {

			});
		}
	}

	@Override
	public void publish(int id) {
		if (!menu.getChildren().contains(item)) {
			item = new ExamingMenuItem();
			menu.getChildren().add(item);
			menu.layout();
		}
		if (item != null) {
			item.load(id);
		}
	}

	@Override
	public void distroy() {
		if (item != null) {
			item.distroy();
		}
	}
}

package com.cas.sim.tis.view.control.imp.library;

import com.cas.sim.tis.consts.RoleConst;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.control.imp.LeftMenu;
import com.cas.sim.tis.view.controller.PageController;
import com.cas.sim.tis.view.controller.PageController.PageLevel;

public class LibraryMenu extends LeftMenu {

	@Override
	protected void initMenu() {
		int role = Session.get(Session.KEY_LOGIN_ROLE);
		if (RoleConst.ADMIN == role) {
			addMenuItem(MsgUtil.getMessage("library.menu.mock"), "static/images/left/exam.png", e -> {
				PageController controller = SpringUtil.getBean(PageController.class);
				controller.loadContent(new LibraryList(LibraryList.LibraryMenuType.ADMIN_MOCK), PageLevel.Level1);
			});
			addMenuItem(MsgUtil.getMessage("library.menu.old"), "static/images/left/exam.png", e -> {

			});
		} else if (RoleConst.TEACHER == role) {
			addMenuItem(MsgUtil.getMessage("library.menu.mock"), "static/images/left/exam.png", e -> {

			});
			addMenuItem(MsgUtil.getMessage("library.menu.old"), "static/images/left/exam.png", e -> {

			});
			addMenuItem(MsgUtil.getMessage("library.menu.mine"), "static/images/left/libs.png", e -> {

			});
		} else if (RoleConst.STUDENT == role) {
			addMenuItem(MsgUtil.getMessage("library.menu.mock"), "static/images/left/exam.png", e -> {

			});
			addMenuItem(MsgUtil.getMessage("library.menu.old"), "static/images/left/exam.png", e -> {

			});
			addMenuItem(MsgUtil.getMessage("library.menu.mine"), "static/images/left/libs.png", e -> {

			});
			addMenuItem(MsgUtil.getMessage("library.menu.exam"), "static/images/left/record.png", e -> {

			});
			addMenuItem(MsgUtil.getMessage("library.menu.train"), "static/images/left/record.png", e -> {

			});
		}
	}

}

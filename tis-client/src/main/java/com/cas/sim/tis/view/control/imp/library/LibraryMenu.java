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
			addMenuItem(MsgUtil.getMessage("library.menu.mock"), "iconfont.svg.exam", e -> {
				PageController controller = SpringUtil.getBean(PageController.class);
				controller.loadContent(new LibraryList(LibraryList.LibraryMenuType.ADMIN_MOCK), PageLevel.Level1);
			});
			addMenuItem(MsgUtil.getMessage("library.menu.old"), "iconfont.svg.exam", e -> {

			});
		} else if (RoleConst.TEACHER == role) {
			addMenuItem(MsgUtil.getMessage("library.menu.mock"), "iconfont.svg.exam", e -> {

			});
			addMenuItem(MsgUtil.getMessage("library.menu.old"), "iconfont.svg.exam", e -> {

			});
			addMenuItem(MsgUtil.getMessage("library.menu.mine"), "iconfont.svg.more", e -> {

			});
		} else if (RoleConst.STUDENT == role) {
			addMenuItem(MsgUtil.getMessage("library.menu.mock"), "iconfont.svg.exam", e -> {

			});
			addMenuItem(MsgUtil.getMessage("library.menu.old"), "iconfont.svg.exam", e -> {

			});
			addMenuItem(MsgUtil.getMessage("library.menu.mine"), "iconfont.svg.more", e -> {

			});
			addMenuItem(MsgUtil.getMessage("library.menu.exam"), "iconfont.svg.record", e -> {

			});
			addMenuItem(MsgUtil.getMessage("library.menu.train"), "iconfont.svg.record", e -> {

			});
		}
	}

}

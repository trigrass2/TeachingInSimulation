package com.cas.sim.tis.view.control.imp.resource;

import com.cas.sim.tis.consts.RoleConst;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.action.UserAction;
import com.cas.sim.tis.view.control.imp.LeftMenu;
import com.cas.sim.tis.view.control.imp.resource.ResourceList.ResourceMenuType;
import com.cas.sim.tis.view.controller.PageController;
import com.cas.sim.tis.view.controller.PageController.PageLevel;

public class ResourceMenu extends LeftMenu {

	@Override
	protected void initMenu() {
		int role = Session.get(Session.KEY_LOGIN_ROLE);
		int userId = Session.get(Session.KEY_LOGIN_ID);
		if (RoleConst.ADMIN == role) {
			addMenuItem(MsgUtil.getMessage("resource.menu.sys"), "static/images/left/resource.png", e -> {
				PageController controller = SpringUtil.getBean(PageController.class);
				controller.loadContent(new ResourceList(ResourceMenuType.EDITABLE, 1), PageLevel.Level1);
			});
		} else if (RoleConst.TEACHER == role) {
			addMenuItem(MsgUtil.getMessage("resource.menu.sys"), "static/images/left/resource.png", e -> {
				PageController controller = SpringUtil.getBean(PageController.class);
				controller.loadContent(new ResourceList(ResourceMenuType.READONLY, 1), PageLevel.Level1);
			});
			addMenuItem(MsgUtil.getMessage("resource.menu.mine"), "static/images/left/resource.png", e -> {
				PageController controller = SpringUtil.getBean(PageController.class);
				controller.loadContent(new ResourceList(ResourceMenuType.EDITABLE, userId), PageLevel.Level1);
			});
		} else if (RoleConst.STUDENT == role) {
			addMenuItem(MsgUtil.getMessage("resource.menu.mine"), "static/images/left/resource.png", e -> {
				// 获得教师编号
				Integer tearcherId = SpringUtil.getBean(UserAction.class).getTeacherIdByStudentId(Session.get(Session.KEY_LOGIN_ID));
				PageController controller = SpringUtil.getBean(PageController.class);
				controller.loadContent(new ResourceList(ResourceMenuType.READONLY, 1, tearcherId), PageLevel.Level1);
			});
		}
		addMenuItem(MsgUtil.getMessage("resource.menu.browse"), "static/images/left/history.png", e -> {
			PageController controller = SpringUtil.getBean(PageController.class);
			controller.loadContent(new ResourceList(ResourceMenuType.BROWSE, 1), PageLevel.Level1);
		});
		addMenuItem(MsgUtil.getMessage("resource.menu.collection"), "static/images/left/favorite.png", e -> {
			PageController controller = SpringUtil.getBean(PageController.class);
			controller.loadContent(new ResourceList(ResourceMenuType.COLLECTION, 1), PageLevel.Level1);
		});
	}
}

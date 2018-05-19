package com.cas.sim.tis.view.control.imp.resource;

import com.cas.sim.tis.consts.RoleConst;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.User;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.util.SpringUtil;
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
			addMenuItem(MsgUtil.getMessage("resource.menu.sys"), "iconfont.svg.resource", e -> {
				PageController controller = SpringUtil.getBean(PageController.class);
				controller.loadContent(new ResourceList(ResourceMenuType.ADMIN_SYS, 1), PageLevel.Level1);
			});
		} else if (RoleConst.TEACHER == role) {
			addMenuItem(MsgUtil.getMessage("resource.menu.sys"), "iconfont.svg.resource", e -> {
				PageController controller = SpringUtil.getBean(PageController.class);
				controller.loadContent(new ResourceList(ResourceMenuType.TEACHER_SYS, 1), PageLevel.Level1);
			});
			addMenuItem(MsgUtil.getMessage("resource.menu.mine"), "iconfont.svg.resource", e -> {
				PageController controller = SpringUtil.getBean(PageController.class);
				controller.loadContent(new ResourceList(ResourceMenuType.TEACHER_MINE, userId), PageLevel.Level1);
			});
		} else if (RoleConst.STUDENT == role) {
			addMenuItem(MsgUtil.getMessage("resource.menu.mine"), "iconfont.svg.resource", e -> {
				PageController controller = SpringUtil.getBean(PageController.class);
				controller.loadContent(new ResourceList(ResourceMenuType.STUDENT_SYS, 1), PageLevel.Level1);
			});
			addMenuItem(MsgUtil.getMessage("resource.menu.teacher"), "iconfont.svg.resource", e -> {
				User user = Session.get(Session.KEY_OBJECT);
				// 获得教师编号
				Integer tearcherId = user.getTeacherId();
				PageController controller = SpringUtil.getBean(PageController.class);
				controller.loadContent(new ResourceList(ResourceMenuType.STUDENT_TECH, tearcherId), PageLevel.Level1);
			});
		}
		addMenuItem(MsgUtil.getMessage("resource.menu.browse"), "iconfont.svg.history", e -> {
			PageController controller = SpringUtil.getBean(PageController.class);
			controller.loadContent(new ResourceList(ResourceMenuType.BROWSE, userId), PageLevel.Level1);
		});
		addMenuItem(MsgUtil.getMessage("resource.menu.collection"), "iconfont.svg.favourite", e -> {
			PageController controller = SpringUtil.getBean(PageController.class);
			controller.loadContent(new ResourceList(ResourceMenuType.COLLECTION, userId), PageLevel.Level1);
		});
	}
}

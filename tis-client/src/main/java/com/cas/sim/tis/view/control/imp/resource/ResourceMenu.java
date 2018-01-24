package com.cas.sim.tis.view.control.imp.resource;

import com.cas.sim.tis.consts.RoleConst;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.control.imp.LeftMenu;
import com.cas.sim.tis.view.controller.PageController;

public class ResourceMenu extends LeftMenu {

	@Override
	protected void initMenu() {
		int role = Session.get(Session.KEY_LOGIN_ROLE);
		int userId = Session.get(Session.KEY_LOGIN_ID);
		if (RoleConst.ADMIN == role) {
			addMenuItem("系统资源", "static/images/left/resource.png", e -> {
				PageController controller = SpringUtil.getBean(PageController.class);
				controller.loadContent(new ResourceList(true, 1));
			});
		} else if (RoleConst.TEACHER == role) {
			addMenuItem("系统资源", "static/images/left/resource.png", e -> {
				PageController controller = SpringUtil.getBean(PageController.class);
				controller.loadContent(new ResourceList(false, 1));
			});
			addMenuItem("我的资源", "static/images/left/resource.png", e -> {
				PageController controller = SpringUtil.getBean(PageController.class);
				controller.loadContent(new ResourceList(true, userId));
			});
		} else if (RoleConst.STUDENT == role) {
			addMenuItem("系统资源", "static/images/left/resource.png", e -> {
				// 获得教师编号
				PageController controller = SpringUtil.getBean(PageController.class);
				controller.loadContent(new ResourceList(false, 1));
			});
		}
		addMenuItem("浏览记录", "static/images/left/history.png", e -> {

		});
		addMenuItem("我的收藏", "static/images/left/favorite.png", e -> {

		});
	}
}

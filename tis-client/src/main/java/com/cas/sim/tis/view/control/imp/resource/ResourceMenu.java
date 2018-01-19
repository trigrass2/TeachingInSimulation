package com.cas.sim.tis.view.control.imp.resource;

import com.cas.sim.tis.consts.RoleConst;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.control.imp.LeftMenu;
import com.cas.sim.tis.view.controller.LoginController;
import com.cas.sim.tis.view.controller.PageController;

public class ResourceMenu extends LeftMenu{

	@Override
	protected void initMenu() {
		if (RoleConst.ADMIN == LoginController.USER_ROLE) {
			addMenuItem("系统资源", "static/images/left/resource.png", e->{
				PageController controller = SpringUtil.getBean(PageController.class);
				controller.loadContent(new AdminResourceList(true));
			});
		} else if (RoleConst.TEACHER == LoginController.USER_ROLE) {
			addMenuItem("系统资源", "static/images/left/resource.png", e->{
				PageController controller = SpringUtil.getBean(PageController.class);
				controller.loadContent(new AdminResourceList(false));
			});
			addMenuItem("我的资源", "static/images/left/resource.png", e->{
				PageController controller = SpringUtil.getBean(PageController.class);
				controller.loadContent(new TeacherResourceList());
			});
		} else if (RoleConst.STUDENT == LoginController.USER_ROLE) {
			addMenuItem("系统资源", "static/images/left/resource.png", e->{
				PageController controller = SpringUtil.getBean(PageController.class);
				controller.loadContent(new StudentResourceList());
			});
		}
		addMenuItem("浏览记录", "static/images/left/history.png", e->{
			
		});
		addMenuItem("我的收藏", "static/images/left/favorite.png", e->{
			
		});
	}
}

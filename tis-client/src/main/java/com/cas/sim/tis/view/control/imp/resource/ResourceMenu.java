package com.cas.sim.tis.view.control.imp.resource;

import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.control.imp.LeftMenu;
import com.cas.sim.tis.view.controller.PageController;

public class ResourceMenu extends LeftMenu{

	@Override
	protected void initMenu() {
		addMenuItem("系统资源", "static/images/left/resource.png", e->{
			PageController controller = SpringUtil.getBean(PageController.class);
			controller.loadContent(new ResourceList());
		});
		addMenuItem("浏览记录", "static/images/left/history.png", e->{
			
		});
		addMenuItem("我的收藏", "static/images/left/favorite.png", e->{
			
		});
	}

}

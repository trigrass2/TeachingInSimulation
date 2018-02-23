package com.cas.sim.tis.view.control.imp.info;

import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.control.imp.LeftMenu;
import com.cas.sim.tis.view.controller.PageController;
import com.cas.sim.tis.view.controller.PageController.PageLevel;
/**
 * 管理员菜单
 * @功能 InformationMenu.java
 * @作者 Caowj
 * @创建日期 2018年2月22日
 * @修改人 Caowj
 */
public class InformationMenu extends LeftMenu  {

	@Override
	protected void initMenu() {
		addMenuItem(MsgUtil.getMessage("information.menu.teacher"), "iconfont.svg.teacher", e -> {
			PageController controller = SpringUtil.getBean(PageController.class);
			controller.loadContent(new TeacherList(), PageLevel.Level1);
		});
		addMenuItem(MsgUtil.getMessage("information.menu.class"), "iconfont.svg.class", e -> {
			PageController controller = SpringUtil.getBean(PageController.class);
			controller.loadContent(new ClassList(), PageLevel.Level1);
		});
	}

}

package com.cas.sim.tis.view;

import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.controller.PageController;

import de.felixroske.jfxsupport.AbstractFxmlView;
import de.felixroske.jfxsupport.FXMLView;
import javafx.scene.Parent;

@FXMLView(value="/view/Page.fxml",bundle = "i18n.messages")
public class PageView extends AbstractFxmlView {
	@Override
	public Parent getView() {
		SpringUtil.getBean(PageController.class).refresh();
		return super.getView();
	}
}

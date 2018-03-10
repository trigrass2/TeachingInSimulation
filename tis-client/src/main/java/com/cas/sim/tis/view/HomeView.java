package com.cas.sim.tis.view;

import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.controller.HomeController;

import de.felixroske.jfxsupport.AbstractFxmlView;
import de.felixroske.jfxsupport.FXMLView;
import javafx.scene.Parent;

@FXMLView(value = "/view/Home.fxml", bundle = "i18n.messages")
public class HomeView extends AbstractFxmlView {
	@Override
	public Parent getView() {
		SpringUtil.getBean(HomeController.class).refresh();
		return super.getView();
	}
}

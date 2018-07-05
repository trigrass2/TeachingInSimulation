package com.cas.sim.tis.view;

import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.controller.PreparationExamController;

import de.felixroske.jfxsupport.AbstractFxmlView;
import de.felixroske.jfxsupport.FXMLView;
import javafx.scene.Parent;

@FXMLView(value="/view/preparation/Exam.fxml",bundle = "i18n.messages")
public class PreparationExamView extends AbstractFxmlView {
	@Override
	public Parent getView() {
		SpringUtil.getBean(PreparationExamController.class).refresh();
		return super.getView();
	}
}

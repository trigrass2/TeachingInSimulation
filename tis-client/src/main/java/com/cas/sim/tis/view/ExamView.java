package com.cas.sim.tis.view;

import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.controller.ExamController;

import de.felixroske.jfxsupport.AbstractFxmlView;
import de.felixroske.jfxsupport.FXMLView;
import javafx.scene.Parent;

@FXMLView(value="/view/Exam.fxml",bundle = "i18n.messages")
public class ExamView extends AbstractFxmlView {
	@Override
	public Parent getView() {
		SpringUtil.getBean(ExamController.class).refresh();
		return super.getView();
	}
}

package com.cas.sim.tis.view.control.imp.jme;

import com.cas.sim.tis.Application;
import com.cas.sim.tis.view.HomeView;
import com.cas.sim.tis.view.control.imp.Decoration;
import com.cas.sim.tis.view.control.imp.broken.BrokenCase3D;

import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

@FXMLController
public class BrokenExamController {
	@FXML
	private Decoration decoration;
	@FXML
	private Button back;
	@FXML
	private StackPane content;
	@FXML
	private VBox corrects;
	@FXML
	private Label number;
	@FXML
	private Label chance;

	public void loadContent(BrokenCase3D case3d) {
		this.content.getChildren().addAll(case3d.getContent());
	}

	@FXML
	private void back() {
		// 考试结束返回首页
		AbstractJavaFxApplicationSupport.showView(HomeView.class);
	}

	public void refresh() {
		if (decoration != null) {
			decoration.maximize();
		}
	}
}

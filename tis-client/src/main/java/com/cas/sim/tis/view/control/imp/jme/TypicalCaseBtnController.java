package com.cas.sim.tis.view.control.imp.jme;

import java.net.URL;
import java.util.ResourceBundle;

import com.cas.sim.tis.app.state.TypicalCaseState;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;

public class TypicalCaseBtnController implements Initializable {
	@FXML
	private CheckBox intro;
	@FXML
	private CheckBox explode;
	@FXML
	private CheckBox transparent;
	@FXML
	private CheckBox showName;
	@FXML
	private CheckBox autoRoate;
	@FXML
	private Control center;
	@FXML
	private Control move;
	@FXML
	private Control rotate;
	@FXML
	private Control zoomIn;
	@FXML
	private Control zoomOut;

	private TypicalCaseState state;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

	public void setState(TypicalCaseState state) {
		this.state = state;
	}

}

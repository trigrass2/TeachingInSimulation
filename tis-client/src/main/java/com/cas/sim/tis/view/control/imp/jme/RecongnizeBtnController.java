package com.cas.sim.tis.view.control.imp.jme;

import java.net.URL;
import java.util.ResourceBundle;

import com.cas.sim.tis.app.state.ElecCompState;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;

public class RecongnizeBtnController implements Initializable {
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
	private Label center;
	@FXML
	private Label move;
	@FXML
	private Label rotate;
	@FXML
	private Label zoomIn;
	@FXML
	private Label zoomOut;

	private ElecCompState compState;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		intro.selectedProperty().addListener((s, o, n) -> {
		});
		explode.selectedProperty().addListener((s, o, n) -> {
			compState.explode(n);
		});
		transparent.selectedProperty().addListener((s, o, n) -> {
			compState.transparent(n);
		});
		showName.selectedProperty().addListener((s, o, n) -> {
			compState.setNameVisible(n);
		});
		autoRoate.selectedProperty().addListener((s, o, n) -> {
			compState.autoRotate(n);
		});
		center.setOnMousePressed(e -> {
			compState.center();
		});
		rotate.setOnMousePressed(e -> {
			compState.rotate();
		});
		move.setOnMousePressed(e -> {
			compState.move();
		});
		zoomIn.setOnMousePressed(e -> {
			compState.zoomIn();
		});
		zoomOut.setOnMousePressed(e -> {
			compState.zoomOut();
		});
	}

	public void setState(ElecCompState compState) {
		this.compState = compState;
	}

}

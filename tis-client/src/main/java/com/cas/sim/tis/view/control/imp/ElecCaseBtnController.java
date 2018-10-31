package com.cas.sim.tis.view.control.imp;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.controlsfx.control.PopOver;
import org.controlsfx.control.PopOver.ArrowLocation;

import com.cas.sim.tis.app.state.ElecCaseState;
import com.cas.sim.tis.app.state.ElecCaseState.CaseMode;
import com.cas.sim.tis.app.state.typical.CircuitState;
import com.cas.sim.tis.consts.Radius;
import com.cas.sim.tis.consts.WireColor;
import com.cas.sim.tis.util.AlertUtil;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.view.control.IDistory;
import com.cas.sim.tis.view.control.imp.jme.TypicalCaseBtnController;
import com.cas.sim.tis.view.control.imp.jme.WireRadius;
import com.cas.sim.tis.view.controller.DrawingController;

import de.felixroske.jfxsupport.GUIState;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public abstract class ElecCaseBtnController implements Initializable, IDistory {
	@FXML
	protected VBox pane;
	@FXML
	protected Title title;
	@FXML
	protected StackPane content;
	@FXML
	protected ChoiceBox<CaseMode> modes;
	@FXML
	protected CheckBox showName;
	@FXML
	protected CheckBox multimeter;
	@FXML
	protected CheckBox transparent;
	@FXML
	protected HBox view;
	@FXML
	protected HBox typical;
	@FXML
	protected HBox free;
	@FXML
	protected HBox broken;
	@FXML
	protected ToggleButton autoComps;
	@FXML
	protected ToggleButton autoWires;
	@FXML
	protected VBox steps;
	@FXML
	protected ScrollPane scroll;
	@FXML
	protected HBox btns;
	@FXML
	protected Button prev;
	@FXML
	protected Button next;
	@FXML
	protected Button submit;

	protected ElecCase3D<?> elecCase3D;
	protected ElecCaseState<?> elecCaseState;

	private PopOver wirePicker;

	private Label preview;

	private Stage drawingWin;
	private DrawingController controller;

	private Label sectionPicked;
	private Label colorPicked;

	private boolean switchModeFailed;

	private CaseMode[] enableModes;

	public ElecCaseBtnController(CaseMode... enableModes) {
		this.enableModes = enableModes;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		modes.getItems().addAll(enableModes);
		modes.getSelectionModel().selectedItemProperty().addListener((b, o, n) -> {
			if (n == null || o == n || elecCase3D == null) {
				return;
			}
			if (o == null) {
				switchCaseMode(n);
			} else if (switchModeFailed) {
				switchModeFailed = false;
			} else if (CaseMode.EDIT_MODE == o) {
				AlertUtil.showConfirm(MsgUtil.getMessage("elec.case.not.be.clean"), resp -> {
					if (ButtonType.YES == resp) {
						switchCaseMode(n);
					} else {
						switchModeFailed = true;
						modes.getSelectionModel().select(o);
					}
				});
			} else {
				switchCaseMode(n);
			}
		});
		modes.getSelectionModel().selectFirst();
	}

	protected abstract void switchCaseMode(CaseMode mode);

	public abstract String getDrawings();

	public abstract void setDrawings(String drawings);

	@FXML
	private void toggleTagName() {
		boolean visible = showName.isSelected();
		if (elecCaseState != null && elecCaseState.getCircuitState() != null) {
			elecCaseState.getCircuitState().setTagNameVisible(visible);
		}
	}

	@FXML
	private void toggleMultiMeter() {
		boolean visible = multimeter.isSelected();
		elecCase3D.setMultimeterVisible(visible);
	}

	@FXML
	private void toggleDimension(MouseEvent event) {
		ToggleButton source = (ToggleButton) event.getSource();
		if (!source.isSelected()) {
			source.setSelected(true);
			return;
		}

		if ("2D".equals(source.getText())) {
			elecCase3D.switchTo2D();
		} else if ("3D".equals(source.getText())) {
			elecCase3D.switchTo3D();
		}
	}

	@FXML
	private void toggleTransparent() {
		if (elecCaseState != null && elecCaseState.getCircuitState() != null) {
			elecCaseState.getCircuitState().setElecCompTransparent(transparent.isSelected());
		}
	}

	@FXML
	private void showDrawingWin(ActionEvent event) {
		try {
			if (drawingWin == null) {
				drawingWin = new Stage();
				drawingWin.initStyle(StageStyle.TRANSPARENT);
				drawingWin.setX(GUIState.getStage().getX() + 100);
				drawingWin.setY(GUIState.getStage().getY() + 100);

				FXMLLoader loader = new FXMLLoader();
				loader.setResources(ResourceBundle.getBundle("i18n/messages"));
				Region root = loader.load(TypicalCaseBtnController.class.getResourceAsStream("/view/jme/Drawing.fxml"));
				controller = loader.getController();
				controller.setStage(drawingWin);
				controller.setElecCaseBtnController(this);

				Scene scene = new Scene(root);
				drawingWin.setScene(scene);
				drawingWin.setOnHidden(e -> {
					((Button) event.getSource()).setDisable(false);
				});
				drawingWin.setOnShown(e -> {
					((Button) event.getSource()).setDisable(true);
				});
			}
			controller.setEditable(CaseMode.EDIT_MODE == modes.getSelectionModel().getSelectedItem());
			drawingWin.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Node getWirePickContent() {
		VBox node = new VBox(10);
		node.setPadding(new Insets(10, 10, 10, 10));

		HBox hbox = new HBox(15);

		VBox number = new VBox(10);
		initNumberPane(number);

		VBox color = new VBox(10);
		initWireColorPane(color);

		VBox section = new VBox(10);
		initSectionPane(section);

		hbox.getChildren().addAll(color, section, number);

		node.getChildren().add(hbox);

		return node;
	}

	private void initNumberPane(VBox number) {
		number.setAlignment(Pos.TOP_CENTER);

		number.getChildren().add(new Label(MsgUtil.getMessage("elec.case.wires.preview")));

		number.getChildren().add(preview = new Label("--", new WireRadius()));
		preview.setPrefSize(80, 80);
		preview.setAlignment(Pos.CENTER);
		preview.setContentDisplay(ContentDisplay.TOP);
		VBox.setVgrow(preview, Priority.ALWAYS);
	}

	private void initSectionPane(VBox section) {
		section.setAlignment(Pos.TOP_CENTER);

		section.getChildren().add(new Label(MsgUtil.getMessage("elec.case.wires.radius")));
		List<Label> list = new ArrayList<>();

		for (Radius radius : Radius.values()) {
			Label label = new Label(radius.getRadius(), new WireRadius(radius.getInnerRadius(), Color.rgb(186, 100, 64), radius.getOutterRadius(), Color.TRANSPARENT));
			label.setUserData(radius);
			list.add(label); //
		}

		onSectionSelected(list.get(0));

		list.forEach(l -> {
			l.getStyleClass().add("picker-item");
			l.setOnMouseClicked(e -> onSectionSelected(l));
		});
		section.getChildren().addAll(list);
	}

	private void onSectionSelected(Label l) {
		if (sectionPicked != null) {
			sectionPicked.getStyleClass().remove("picker-item-selected");
		}
		sectionPicked = l;
		sectionPicked.getStyleClass().add("picker-item-selected");

		preview.setText(sectionPicked.getText());

		WireRadius cir = (WireRadius) sectionPicked.getGraphic();

		WireRadius wr = (WireRadius) preview.getGraphic();

		wr.setInnerFill(cir.getInnerFill());
		wr.setInnerRadius(cir.getInnerRadius());
		wr.setOuterRadius(cir.getOuterRadius());

		CircuitState.setWidth((Radius) l.getUserData());
	}

	/**
	 * @param color
	 */
	private void initWireColorPane(VBox color) {
		color.setAlignment(Pos.TOP_CENTER);

		color.getChildren().add(new Label(MsgUtil.getMessage("elec.case.wires.color")));

		List<Label> list = new ArrayList<>();
		for (WireColor wireColor : WireColor.values()) {
			Label l = new Label(MsgUtil.getMessage(wireColor.getTextKey()), new Circle(10, wireColor.getColor()));
			l.setUserData(wireColor);
			list.add(l); //
		}

		onColorSelected(list.get(0));

		list.forEach(l -> {
			l.getStyleClass().add("picker-item");
			l.setOnMouseClicked(e -> onColorSelected(l));
		});

		color.getChildren().addAll(list);
	}

	private void onColorSelected(Label l) {
		if (colorPicked != null) {
			colorPicked.getStyleClass().remove("picker-item-selected");
		}
		colorPicked = l;
		colorPicked.getStyleClass().add("picker-item-selected");

		Circle cir = (Circle) colorPicked.getGraphic();
		WireRadius wr = (WireRadius) preview.getGraphic();

		Color color = (Color) cir.getFill();
		wr.setOuterFill(color);
		CircuitState.setColor((WireColor) l.getUserData());
	}

	@FXML
	private void showWirePicker(ActionEvent event) {
		Button wire = (Button) event.getSource();
		if (wirePicker == null) {
			wirePicker = new PopOver();
			wirePicker.setArrowLocation(ArrowLocation.TOP_CENTER);
			wirePicker.setPrefSize(300, 400);
			wirePicker.setTitle(MsgUtil.getMessage("elec.case.wires.pane"));
			wirePicker.setContentNode(getWirePickContent());
		}
		Point2D point = wire.localToScreen(wire.getWidth() / 2, 50);
		wirePicker.show(wire, point.getX(), point.getY());
	}

	public void setTitle(String title) {
		this.title.setTitle(title);
		this.title.setVisible(true);
	}

	public void setState(ElecCaseState<?> elecCaseState) {
		this.elecCaseState = elecCaseState;
	}

	@Override
	public void distroy() {
		clean();
	}

	public void clean() {
		showName.setSelected(false);
		transparent.setSelected(false);

		if (drawingWin != null) {
			drawingWin.close();
		}

		if (wirePicker != null) {
			wirePicker.setContentNode(getWirePickContent());
			wirePicker.hide();
		}

		modes.getSelectionModel().select(null);
		autoComps.setSelected(false);
		autoWires.setSelected(false);
		pane.setVisible(true);
	}

	public void setMode(CaseMode mode) {
		modes.getSelectionModel().select(mode);
	}

	public void setElecCase3D(ElecCase3D<?> elecCase3D) {
		this.elecCase3D = elecCase3D;
	}
}

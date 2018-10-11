package com.cas.sim.tis.view.control.imp.jme;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.controlsfx.control.PopOver;
import org.controlsfx.control.PopOver.ArrowLocation;

import com.cas.sim.tis.app.state.typical.CircuitState;
import com.cas.sim.tis.app.state.typical.TypicalCaseState;
import com.cas.sim.tis.app.state.typical.TypicalCaseState.CaseMode;
import com.cas.sim.tis.consts.Radius;
import com.cas.sim.tis.consts.RoleConst;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.consts.WireColor;
import com.cas.sim.tis.flow.Step;
import com.cas.sim.tis.util.AlertUtil;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.control.IDistory;
import com.cas.sim.tis.view.control.imp.Title;
import com.cas.sim.tis.view.control.imp.typical.FlowItem;
import com.cas.sim.tis.view.control.imp.typical.StepItem;
import com.cas.sim.tis.view.controller.DrawingController;
import com.cas.sim.tis.view.controller.PageController;

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

public class TypicalCaseBtnController implements Initializable, IDistory {
	@FXML
	private VBox pane;
	@FXML
	private Title title;
	@FXML
	private StackPane content;
	@FXML
	private ChoiceBox<CaseMode> modes;
	@FXML
	private CheckBox showName;
	@FXML
	private CheckBox transparent;
	@FXML
	private HBox view;
	@FXML
	private HBox trainOrEdit;
	@FXML
	private ToggleButton autoComps;
	@FXML
	private ToggleButton autoWires;
	@FXML
	private VBox steps;
	@FXML
	private ScrollPane scroll;
	@FXML
	private FlowItem flow;
	@FXML
	private HBox btns;

	private TypicalCaseState state;

	private PopOver wirePicker;

	private Label preview;

	private Stage drawingWin;

	private TypicalCase3D typicalCase3D;
	private Label sectionPicked;
	private Label colorPicked;

//	private boolean layout;
//	private boolean routing;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		modes.getItems().add(CaseMode.VIEW_MODE);
		modes.getItems().add(CaseMode.TRAIN_MODE);

		int role = Session.get(Session.KEY_LOGIN_ROLE);
		if (RoleConst.TEACHER <= role) {
			modes.getItems().add(CaseMode.EDIT_MODE);
		}
		modes.getSelectionModel().selectedItemProperty().addListener((b, o, n) -> {
			if (o == null) {
				switchCaseMode(n);
			} else {
				AlertUtil.showConfirm(MsgUtil.getMessage("typical.case.not.be.clean"), resp -> {
					switchCaseMode(n);
				});
			}
		});
		modes.getSelectionModel().selectFirst();
		autoComps.selectedProperty().addListener((b, o, n) -> {
			if (n) {
				autoComps.setText(MsgUtil.getMessage("typical.case.clear.comp"));
			} else {
				autoComps.setText(MsgUtil.getMessage("typical.case.auto.comp"));
				autoWires.setSelected(false);
			}
			typicalCase3D.autoComps(n);
		});
		autoWires.selectedProperty().addListener((b, o, n) -> {
			if (n) {
				autoWires.setText(MsgUtil.getMessage("typical.case.clear.wire"));
				autoComps.setSelected(true);
			} else {
				autoWires.setText(MsgUtil.getMessage("typical.case.auto.wire"));
			}
			typicalCase3D.autoWires(n);
		});
	}

	private void switchCaseMode(CaseMode n) {
		SpringUtil.getBean(PageController.class).showLoading();
		if (CaseMode.VIEW_MODE == n) {
			view.toFront();
			view.setVisible(true);
			trainOrEdit.setVisible(false);
			steps.setVisible(true);
			btns.setVisible(true);
			autoComps.setSelected(false);
			autoWires.setSelected(false);
			if (typicalCase3D != null) {
				typicalCase3D.autoComps(false);
				typicalCase3D.autoWires(false);
			}
		} else if (CaseMode.TRAIN_MODE == n) {
			trainOrEdit.toFront();
			trainOrEdit.setVisible(true);
			view.setVisible(false);
			steps.setVisible(true);
			btns.setVisible(false);
			if (typicalCase3D != null) {
				typicalCase3D.autoWires(false);
				typicalCase3D.autoComps(false);
			}
		} else if (CaseMode.EDIT_MODE == n) {
			trainOrEdit.toFront();
			trainOrEdit.setVisible(true);
			view.setVisible(false);
			steps.setVisible(false);
			btns.setVisible(false);
			if (typicalCase3D != null) {
				typicalCase3D.autoComps(true);
				typicalCase3D.autoWires(true);
			}
		}
		if (state != null) {
			state.setMode(n);
		}
	}

	@FXML
	private void toggleTagName() {
		boolean visiable = showName.isSelected();
		if (state != null && state.getCircuitState() != null) {
			state.getCircuitState().setTagNameVisible(visiable);
		}
	}

	@FXML
	private void toggleDimension(MouseEvent event) {
		ToggleButton source = (ToggleButton) event.getSource();
		if (!source.isSelected()) {
			return;
		}

		if ("2D".equals(source.getText())) {
			typicalCase3D.switchTo2D();
		} else if ("3D".equals(source.getText())) {
			typicalCase3D.switchTo3D();
		}
	}

	@FXML
	private void toggleTransparent() {
		if (state != null && state.getCircuitState() != null) {
			state.getCircuitState().setElecCompTransparent(transparent.isSelected());
		}
	}

	@FXML
	private void showDrawingWin(ActionEvent event) {
		((Button) event.getSource()).setDisable(true);
		try {
			if (drawingWin == null) {
				drawingWin = new Stage();
				drawingWin.initStyle(StageStyle.TRANSPARENT);
				drawingWin.setX(GUIState.getStage().getX() + 100);
				drawingWin.setY(GUIState.getStage().getY() + 100);

				FXMLLoader loader = new FXMLLoader();
				loader.setResources(ResourceBundle.getBundle("i18n/messages"));
				Region root = loader.load(TypicalCaseBtnController.class.getResourceAsStream("/view/jme/Drawing.fxml"));
				DrawingController controller = loader.getController();
				controller.setStage(drawingWin);
				controller.setUI(typicalCase3D);

				Scene scene = new Scene(root);
				drawingWin.setScene(scene);
				drawingWin.setOnHidden(e -> {
					((Button) event.getSource()).setDisable(false);
				});
			}
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

//		number.getChildren().add(new Label(MsgUtil.getMessage("typical.case.wire.num")));

//		number.getChildren().add(num = new TextField());

		number.getChildren().add(new Label(MsgUtil.getMessage("typical.case.wire.preview")));

		number.getChildren().add(preview = new Label("--", new WireRadius()));
		preview.setPrefSize(80, 80);
		preview.setAlignment(Pos.CENTER);
		preview.setContentDisplay(ContentDisplay.TOP);
		VBox.setVgrow(preview, Priority.ALWAYS);
	}

	private void initSectionPane(VBox section) {
		section.setAlignment(Pos.TOP_CENTER);

		section.getChildren().add(new Label(MsgUtil.getMessage("typical.case.wire.radius")));
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

		color.getChildren().add(new Label(MsgUtil.getMessage("typical.case.wire.color")));

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
			wirePicker.setArrowLocation(ArrowLocation.BOTTOM_CENTER);
			wirePicker.setPrefSize(300, 400);
			wirePicker.setTitle(MsgUtil.getMessage("typical.case.wire.pane"));
			wirePicker.setContentNode(getWirePickContent());
		}
		Point2D point = wire.localToScreen(wire.getWidth() / 2, -10);
		wirePicker.show(wire, point.getX(), point.getY());
	}

	public void setTitle(String title) {
		this.title.setTitle(title);
		this.title.setVisible(true);
	}

	public void setState(TypicalCaseState state) {
		this.state = state;
	}

	@Override
	public void distroy() {
		if (drawingWin != null) {
			drawingWin.close();
		}
		if (wirePicker != null && wirePicker.isShowing()) {
			wirePicker.hide();
		}
	}

	public void setUI(TypicalCase3D typicalCase3D) {
		this.typicalCase3D = typicalCase3D;
	}

	public void clean() {
		showName.setSelected(false);
		transparent.setSelected(false);

		if (drawingWin != null) {
			drawingWin.close();
		}

		if (wirePicker != null) {
			wirePicker.setContentNode(getWirePickContent());
		}

		autoComps.setSelected(false);
		autoWires.setSelected(false);
		pane.setVisible(true);
	}

	public void setMode(CaseMode mode) {
		modes.getSelectionModel().select(mode);
	}

	public void loadSteps(List<Step> steps) {
		this.flow.getChildren().clear();

		for (int i = 0; i < steps.size(); i++) {
			Step step = steps.get(i);
			StepItem item = new StepItem(i, step);
			flow.getChildren().add(item);
		}
		next();
	}

	@FXML
	public void prev() {
		flow.prev(scroll);
	}

	@FXML
	public void next() {
		flow.next(scroll);
	}
}

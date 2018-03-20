package com.cas.sim.tis.view.control.imp.jme;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.controlsfx.control.PopOver;
import org.controlsfx.control.PopOver.ArrowLocation;

import com.cas.sim.tis.app.state.TypicalCaseState;
import com.cas.sim.tis.util.JmeUtil;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.view.control.IDistory;
import com.cas.sim.tis.view.controller.DrawingController;

import de.felixroske.jfxsupport.GUIState;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class TypicalCaseBtnController implements IDistory {
	@FXML
	private StackPane content;

	private TypicalCaseState state;

	private PopOver wirePicker;

	private Label colorPicked;
	private Label sectionPicked;
	private Label preview;
	private TextField num;

	private Stage drawingWin;

	private TypicalCase3D typicalCase3D;

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

		number.getChildren().add(new Label(MsgUtil.getMessage("typical.case.wire.num")));

		number.getChildren().add(num = new TextField());

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
		list.add(new Label("0.75mm", new WireRadius(2, Color.rgb(186, 100, 64), 6, Color.TRANSPARENT))); //
		list.add(new Label("1.00mm", new WireRadius(3, Color.rgb(186, 100, 64), 7, Color.TRANSPARENT))); //
		list.add(new Label("1.50mm", new WireRadius(4, Color.rgb(186, 100, 64), 8, Color.TRANSPARENT))); //
		list.add(new Label("2.50mm", new WireRadius(6, Color.rgb(186, 100, 64), 10, Color.TRANSPARENT))); //
		list.add(new Label("4.00mm", new WireRadius(8, Color.rgb(186, 100, 64), 12, Color.TRANSPARENT)));//

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

		if (state != null && state.getCircuitState() != null) {
			state.getCircuitState().setWidth((float) cir.getInnerRadius() * 2);
		}
	}

	private void initWireColorPane(VBox color) {
		color.setAlignment(Pos.TOP_CENTER);

		color.getChildren().add(new Label(MsgUtil.getMessage("typical.case.wire.color")));
		List<Label> list = new ArrayList<>();
		list.add(new Label(MsgUtil.getMessage("typical.case.wire.red"), new Circle(10, Color.RED))); //
		list.add(new Label(MsgUtil.getMessage("typical.case.wire.blank"), new Circle(10, Color.BLACK))); //
		list.add(new Label(MsgUtil.getMessage("typical.case.wire.green"), new Circle(10, Color.GREEN))); //
		list.add(new Label(MsgUtil.getMessage("typical.case.wire.yellow"), new Circle(10, Color.YELLOW))); //
		list.add(new Label(MsgUtil.getMessage("typical.case.wire.blue"), new Circle(10, Color.BLUE)));//

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

		if (state != null && state.getCircuitState() != null) {
			state.getCircuitState().setColor(JmeUtil.convert(color));
		}
	}

	public void setState(TypicalCaseState state) {
		this.state = state;
	}

	@Override
	public void distroy() {
		if (drawingWin != null) {
			drawingWin.close();
		}
	}

	public void setUI(TypicalCase3D typicalCase3D) {
		this.typicalCase3D = typicalCase3D;
	}

}

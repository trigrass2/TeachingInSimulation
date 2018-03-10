package com.cas.sim.tis.view.control.imp.jme;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.controlsfx.control.PopOver;
import org.controlsfx.control.PopOver.ArrowLocation;

import com.cas.sim.tis.app.state.TypicalCaseState;
import com.cas.sim.tis.util.JmeUtil;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class TypicalCaseBtnController implements Initializable {
	@FXML
	private StackPane content;
	@FXML
	private Control draw;
	@FXML
	private Control wire;

	private TypicalCaseState state;

	private PopOver wirePicker;

	private Label colorPicked;
	private Label sectionPicked;
	private Label preview;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		wire.setOnMouseClicked(e -> showWirePicker());
	}

	private void showWirePicker() {
		if (wirePicker == null) {
			wirePicker = new PopOver();
			wirePicker.setArrowLocation(ArrowLocation.BOTTOM_CENTER);
			wirePicker.setPrefSize(300, 400);
			wirePicker.setTitle("导线选择面板");
			wirePicker.setContentNode(getWirePickContent());
		}
		Point2D point = wire.localToScreen(wire.getWidth() / 2, -10);
		wirePicker.show(wire, point.getX(), point.getY());
	}

	private Node getWirePickContent() {
		VBox node = new VBox(10);
		node.setPadding(new Insets(10, 10, 10, 10));
//		node.setPrefSize(330, 230);

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

		number.getChildren().add(new Label("线号"));

		number.getChildren().add(new TextField());

		number.getChildren().add(new Label("预览"));

		number.getChildren().add(preview = new Label("未选择", new StackPane(new Circle(1), new Circle(1))));
		preview.setPrefSize(80, 80);
		preview.setAlignment(Pos.CENTER);
		preview.setContentDisplay(ContentDisplay.TOP);
	}

	private void initSectionPane(VBox section) {
		section.setAlignment(Pos.CENTER);

		section.getChildren().add(new Label("线径"));
		List<Label> list = new ArrayList<>();
		list.add(new Label("0.75mm", new Circle(2, Color.rgb(186, 100, 64)))); //
		list.add(new Label("1.0mm", new Circle(3, Color.rgb(186, 100, 64)))); //
		list.add(new Label("1.5mm", new Circle(4, Color.rgb(186, 100, 64)))); //
		list.add(new Label("2.5mm", new Circle(6, Color.rgb(186, 100, 64)))); //
		list.add(new Label("4.0mm", new Circle(8, Color.rgb(186, 100, 64))));//

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

		Circle cir = (Circle) sectionPicked.getGraphic();

		StackPane sp = (StackPane) preview.getGraphic();
		Circle inner = (Circle) sp.getChildren().get(1);

		inner.setFill(cir.getFill());
		inner.setRadius(cir.getRadius());

		Circle outer = (Circle) sp.getChildren().get(0);
		outer.setRadius(cir.getRadius() + 4);

		if (state != null && state.getCircuitState() != null) {
			state.getCircuitState().setWidth((float) cir.getRadius() * 2);
		}
	}

	private void initWireColorPane(VBox color) {
		color.setAlignment(Pos.TOP_CENTER);

		color.getChildren().add(new Label("线色"));
		List<Label> list = new ArrayList<>();
		list.add(new Label("红色", new Circle(10, Color.RED))); //
		list.add(new Label("黑色", new Circle(10, Color.BLACK))); //
		list.add(new Label("绿色", new Circle(10, Color.GREEN))); //
		list.add(new Label("黄色", new Circle(10, Color.YELLOW))); //
		list.add(new Label("蓝色", new Circle(10, Color.BLUE)));//

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

		StackPane sp = (StackPane) preview.getGraphic();
		Circle outer = (Circle) sp.getChildren().get(0);
		Circle cir = (Circle) colorPicked.getGraphic();
		Color color = (Color) cir.getFill();
		outer.setFill(color);

		if (state != null && state.getCircuitState() != null) {
			state.getCircuitState().setColor(JmeUtil.convert(color));
		}
	}

	public void setState(TypicalCaseState state) {
		this.state = state;
	}

}

package com.cas.sim.tis.view.control.imp.jme;

import java.io.IOException;
import java.util.ResourceBundle;

import org.controlsfx.control.PopOver;
import org.controlsfx.control.PopOver.ArrowLocation;

import com.cas.sim.tis.app.state.TypicalCaseState;
import com.cas.sim.tis.util.JmeUtil;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.view.control.IDistory;
import com.cas.sim.tis.view.controller.DrawingController;

import de.felixroske.jfxsupport.GUIState;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

public class TypicalCaseBtnController implements IDistory {
	private static final Color COPPER = Color.rgb(186, 100, 64);
	@FXML
	private StackPane content;
	@FXML
	private CheckBox showName;

	private TypicalCaseState state;

	private PopOver wirePicker;

	private Label preview;

	private Stage drawingWin;

	private TypicalCase3D typicalCase3D;

	@FXML
	private void toggleTagName() {
		boolean visiable = showName.isSelected();
		if (state != null && state.getCircuitState() != null) {
			state.getCircuitState().setTagNameVisible(visiable);
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
		node.setAlignment(Pos.CENTER_LEFT);
		node.setPadding(new Insets(20));

//		HBox hbox = new HBox(15);

		HBox number = new HBox(10);
		initNumberPane(number);

		HBox color = new HBox(10);
		initWireColorPane(color);

		HBox section = new HBox(10);
		initSectionPane(section);

//		hbox.getChildren().addAll(color, section, number);

		node.getChildren().addAll(color, section, number);

		return node;
	}

	private void initNumberPane(HBox number) {
		number.setAlignment(Pos.CENTER_LEFT);

//		number.getChildren().add(new Label(MsgUtil.getMessage("typical.case.wire.num")));

//		number.getChildren().add(num = new TextField());

		number.getChildren().add(new Label(MsgUtil.getMessage("typical.case.wire.preview")));

		number.getChildren().add(preview = new Label("--", new WireRadius()));
		preview.setPrefSize(145, 80);
		preview.setAlignment(Pos.CENTER);
		preview.setContentDisplay(ContentDisplay.TOP);
		HBox.setHgrow(preview, Priority.ALWAYS);
	}

	private void initSectionPane(HBox section) {
		section.setAlignment(Pos.CENTER_LEFT);

		section.getChildren().add(new Label(MsgUtil.getMessage("typical.case.wire.radius")));
		ObservableList<Label> list = FXCollections.observableArrayList();
		list.addAll(new Label("0.75mm²", new WireRadius(3, COPPER, 10, Color.TRANSPARENT)));
		list.addAll(new Label("1.00mm²", new WireRadius(4, COPPER, 10, Color.TRANSPARENT)));
		list.addAll(new Label("1.50mm²", new WireRadius(6, COPPER, 10, Color.TRANSPARENT)));
		list.addAll(new Label("2.50mm²", new WireRadius(8, COPPER, 10, Color.TRANSPARENT)));
		list.addAll(new Label("4.00mm²", new WireRadius(10, COPPER, 10, Color.TRANSPARENT)));

		ComboBox<Label> sectionCombo = new ComboBox<Label>();
		sectionCombo.getItems().addAll(list);
		// 不定义显示样式的话，下拉选项会在选择一次之后消失，这应该是FX的一个BUG
		sectionCombo.setCellFactory(new Callback<ListView<Label>, ListCell<Label>>() {
			@Override
			public ListCell<Label> call(ListView<Label> p) {
				return new ListCell<Label>() {
					private final WireRadius copper = new WireRadius();
					private final Label label = new Label("", copper);
					{
						copper.setInnerFill(COPPER);
						copper.setOuterRadius(10);
					}

					@Override
					protected void updateItem(Label item, boolean empty) {
						super.updateItem(item, empty);
						if (item == null || empty) {
							setGraphic(null);
						} else {
							label.setText(item.getText());
							WireRadius copper = (WireRadius) item.getGraphic();
							double radius = copper.getInnerRadius();
							this.copper.setInnerRadius(radius);
							setGraphic(label);
						}
					}
				};
			}
		});
		sectionCombo.getSelectionModel().selectedItemProperty().addListener((b, o, n) -> {
			onSectionSelected(n);
		});
		sectionCombo.getSelectionModel().select(0);
		section.getChildren().add(sectionCombo);
	}

	private void onSectionSelected(Label l) {

		preview.setText(l.getText());

		WireRadius cir = (WireRadius) l.getGraphic();

		WireRadius wr = (WireRadius) preview.getGraphic();

		double radius = cir.getInnerRadius();
		wr.setInnerFill(cir.getInnerFill());
		wr.setInnerRadius(radius);
		wr.setOuterRadius(radius + 4);

		if (state != null && state.getCircuitState() != null) {
			state.getCircuitState().setWidth((float) radius * 2);
		}
	}

	private void initWireColorPane(HBox color) {
		color.setAlignment(Pos.CENTER_LEFT);

		color.getChildren().add(new Label(MsgUtil.getMessage("typical.case.wire.color")));
		ObservableList<Rectangle> list = FXCollections.observableArrayList();
		list.add(new Rectangle(90, 20, Color.YELLOW)); //
		list.add(new Rectangle(90, 20, Color.GREEN)); //
		list.add(new Rectangle(90, 20, Color.RED)); //
		list.add(new Rectangle(90, 20, Color.BLUE));//
		list.add(new Rectangle(90, 20, Color.BLACK)); //

		list.forEach(r -> {
			r.setStroke(Color.GRAY);
		});

		ComboBox<Rectangle> colorCombo = new ComboBox<Rectangle>();
		colorCombo.getItems().addAll(list);
		// 不定义显示样式的话，下拉选项会在选择一次之后消失，这应该是FX的一个BUG
		colorCombo.setCellFactory(new Callback<ListView<Rectangle>, ListCell<Rectangle>>() {
			@Override
			public ListCell<Rectangle> call(ListView<Rectangle> p) {
				return new ListCell<Rectangle>() {
					private final Rectangle rectangle;
					{
						setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
						rectangle = new Rectangle(90, 20);
						rectangle.setStroke(Color.GRAY);
					}

					@Override
					protected void updateItem(Rectangle item, boolean empty) {
						super.updateItem(item, empty);
						if (item == null || empty) {

						} else {
							rectangle.setFill(item.getFill());
							setGraphic(rectangle);
						}
					}
				};
			}
		});
		colorCombo.getSelectionModel().selectedItemProperty().addListener((b, o, n) -> {
			onColorSelected(n);
		});
		colorCombo.getSelectionModel().select(0);
		color.getChildren().add(colorCombo);
	}

	private void onColorSelected(Rectangle rectangle) {
		WireRadius wr = (WireRadius) preview.getGraphic();

		Color color = (Color) rectangle.getFill();
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
		if (wirePicker != null && wirePicker.isShowing()) {
			wirePicker.hide();
		}
	}

	public void setUI(TypicalCase3D typicalCase3D) {
		this.typicalCase3D = typicalCase3D;
	}

	public void clean() {
		showName.setSelected(false);

		if (drawingWin != null) {
			drawingWin.close();
		}

		if (wirePicker != null) {
			wirePicker.setContentNode(getWirePickContent());
		}
	}

}

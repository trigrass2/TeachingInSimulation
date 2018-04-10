package com.cas.sim.tis.view.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;

import com.cas.sim.tis.action.ResourceAction;
import com.cas.sim.tis.consts.ResourceConsts;
import com.cas.sim.tis.consts.ResourceType;
import com.cas.sim.tis.entity.Resource;
import com.cas.sim.tis.entity.TypicalCase;
import com.cas.sim.tis.svg.SVGGlyph;
import com.cas.sim.tis.util.AlertUtil;
import com.cas.sim.tis.util.HTTPUtils;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.control.imp.dialog.Dialog;
import com.cas.sim.tis.view.control.imp.jme.DrawingSelectDialog;
import com.cas.sim.tis.view.control.imp.jme.TypicalCase3D;
import com.cas.sim.tis.view.control.imp.preparation.ResourceUploadDialog;
import com.cas.util.MathUtil;
import com.cas.util.StringUtil;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class DrawingController implements Initializable {
	@FXML
	private HBox handle;
	@FXML
	private Button max;
	@FXML
	private Tooltip maxTip;
	@FXML
	private VBox expand;
	@FXML
	private HBox btns;
	@FXML
	private Label name;
	@FXML
	private ScrollPane scorll;
	@FXML
	private AnchorPane pane;
	@FXML
	private ImageView drawing;
	@FXML
	private Label scale;
	@FXML
	private Button zoomIn;
	@FXML
	private Button zoomOut;
	@FXML
	private Button prev;
	@FXML
	private Button next;

	private boolean expanding = true;

	private float scaleVal;

	private Stage stage;

	private double xOffset;
	private double yOffset;
	private TypicalCase3D typicalCase3D;

	private List<String> drawings = new ArrayList<String>();
	private ToggleGroup group = new ToggleGroup();
	private int index;

	private HTTPUtils utils;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		utils = SpringUtil.getBean(HTTPUtils.class);
		handle.setOnMouseDragged(e -> {
			stage.setX(e.getScreenX() + xOffset);
			stage.setY(e.getScreenY() + yOffset);
		});
		handle.setOnMousePressed(e -> {
//			按下鼠标后，记录当前鼠标的坐标
			xOffset = stage.getX() - e.getScreenX();
			yOffset = stage.getY() - e.getScreenY();
		});
		group.selectedToggleProperty().addListener((b, o, n) -> {
			if (n == null) {
				group.selectToggle(o);
				return;
			}
			index = group.getToggles().indexOf(n);
			prev.setDisable(false);
			next.setDisable(false);
			if (index <= 0) {
				prev.setDisable(true);
			}
			if (index >= drawings.size() - 1) {
				next.setDisable(true);
			}
			Resource resource = (Resource) n.getUserData();
			String url = utils.getFullPath(ResourceConsts.FTP_RES_PATH + resource.getPath());
			loadDrawing(resource.getName(), url);
		});
		pane.addEventFilter(ScrollEvent.ANY, new EventHandler<ScrollEvent>() {

			@Override
			public void handle(ScrollEvent event) {
				double delta = event.getDeltaY();
				double x = event.getX();
				double y = event.getY();
				double leftOffset = drawing.getLayoutX();
				double topOffset = drawing.getLayoutY();
				double width = drawing.getFitWidth();
				double height = drawing.getFitHeight();
				double xPecent = (x - leftOffset) / width;
				double yPecent = (y - topOffset) / height;

				if (delta < 0) {
					zoomOut();
				} else {
					zoomIn();
				}
				width = drawing.getFitWidth();
				height = drawing.getFitHeight();
				if (width > pane.getWidth() || height > pane.getHeight()) {
					leftOffset = x - (width * xPecent);
					topOffset = y - (height * yPecent);
					drawing.setLayoutX(leftOffset);
					drawing.setLayoutY(topOffset);
				} else {
					toCenter(pane.getWidth(), pane.getHeight());
				}
			}
		});
	}

	private void addDrawingPreviewBtn(final Resource resource) {
		String url = utils.getFullPath(ResourceConsts.FTP_RES_PATH + resource.getPath());
		Image image = new Image(url, 70, 70, true, true);

		ImageView view = new ImageView(image);

		ToggleButton toggle = new ToggleButton();
		toggle.setGraphic(view);
		toggle.getStyleClass().add("drawing-btn");
		toggle.setUserData(resource);

		ContextMenu menu = new ContextMenu();
		MenuItem item = new MenuItem(MsgUtil.getMessage("button.delete"));
		item.setOnAction(e -> {
			AlertUtil.showConfirm(stage, MsgUtil.getMessage("alert.confirmation.data.delete"), resp -> {
				if (ButtonType.YES == resp) {
					drawings.remove(String.valueOf(resource.getId()));
					refresh();
				}
			});
		});
		menu.getItems().add(item);
		toggle.setContextMenu(menu);

		group.getToggles().add(toggle);
		btns.getChildren().add(toggle);
	}

	private void loadDrawing(String name, String url) {
		zoomIn.setDisable(true);
		zoomOut.setDisable(false);

		this.name.setText(name);
		drawing.setImage(new Image(url));
		scale.setText(String.format("%d%%", 100));
		scaleVal = 1;
		zoom();

		resizePane();
	}

	@FXML
	private void prev() {
		if (index - 1 <= 0) {
			index = 0;
		} else {
			index--;
		}
		group.selectToggle(group.getToggles().get(index));
	}

	@FXML
	private void next() {
		if (index + 1 >= drawings.size() - 1) {
			index = drawings.size() - 1;
		} else {
			index++;
		}
		group.selectToggle(group.getToggles().get(index));
	}

	@FXML
	private void zoomIn() {
		scaleVal = scaleVal + 0.1f;
		if (scaleVal > 1) {
			scaleVal = 1;
			zoomIn.setDisable(true);
		}
		zoomOut.setDisable(false);
		zoom();
	}

	@FXML
	private void zoomOut() {
		scaleVal = MathUtil.round(1, scaleVal - 0.1f);
		if (scaleVal < 0.1) {
			scaleVal = 0.1f;
			zoomOut.setDisable(true);
		}
		zoomIn.setDisable(false);
		zoom();
	}

	private void zoom() {
		scale.setText(String.format("%d%%", (int) (scaleVal * 100)));
		drawing.setFitHeight(drawing.getImage().getHeight() * scaleVal);
		drawing.setFitWidth(drawing.getImage().getWidth() * scaleVal);
	}

	@FXML
	private void toggle(ActionEvent event) {
		if (expanding) {
			expand.setMinHeight(40);
			expand.setMaxHeight(40);
			((Button) event.getSource()).setGraphic(new SVGGlyph("iconfont.svg.up", Color.web("#19b0c6"), 10));
		} else {
			expand.setMinHeight(140);
			expand.setMaxHeight(140);
			((Button) event.getSource()).setGraphic(new SVGGlyph("iconfont.svg.down", Color.web("#19b0c6"), 10));
		}
		expanding = !expanding;
	}

	@FXML
	private void showSelectDrawingDialog() {
		Dialog<Integer> dialog = new Dialog<>();
		dialog.setDialogPane(new DrawingSelectDialog());
		dialog.setTitle(MsgUtil.getMessage("typical.case.drawings.select"));
		dialog.setPrefSize(640, 540);
		dialog.initOwner(stage);
		dialog.showAndWait().ifPresent(id -> {
			if (id == null) {
				return;
			}
			// 记录到数据库
			addDrawings(id);
		});
	}

	@FXML
	private void showUploadDrawingDialog() {
		Dialog<Integer> dialog = new Dialog<>();
		dialog.setDialogPane(new ResourceUploadDialog(ResourceType.DRAWING));
		dialog.setTitle(MsgUtil.getMessage("typical.case.drawings.upload"));
		dialog.setPrefSize(640, 330);
		dialog.initOwner(stage);
		dialog.showAndWait().ifPresent(id -> {
			if (id == null) {
				return;
			}
			// 记录到数据库
			addDrawings(id);
			AlertUtil.showAlert(AlertType.INFORMATION, MsgUtil.getMessage("ftp.upload.success"));
		});
	}

	private void initDrawings(TypicalCase3D typicalCase3D) {
		clean();

		TypicalCase typicalCase = typicalCase3D.getTypicalCase();
		String drawings = typicalCase.getDrawings();

		if (StringUtils.isEmpty(drawings)) {
			return;
		}
		this.drawings = StringUtil.split(drawings);
		List<Resource> resources = SpringUtil.getBean(ResourceAction.class).findResourcesByIds(this.drawings);
		for (Resource resource : resources) {
			addDrawingPreviewBtn(resource);
		}

		index = 0;
		group.selectToggle(group.getToggles().get(index));
	}

	private void addDrawings(Integer id) {
		drawings.add(String.valueOf(id));
		refresh();
	}

	private void refresh() {
		TypicalCase typicalCase = typicalCase3D.getTypicalCase();
		typicalCase.setDrawings(StringUtil.combine(drawings));
//		if (typicalCase.getId() != null) {
//			// 典型案例已经保存过，则直接更新新增图纸到数据库
//			TypicalCaseAction action = SpringUtil.getBean(TypicalCaseAction.class);
//			action.modify(typicalCase);
//		}
		initDrawings(typicalCase3D);
	}

	@FXML
	private void requestFocus() {
		handle.requestFocus();
	}

	/**
	 * 窗口最小化
	 */
	@FXML
	private void min() {
		this.stage.setIconified(true);
	}

	/**
	 * 窗口最大化
	 */
	@FXML
	private void max() {
		this.stage.setMaximized(!this.stage.isMaximized());
		maximize();
	}

	public void maximize() {
		if (this.stage.isMaximized()) {
			max.setGraphic(new SVGGlyph("iconfont.svg.revert", Color.web("#A2CBF3"), 10));
			maxTip.setText(MsgUtil.getMessage("button.revert"));
		} else {
			max.setGraphic(new SVGGlyph("iconfont.svg.max", Color.web("#A2CBF3"), 10));
			maxTip.setText(MsgUtil.getMessage("button.maximize"));
		}
	}

	@FXML
	public void close() {
		if (stage == null) {
			return;
		}
		this.stage.close();
	}

	public void setStage(Stage stage) {
		this.stage = stage;
		this.stage.setOnShowing(e -> {
			if (typicalCase3D != null) {
				initDrawings(typicalCase3D);
			}
		});
		this.stage.heightProperty().addListener((b, o, n) -> {
			resizePane();
		});
	}

	public void setUI(TypicalCase3D typicalCase3D) {
		this.typicalCase3D = typicalCase3D;
	}

	private void resizePane() {
		double width = stage.getWidth();
		double height = stage.getHeight() - 70;
		pane.setMinSize(width, height);
		pane.setMaxSize(width, height);
		pane.setPrefSize(width, height);
		if (pane.getClip() == null) {
			pane.setClip(new Rectangle(pane.getWidth(), pane.getHeight()));
		} else {
			Rectangle rectangle = (Rectangle) pane.getClip();
			rectangle.setWidth(width);
			rectangle.setHeight(height);
		}
		toCenter(width, height);
		System.out.printf("maximized=%b,width=%f,height=%f%n", stage.isMaximized(), width, height);
	}

	private void toCenter(double width, double height) {
		double x = (width - drawing.getFitWidth()) / 2;
		double y = (height - drawing.getFitHeight()) / 2;
		drawing.setLayoutX(x);
		drawing.setLayoutY(y);
	}

	private void clean() {
		group.getToggles().clear();
		btns.getChildren().clear();
		drawing.setFitHeight(0);
		drawing.setFitWidth(0);
		drawing.setImage(null);
		prev.setDisable(true);
		next.setDisable(true);
		zoomIn.setDisable(true);
		zoomOut.setDisable(true);
	}
}

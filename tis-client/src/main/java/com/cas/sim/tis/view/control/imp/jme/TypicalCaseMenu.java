package com.cas.sim.tis.view.control.imp.jme;

import com.alibaba.fastjson.JSONArray;
import com.cas.sim.tis.action.DrawAction;
import com.cas.sim.tis.action.TypicalCaseAction;
import com.cas.sim.tis.entity.ElecComp;
import com.cas.sim.tis.entity.TypicalCase;
import com.cas.sim.tis.svg.SVGGlyph;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.control.ILeftContent;
import com.cas.sim.tis.view.control.imp.SearchBox;
import com.cas.sim.tis.view.control.imp.dialog.Dialog;
import com.cas.sim.tis.view.control.imp.dialog.DialogPane;
import com.cas.sim.tis.view.control.imp.table.Column;
import com.cas.sim.tis.view.control.imp.table.Table;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class TypicalCaseMenu implements ILeftContent {

	private TypicalCase3D typicalCase3D;

	public TypicalCaseMenu(TypicalCase3D typicalCase3D) {
		this.typicalCase3D = typicalCase3D;
	}

	@Override
	public Region getLeftContent() {
		VBox vb = new VBox(10);
//		vb.getStyleClass().add("cover");
		Button create = createMenu(MsgUtil.getMessage("menu.button.new"), new SVGGlyph("iconfont.svg.new", Color.WHITE, 32));
//		create.setOnMouseClicked(e -> showDrawDialog());
		create.setOnMouseClicked(e -> newCase());

		Button open = createMenu(MsgUtil.getMessage("menu.button.open"), new SVGGlyph("iconfont.svg.open", Color.WHITE, 32));
		open.setOnMouseClicked(e -> showCaseDialog());

		Button save = createMenu(MsgUtil.getMessage("menu.button.save"), new SVGGlyph("iconfont.svg.save", Color.WHITE, 32));
		save.setOnMouseClicked(e -> saveCase());

		HBox menu = new HBox(22, create, open, save);
		menu.setAlignment(Pos.CENTER);
//		menu.getStyleClass().add("cover");
//		菜单
		vb.getChildren().add(menu);

		vb.getChildren().add(new ElecCompTree(elecComp -> typicalCase3D.selectedElecComp(elecComp)));

		return vb;
	}

	private Button createMenu(String text, Node graphic) {
		Button btn = new Button(text, graphic);
		btn.setContentDisplay(ContentDisplay.TOP);
		btn.setPrefSize(45, 45);
		btn.setAlignment(Pos.CENTER);
		btn.getStyleClass().add("img-btn");
		btn.setTextFill(Color.WHITE);
		btn.setFont(new Font(14));
		return btn;
	}

	// open打开案例选择面板
	private void showCaseDialog() {
		Dialog<ElecComp> dialog = new Dialog<>();
		DialogPane<ElecComp> dialogPane = new DialogPane<>();
		dialogPane.setTitle(MsgUtil.getMessage("typical.case.title.list"));
		dialogPane.getChildren().add(createCaseListUI());
		dialog.setDialogPane(dialogPane);
		dialog.setPrefSize(300, 400);
		dialog.showAndWait();
	}

	private Node createCaseListUI() {
		VBox vb = new VBox(15);
		ScrollPane sp = new ScrollPane(vb);

		TypicalCaseAction action = SpringUtil.getBean(TypicalCaseAction.class);
		action.getTypicalCaseList().forEach(c -> {
			Button item = new Button(c.getName());
			item.setOnMouseClicked(e -> typicalCase3D.setupCase(c));
			vb.getChildren().add(item);
		});
		for (int i = 0; i < 20; i++) {
			Button item = new Button("Test-" + i);
			vb.getChildren().add(item);
		}

		return sp;
	}

	private void showDrawDialog() {
		Dialog<ElecComp> dialog = new Dialog<>();
		DialogPane<ElecComp> dialogPane = new DialogPane<>();
		dialogPane.setSpacing(15);

		dialogPane.setTitle(MsgUtil.getMessage("typical.case.title.drawings"));

		HBox hbox = new HBox(15);
		hbox.setPadding(new Insets(0, 10, 0, 10));

		HBox menu = new HBox(15);
		HBox.setHgrow(menu, Priority.ALWAYS);

		ToggleGroup tg = new ToggleGroup();
		ToggleButton sys = new ToggleButton(MsgUtil.getMessage("typical.case.sys.drawing"));
		ToggleButton mine = new ToggleButton(MsgUtil.getMessage("typical.case.min.drawing"));
		tg.getToggles().add(sys);
		tg.getToggles().add(mine);
//		系统图库
		menu.getChildren().add(sys);
//		我的图库
		menu.getChildren().add(mine);

		hbox.getChildren().add(menu);
//		搜索框
		SearchBox search = new SearchBox();
		search.setPrefWidth(128);
		hbox.getChildren().add(search);

		dialogPane.getChildren().add(hbox);

//		图纸列表
		dialogPane.getChildren().add(createDrawListUI());
//		案例名称
		TextField textField = new TextField();
		textField.setPromptText(MsgUtil.getMessage("typical.case.prompt.input.case"));
		dialogPane.getChildren().add(textField);

		dialogPane.getChildren().add(new Button(MsgUtil.getMessage("button.ok")));

		dialog.setDialogPane(dialogPane);
		dialog.setPrefSize(380, 600);
		dialog.showAndWait();
	}

	private Node createDrawListUI() {
//		serial="true" rowHeight="45" spacing="10" separatorable="false" normalStyleClass="table-row" hoverStyleClass="table-row-hover" selectedStyleClass="table-row-selected" 
		Table table = new Table("table-row", "table-row-hover", "table-row-selected");
		table.setSerial(true);
		table.setRowHeight(35);
		table.setSpacing(5);
//		table.setSeparatorable(false);

		DrawAction action = SpringUtil.getBean(DrawAction.class);

		// 数据库唯一表示
		Column<Integer> primary = new Column<>();
		primary.setPrimary(true);
		primary.setVisible(false);
		primary.setKey("id");
		// 题库名称
		Column<String> name = new Column<>();
		name.setAlignment(Pos.CENTER_LEFT);
		name.setKey("name");
		name.setText(MsgUtil.getMessage("library.name"));
		name.setPrefWidth(250);
		table.getColumns().addAll(primary, name);

		JSONArray array = new JSONArray();
		array.addAll(action.getDrawListAll());
		table.setItems(array);
		table.build();

		return table;
	}

	private void newCase() {
		TypicalCase t = new TypicalCase();
		t.setName("NonName");

		typicalCase3D.setupCase(t);
	}

	private void saveCase() {
		typicalCase3D.save();
	}

}

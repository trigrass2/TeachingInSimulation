package com.cas.sim.tis.view.control.imp.preparation;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.util.TypeUtils;
import com.cas.sim.tis.action.TypicalCaseAction;
import com.cas.sim.tis.consts.RoleConst;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.TypicalCase;
import com.cas.sim.tis.util.AlertUtil;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.control.imp.dialog.DialogPane;
import com.cas.sim.tis.view.control.imp.table.BtnCell;
import com.cas.sim.tis.view.control.imp.table.Cell;
import com.cas.sim.tis.view.control.imp.table.Column;
import com.cas.sim.tis.view.control.imp.table.Row;
import com.cas.sim.tis.view.control.imp.table.Table;
import com.cas.util.DateUtil;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

public class TypicalCaseSelectDialog extends DialogPane<Integer> {

	private Table table;
	private ToggleGroup group = new ToggleGroup();
	private ToggleButton sys;
	private Column<String> delete;

	public TypicalCaseSelectDialog(boolean editable) {
		VBox box = new VBox(20);
		VBox.setVgrow(box, Priority.ALWAYS);
		box.setAlignment(Pos.TOP_CENTER);
		box.setPadding(new Insets(20));

		HBox toggleBox = new HBox(10);
		if (Session.get(Session.KEY_LOGIN_ROLE, 0) != RoleConst.ADMIN) {
			ToggleButton mine = new ToggleButton(MsgUtil.getMessage("typical.case.min.case"));
			mine.setMinSize(100, 40);
			mine.setStyle("-fx-font-size:14px");
			mine.setUserData(Session.get(Session.KEY_LOGIN_ID));
			group.getToggles().add(mine);
			toggleBox.getChildren().add(mine);
		}
		sys = new ToggleButton(MsgUtil.getMessage("typical.case.sys.case"));
		sys.setMinSize(100, 40);
		sys.setStyle("-fx-font-size:14px");
		sys.setUserData(1);
		group.getToggles().add(sys);
		toggleBox.getChildren().add(sys);
		group.selectedToggleProperty().addListener((b, o, n) -> {
			if (n == null) {
				group.selectToggle(o);
			} else {
				reload();
			}
		});

		table = new Table("table-row", "table-row-hover", "table-row-selected");
		table.setSerial(true);
		table.setRowHeight(45);
		table.setRowsSpacing(1);
		table.setSeparatorable(false);
		// 数据库唯一表示
		Column<Integer> id = new Column<>();
		id.setPrimary(true);
		id.setVisible(false);
		id.setKey("id");
		// 典型案例
		Column<Integer> name = new Column<>();
		name.setKey("name");
		name.setText(MsgUtil.getMessage("preparation.typical.case"));
		// 日期
		Column<Date> date = new Column<>();
		date.setAlignment(Pos.CENTER);
		date.setKey("createDate");
		date.setText(MsgUtil.getMessage("preparation.create.date"));
		date.setMaxWidth(160);
		date.setCellFactory(Cell.forTableColumn(new StringConverter<Date>() {

			@Override
			public String toString(Date date) {
				return DateUtil.date2Str(date, DateUtil.DATE_TIME_PAT_SHOW_);
			}

			@Override
			public Date fromString(String string) {
				return null;
			}
		}));
		table.getColumns().addAll(id, name, date);
		if (editable) {
			// 删除按钮
			delete = new Column<String>();
			delete.setCellFactory(BtnCell.forTableColumn(MsgUtil.getMessage("button.delete"), "blue-btn", rid -> {
				AlertUtil.showConfirm(MsgUtil.getMessage("alert.confirmation.data.delete"), response -> {
					if (response == ButtonType.NO) {
						return;
					}
					SpringUtil.getBean(TypicalCaseAction.class).delete((Integer) rid);
					reload();
				});
			}));
			delete.setAlignment(Pos.CENTER_RIGHT);
			delete.setMaxWidth(58);
			table.getColumns().add(delete);
		}

		ScrollPane scroll = new ScrollPane(table);
		scroll.setHbarPolicy(ScrollBarPolicy.NEVER);
		scroll.setFitToWidth(true);
		VBox.setVgrow(scroll, Priority.ALWAYS);

		Label error = new Label();
		error.getStyleClass().add("red");

		Button ok = new Button(MsgUtil.getMessage("button.ok"));
		ok.getStyleClass().add("blue-btn");
		ok.setMinSize(390, 40);
		ok.setPrefSize(390, 40);
		ok.setOnAction(e -> {
			Row row = table.getSelectedRow();
			if (row == null) {
				error.setText(MsgUtil.getMessage("alert.warning.must.select", MsgUtil.getMessage("preparation.typical.case")));
				return;
			}
			this.setResult(row.getItems().getInteger("id"));
		});

		box.getChildren().addAll(toggleBox, scroll, error, ok);
		getChildren().add(box);

		group.selectToggle(group.getToggles().get(0));
	}

	private void reload() {
		Toggle toggle = group.getSelectedToggle();
		Integer creator = TypeUtils.castToInt(toggle.getUserData());
		if (delete == null) {

		} else if (sys.isSelected()) {
			if (Session.get(Session.KEY_LOGIN_ROLE, 0) != RoleConst.ADMIN) {
				table.getColumns().remove(delete);
			}
		} else if (!table.getColumns().contains(delete)) {
			table.getColumns().add(delete);
		}

		List<TypicalCase> cases = SpringUtil.getBean(TypicalCaseAction.class).getTypicalCasesByCreator(creator);
		JSONArray array = new JSONArray();
		array.addAll(cases);
		table.setItems(array);
		table.build();
	}
}

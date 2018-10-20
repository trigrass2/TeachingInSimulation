package com.cas.sim.tis.view.control.imp.free;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.cas.sim.tis.action.ArchiveCaseAction;
import com.cas.sim.tis.consts.ArchiveType;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.ArchiveCase;
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
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

public class FreeCaseSelectDialog extends DialogPane<Integer> {

	private Table table;

	public FreeCaseSelectDialog() {
		VBox box = new VBox(20);
		VBox.setVgrow(box, Priority.ALWAYS);
		box.setAlignment(Pos.TOP_CENTER);
		box.setPadding(new Insets(20));

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
		// 案例
		Column<Integer> name = new Column<>();
		name.setKey("name");
		name.setText(MsgUtil.getMessage("preparation.free.case"));
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
		// 删除按钮
		Column<String> delete = new Column<String>();
		delete.setCellFactory(BtnCell.forTableColumn(MsgUtil.getMessage("button.delete"), "red-btn", rid -> {
			AlertUtil.showConfirm(MsgUtil.getMessage("alert.confirmation.data.delete"), response -> {
				if (response == ButtonType.NO) {
					return;
				}
				SpringUtil.getBean(ArchiveCaseAction.class).deleteByLogic((Integer) rid);
				reload();
			});
		}));
		delete.setAlignment(Pos.CENTER_RIGHT);
		delete.setMaxWidth(58);
		table.getColumns().add(delete);

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

		box.getChildren().addAll(scroll, error, ok);
		getChildren().add(box);
		
		reload();
	}

	private void reload() {
		Integer creator = Session.get(Session.KEY_LOGIN_ID);
		List<ArchiveCase> cases = SpringUtil.getBean(ArchiveCaseAction.class).getArchiveCasesByCreator(creator, false, ArchiveType.FREE.getIndex());
		JSONArray array = new JSONArray();
		array.addAll(cases);
		table.setItems(array);
		table.build();
	}

}

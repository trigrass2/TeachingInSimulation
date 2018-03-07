package com.cas.sim.tis.view.control.imp.preparation;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.cas.sim.tis.entity.TypicalCase;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.view.control.imp.dialog.DialogPane;
import com.cas.sim.tis.view.control.imp.table.Column;
import com.cas.sim.tis.view.control.imp.table.Row;
import com.cas.sim.tis.view.control.imp.table.Table;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class TypicalCaseSelectDialog extends DialogPane<Integer> {

	public TypicalCaseSelectDialog(List<TypicalCase> cases) {
		VBox box = new VBox(25);
		VBox.setVgrow(box, Priority.ALWAYS);
		box.setAlignment(Pos.TOP_CENTER);
		box.setPadding(new Insets(20));

		Table table = new Table("table-row", "table-row-hover", "table-row-selected");
		table.setSerial(true);
		table.setRowHeight(45);
		table.setHeaderable(false);
		// 数据库唯一表示
		Column<Integer> id = new Column<>();
		id.setPrimary(true);
		id.setVisible(false);
		id.setKey("id");
		// 资源图标
		Column<Integer> name = new Column<>();
		name.setAlignment(Pos.CENTER_RIGHT);
		name.setKey("name");
		table.getColumns().addAll(id, name);
		JSONArray array = new JSONArray();
		array.addAll(cases);
		table.build();

		ScrollPane scroll = new ScrollPane();
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
	}
}

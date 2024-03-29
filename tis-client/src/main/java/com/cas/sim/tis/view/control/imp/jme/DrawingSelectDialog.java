package com.cas.sim.tis.view.control.imp.jme;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import com.alibaba.fastjson.JSONArray;
import com.cas.sim.tis.action.ResourceAction;
import com.cas.sim.tis.consts.ResourceType;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.Resource;
import com.cas.sim.tis.svg.SVGGlyph;
import com.cas.sim.tis.util.AlertUtil;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.control.imp.SearchBox;
import com.cas.sim.tis.view.control.imp.dialog.DialogPane;
import com.cas.sim.tis.view.control.imp.table.BtnCell;
import com.cas.sim.tis.view.control.imp.table.Column;
import com.cas.sim.tis.view.control.imp.table.Row;
import com.cas.sim.tis.view.control.imp.table.SVGIconCell;
import com.cas.sim.tis.view.control.imp.table.Table;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class DrawingSelectDialog extends DialogPane<Integer> {

	private Table table;
	private SearchBox search;

	private List<Integer> types = Arrays.asList(ResourceType.DRAWING.getType());

	public DrawingSelectDialog() {
		VBox box = new VBox(15);
		VBox.setVgrow(box, Priority.ALWAYS);
		box.setAlignment(Pos.TOP_CENTER);
		box.setPadding(new Insets(20));

		search = new SearchBox();
		search.setOnSearch(text -> {
			reload();
		});
		HBox searchBox = new HBox();
		searchBox.getChildren().add(search);
		searchBox.setAlignment(Pos.CENTER_RIGHT);
		HBox.setHgrow(searchBox, Priority.ALWAYS);

		HBox toggleBox = new HBox(10);
		toggleBox.getChildren().addAll(searchBox);

		table = new Table("table-row", "table-row-hover", "table-row-selected");
		table.setSerial(true);
		table.setRowHeight(45);
		table.setSeparatorable(false);
		table.setRowsSpacing(1);
		// 数据库唯一表示
		Column<Integer> id = new Column<>();
		id.setPrimary(true);
		id.setVisible(false);
		id.setKey("id");
		// 资源图标
		Column<Integer> icon = new Column<>();
		icon.setAlignment(Pos.CENTER_RIGHT);
		icon.setKey("type");
		icon.setText("");
		icon.setMaxWidth(22);
		Function<Integer, SVGGlyph> converter = new Function<Integer, SVGGlyph>() {

			@Override
			public SVGGlyph apply(Integer type) {
				if (type == null) {
					return null;
				}
				ResourceType resourceType = ResourceType.getResourceType(type);
				return new SVGGlyph(resourceType.getIcon(), resourceType.getColor(), 22);
			}
		};
		icon.setCellFactory(SVGIconCell.forTableColumn(converter));
		// 资源名称
		Column<String> name = new Column<>();
		name.setAlignment(Pos.CENTER_LEFT);
		name.setKey("name");
		name.setText(MsgUtil.getMessage("resource.name"));
		// 删除按钮
		Column<String> delete = new Column<String>();
		delete.setCellFactory(BtnCell.forTableColumn(MsgUtil.getMessage("button.delete"), "blue-btn", rid -> {
			AlertUtil.showConfirm(dialog.getWindow(), MsgUtil.getMessage("alert.confirmation.data.delete"), response -> {
				if (response == ButtonType.YES) {
					SpringUtil.getBean(ResourceAction.class).deteleByLogic((Integer) rid);
					reload();
				}
			});
		}));
		delete.setAlignment(Pos.CENTER_RIGHT);
		delete.setMaxWidth(58);
		table.getColumns().addAll(id, icon, name, delete);

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
			Row selected = table.getSelectedRow();
			if (selected == null) {
				error.setText(MsgUtil.getMessage("alert.warning.must.select", MsgUtil.getMessage("elec.case.drawings")));
				return;
			}
			dialog.setResult(selected.getItems().getInteger("id"));
		});

		box.getChildren().addAll(toggleBox, scroll, error, ok);
		getChildren().add(box);

		reload();
	}

	private void reload() {
		List<Resource> resources = SpringUtil.getBean(ResourceAction.class).findResourcesByCreator(types, search.getText(), Session.get(Session.KEY_LOGIN_ID));
		JSONArray array = new JSONArray();
		array.addAll(resources);
		table.setItems(array);
		table.build();
	}
}

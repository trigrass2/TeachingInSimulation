package com.cas.sim.tis.view.control.imp.preparation;

import java.util.List;
import java.util.function.Function;

import com.alibaba.fastjson.JSONArray;
import com.cas.sim.tis.action.ResourceAction;
import com.cas.sim.tis.consts.ResourceType;
import com.cas.sim.tis.entity.Resource;
import com.cas.sim.tis.svg.SVGGlyph;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.control.imp.SearchBox;
import com.cas.sim.tis.view.control.imp.dialog.DialogPane;
import com.cas.sim.tis.view.control.imp.table.Column;
import com.cas.sim.tis.view.control.imp.table.Row;
import com.cas.sim.tis.view.control.imp.table.SVGIconCell;
import com.cas.sim.tis.view.control.imp.table.Table;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class ResourceSelectedDialog extends DialogPane<Integer> {

	private Table table;
	private SearchBox search;

	public ResourceSelectedDialog() {
		VBox box = new VBox(25);
		VBox.setVgrow(box, Priority.ALWAYS);
		box.setAlignment(Pos.TOP_CENTER);
		box.setPadding(new Insets(20));

		search = new SearchBox();
		search.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ENTER) {
				reload();
			}
		});
		HBox searchBox = new HBox();
		searchBox.setAlignment(Pos.CENTER_RIGHT);
		searchBox.getChildren().add(search);

		table = new Table("table-row", "table-row-hover", "table-row-selected");
		table.setSerial(true);
		table.setRowHeight(45);
		table.setSeparatorable(false);
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
		icon.setMaxWidth(25);
		Function<Integer, SVGGlyph> converter = new Function<Integer, SVGGlyph>() {

			@Override
			public SVGGlyph apply(Integer type) {
				if (type == null) {
					return null;
				}
				ResourceType resourceType = ResourceType.getResourceType(type);
				return new SVGGlyph(resourceType.getIcon(), resourceType.getColor(), 22, 25);
			}
		};
		icon.setCellFactory(SVGIconCell.forTableColumn(converter));
		// 资源名称
		Column<String> name = new Column<>();
		name.setAlignment(Pos.CENTER_LEFT);
		name.setKey("name");
		name.setText(MsgUtil.getMessage("resource.name"));
		name.setMaxWidth(250);
		table.getColumns().addAll(id, icon, name);

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
				error.setText(MsgUtil.getMessage("alert.warning.must.select", MsgUtil.getMessage("preparation.library.resource")));
				return;
			}
			dialog.setResult(selected.getItems().getInteger("id"));
		});

		box.getChildren().addAll(searchBox, scroll, error, ok);
		getChildren().add(box);

		reload();
	}

	private void reload() {
		List<Resource> resources = SpringUtil.getBean(ResourceAction.class).findPreparationResources(search.getText());
		JSONArray array = new JSONArray();
		array.addAll(resources);
		table.setItems(array);
		table.build();
	}
}

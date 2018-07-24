package com.cas.sim.tis.view.control.imp.preparation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.alibaba.fastjson.JSONArray;
import com.cas.sim.tis.action.ResourceAction;
import com.cas.sim.tis.consts.ResourceType;
import com.cas.sim.tis.consts.Session;
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
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class ResourceSelectedDialog extends DialogPane<Integer> {

	private Table table;
	private SearchBox search;
	private ToggleGroup group = new ToggleGroup();

	private List<Integer> types = new ArrayList<>();

	public ResourceSelectedDialog() {
		VBox box = new VBox(0);
		VBox.setVgrow(box, Priority.ALWAYS);
		box.setAlignment(Pos.TOP_CENTER);
		box.setPadding(new Insets(20));

		ToggleButton sys = new ToggleButton(MsgUtil.getMessage("resource.menu.sys"));
		sys.setMinSize(100, 40);
		sys.setStyle("-fx-font-size:14px");
		sys.setUserData(1);
		ToggleButton mine = new ToggleButton(MsgUtil.getMessage("resource.menu.mine"));
		mine.setMinSize(100, 40);
		mine.setStyle("-fx-font-size:14px");
		mine.setUserData(Session.get(Session.KEY_LOGIN_ID));
		group.getToggles().addAll(sys, mine);
		group.selectedToggleProperty().addListener((b, o, n) -> {
			if (n == null) {
				group.selectToggle(o);
			} else {
				reload();
			}
		});

		search = new SearchBox();
		search.setOnSearch(text -> {
			reload();
		});
		HBox searchBox = new HBox();
		searchBox.getChildren().add(search);
		searchBox.setAlignment(Pos.CENTER_RIGHT);
		HBox.setHgrow(searchBox, Priority.ALWAYS);

		HBox toggleBox = new HBox(10);
		toggleBox.getChildren().addAll(sys, mine, searchBox);

		HBox filterBox = new HBox(10);
		for (ResourceType resourceType : ResourceType.values()) {
			if (ResourceType.LINK == resourceType || ResourceType.DRAWING == resourceType) {
				continue;
			}
			int type = resourceType.getType();
			CheckBox checkBox = new CheckBox();
			checkBox.setGraphic(new SVGGlyph(resourceType.getIcon(), resourceType.getColor(), 22, 25));
			checkBox.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
			checkBox.getStyleClass().add("img-check-box");
			checkBox.setSelected(true);
			checkBox.setUserData(type);
			checkBox.setOnAction(e -> {
				if (!checkBox.isSelected()) {
					types.remove(checkBox.getUserData());
				} else if (!types.contains(checkBox.getUserData())) {
					types.add((Integer) checkBox.getUserData());
				}
				reload();
			});
			types.add(type);
			filterBox.getChildren().add(checkBox);
		}

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
		icon.setMaxWidth(25);
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
		name.setMaxWidth(250);
		table.getColumns().addAll(id, icon, name);

		ScrollPane scroll = new ScrollPane(table);
		scroll.setHbarPolicy(ScrollBarPolicy.NEVER);
		scroll.setFitToWidth(true);
		VBox.setVgrow(scroll, Priority.ALWAYS);

		VBox v = new VBox(15);
		v.getChildren().addAll(toggleBox, filterBox, scroll);

		Label error = new Label();
		error.setMaxHeight(0);
		error.getStyleClass().add("red");

		Button ok = new Button(MsgUtil.getMessage("button.ok"));
		ok.getStyleClass().add("blue-btn");
		ok.setMinSize(390, 40);
		ok.setPrefSize(390, 40);
		ok.setOnAction(e -> {
			Row selected = table.getSelectedRow();
			if (selected == null) {
				error.setText(MsgUtil.getMessage("alert.warning.must.select", MsgUtil.getMessage("preparation.library.resource")));
				error.setMaxHeight(30);
				return;
			}
			dialog.setResult(selected.getItems().getInteger("id"));
		});

		VBox.setVgrow(v, Priority.ALWAYS);
		box.getChildren().addAll(v, error, ok);
		getChildren().add(box);

		group.selectToggle(sys);
	}

	private void reload() {
		List<Resource> resources = SpringUtil.getBean(ResourceAction.class).findResourcesByCreator(types, search.getText(), (Integer) group.getSelectedToggle().getUserData());
		JSONArray array = new JSONArray();
		array.addAll(resources);
		table.setItems(array);
		table.build();
	}
}

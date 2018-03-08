package com.cas.sim.tis.view.control.imp.preparation;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.cas.sim.tis.action.LibraryAction;
import com.cas.sim.tis.consts.LibraryType;
import com.cas.sim.tis.entity.Library;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.control.imp.SearchBox;
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
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class LibrarySelectDialog extends DialogPane<Integer> {

	private ToggleGroup group = new ToggleGroup();
	private SearchBox search;
	private Table table;

	public LibrarySelectDialog() {
		VBox box = new VBox(10);
		VBox.setVgrow(box, Priority.ALWAYS);
		box.setAlignment(Pos.TOP_CENTER);
		box.setPadding(new Insets(20));

		HBox toggleBox = new HBox(10);
		for (LibraryType libraryType : LibraryType.values()) {
			ToggleButton toggle = new ToggleButton(libraryType.getKey());
			toggle.setMinSize(100, 40);
			toggle.setStyle("-fx-font-size:14px");
			toggle.setUserData(libraryType.getType());
			toggleBox.getChildren().add(toggle);
			group.getToggles().add(toggle);
		}
		group.selectedToggleProperty().addListener((b, o, n) -> {
			if (n == null) {
				group.selectToggle(o);
			} else {
				reload();
			}
		});
		search = new SearchBox();
		search.setOnSearch(event -> {
			reload();
		});
		HBox searchBox = new HBox();
		searchBox.getChildren().add(search);
		searchBox.setAlignment(Pos.CENTER_RIGHT);
		HBox.setHgrow(searchBox, Priority.ALWAYS);

		toggleBox.getChildren().add(searchBox);

		table = new Table("table-row", "table-row-hover", "table-row-selected");
		table.setSerial(true);
		table.setRowHeight(45);
		table.setSeparatorable(false);
		table.setRowsSpacing(1);
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
				error.setText(MsgUtil.getMessage("alert.warning.must.select", MsgUtil.getMessage("library.name")));
				return;
			}
			dialog.setResult(selected.getItems().getInteger("id"));
		});

		box.getChildren().addAll(toggleBox, scroll, error, ok);
		getChildren().add(box);

		group.selectToggle(group.getToggles().get(0));

	}

	private void reload() {
		List<Library> libraries = SpringUtil.getBean(LibraryAction.class).findLibraryByType((int) group.getSelectedToggle().getUserData(), search.getText());
		JSONArray array = new JSONArray();
		array.addAll(libraries);
		table.setItems(array);
		table.build();
	}
}

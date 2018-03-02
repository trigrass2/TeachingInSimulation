package com.cas.sim.tis.view.control.imp.preparation;

import java.util.List;

import com.cas.sim.tis.entity.Catalog;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.view.control.imp.dialog.DialogPane;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class CatalogSelectDialog extends DialogPane<Catalog> {
	private ToggleGroup group = new ToggleGroup();
	
	public CatalogSelectDialog(List<Catalog> catalogs) {
		FlowPane flow = new FlowPane();
		flow.setHgap(15);
		flow.setVgap(15);
		flow.setPrefWrapLength(600);
		flow.setAlignment(Pos.TOP_CENTER);

		for (Catalog catalog : catalogs) {
			flow.getChildren().add(createSelectItem(catalog));
		}

		ScrollPane scroll = new ScrollPane(flow);
		scroll.setHbarPolicy(ScrollBarPolicy.NEVER);
		scroll.setFitToWidth(true);
		scroll.setPadding(new Insets(10));
		VBox.setVgrow(scroll, Priority.ALWAYS);

		Label error = new Label();
		error.getStyleClass().add("red");

		Button ok = new Button(MsgUtil.getMessage("button.ok"));
		ok.getStyleClass().add("blue-btn");
		ok.setPrefSize(390, 40);
		ok.setOnAction(e -> {
			Toggle toggle = group.getSelectedToggle();
			if (toggle == null) {
				error.setText(MsgUtil.getMessage("class.select.error"));
				return;
			}
			Catalog catalog = (Catalog) toggle.getUserData();
			dialog.setResult(catalog);
		});

		this.setSpacing(20);
		this.setPadding(new Insets(0, 0, 20, 0));
		this.setAlignment(Pos.CENTER);
		this.getChildren().addAll(scroll, error, ok);
	}

	private ToggleButton createSelectItem(Catalog catalog) {
		ToggleButton toggle = new ToggleButton(catalog.getName());
		toggle.getStyleClass().add("class-select-item");
		toggle.setUserData(catalog);
		toggle.setWrapText(true);
		group.getToggles().add(toggle);
		return toggle;
	}
}

package com.cas.sim.tis.view.control.imp.preparation;

import java.util.List;
import java.util.Map;

import com.cas.circuit.consts.ElecCompType;
import com.cas.sim.tis.entity.ElecComp;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.view.control.imp.dialog.DialogPane;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class ElecCompSelectDialog extends DialogPane<Integer> {

	public ElecCompSelectDialog(Map<Integer, List<ElecComp>> elecCompMap) {
		VBox box = new VBox(25);
		VBox.setVgrow(box, Priority.ALWAYS);
		box.setAlignment(Pos.TOP_CENTER);
		box.setPadding(new Insets(20));

		ToggleGroup group = new ToggleGroup();
		Accordion accordion = new Accordion();
		accordion.getStyleClass().add("white-accordion");
		for (ElecCompType type : ElecCompType.values()) {
			VBox content = new VBox();
			content.setStyle("-fx-padding:0 0 0 20");
			for (ElecComp elecComp : elecCompMap.get(type.getType())) {
				ToggleButton button = new ToggleButton(elecComp.getName());
				button.getStyleClass().add("white-titled-content-btn");
				button.setUserData(elecComp.getId());
				group.getToggles().add(button);
				content.getChildren().add(button);
			}
			TitledPane pane = new TitledPane(type.getName(), content);
			accordion.getPanes().add(pane);
		}
		ScrollPane scroll = new ScrollPane(accordion);
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
			Toggle selected = group.getSelectedToggle();
			if (selected == null) {
				error.setText(MsgUtil.getMessage("alert.warning.must.select", MsgUtil.getMessage("preparation.elec.comp")));
				return;
			}
			dialog.setResult((Integer) selected.getUserData());
		});

		box.getChildren().addAll(scroll, error, ok);
		getChildren().add(box);
	}
}

package com.cas.sim.tis.view.control.imp.jme;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import com.cas.circuit.consts.ElecCompType;
import com.cas.sim.tis.action.ElecCompAction;
import com.cas.sim.tis.entity.ElecComp;
import com.cas.sim.tis.util.SpringUtil;

import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;

public class ElecCompTree extends Accordion {
	
	public ElecCompTree(Consumer<ElecComp> c, ToggleGroup group) {
//		查询元器件列表
		ElecCompAction elecCompAction = SpringUtil.getBean(ElecCompAction.class);

		Map<Integer, List<ElecComp>> map = elecCompAction.getElecCompMap();

		for (ElecCompType type : ElecCompType.values()) {
			TitledPane t1 = new TitledPane();
			getPanes().add(t1);
			
			t1.setText(type.getName());
			
			VBox content = new VBox();
			content.setSpacing(10);
			
			map.get(type.getType()).forEach(elecComp -> {
				ToggleButton lbl = new ToggleButton(elecComp.getName() + "(" + elecComp.getModel() + ")");
//				lbl.setPrefSize(190, 30);
				lbl.setTooltip(new Tooltip(lbl.getText()));
//				lbl.setTextFill(Color.WHITE);
//				lbl.setMaxWidth(190);
//				lbl.setPrefWidth(190);
//				lbl.setTextAlignment(TextAlignment.LEFT);
//				lbl.setAlignment(Pos.CENTER_LEFT);
//				lbl.setFont(Font.font(10));
				lbl.getStyleClass().add("titled-content-btn");
//				lbl.setStyle("-fx-padding: 0 5px 0 5px;");
//				lbl.setPadding(new Insets(0, 5, 0, 5));
				lbl.setOnMouseClicked(event -> {
					c.accept(elecComp);
				});
				content.getChildren().add(lbl);
				group.getToggles().add(lbl);
			});
			t1.setContent(content);
			
		}
	}

	public ElecCompTree(Object object) {
	}
}

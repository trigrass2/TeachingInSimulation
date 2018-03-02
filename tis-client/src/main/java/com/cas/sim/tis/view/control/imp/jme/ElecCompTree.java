package com.cas.sim.tis.view.control.imp.jme;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import com.cas.sim.tis.action.ElecCompAction;
import com.cas.sim.tis.entity.ElecComp;
import com.cas.sim.tis.util.SpringUtil;

import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

public class ElecCompTree extends Accordion {
	public ElecCompTree(Consumer<ElecComp> c) {
//		查询元器件列表
		ElecCompAction elecCompAction = SpringUtil.getBean(ElecCompAction.class);

		Map<String, List<ElecComp>> map = elecCompAction.getElecCompMap();

		map.entrySet().forEach(entry -> {
//			
			TitledPane t1 = new TitledPane();
			getPanes().add(t1);

			t1.setText(entry.getKey());

			VBox content = new VBox();
			content.setSpacing(5);

			entry.getValue().forEach(elecComp -> {
				Button lbl = new Button(elecComp.getName() + "(" + elecComp.getModel() + ")");
				lbl.setPrefSize(190, 30);
//				lbl.setMaxWidth(190);
//				lbl.setPrefWidth(190);
				lbl.setTextAlignment(TextAlignment.LEFT);
//				lbl.setFont(Font.font(10));
				lbl.setOnMouseClicked(event -> {
					c.accept(elecComp);
				});
				content.getChildren().add(lbl);
			});
			t1.setContent(content);
		});
	}

	public ElecCompTree(Object object) {
	}
}

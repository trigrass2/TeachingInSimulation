package com.cas.sim.tis.view.control.imp.jme;

import java.util.List;
import java.util.Map;

import com.cas.sim.tis.entity.ElecComp;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.action.ElecCompAction;
import com.cas.sim.tis.view.control.ILeftContent;

import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class RecongnizeMenu implements ILeftContent {

	private Recongnize3D recongnize3D;

	public RecongnizeMenu(Recongnize3D recongnize3D) {
		this.recongnize3D = recongnize3D;
	}

	@Override
	public Region getLeftContent() {
//		查询元器件列表
		ElecCompAction elecCompAction = SpringUtil.getBean(ElecCompAction.class);
		Map<String, List<ElecComp>> map = elecCompAction.getElecCompMap();

		Accordion accordion = new Accordion();
		map.entrySet().forEach(entry -> {
//			
			TitledPane t1 = new TitledPane();
			accordion.getPanes().add(t1);

			t1.setText(entry.getKey());
			
			VBox content = new VBox();
			content.setSpacing(5);
			
			entry.getValue().forEach(e -> {
				Button lbl = new Button(e.getName() + "(" + e.getModel() + ")");
				lbl.setPrefSize(190, 30);
//				lbl.setMaxWidth(190);
//				lbl.setPrefWidth(190);
				lbl.setTextAlignment(TextAlignment.LEFT);
//				lbl.setFont(Font.font(10));
				lbl.setOnMouseClicked(event -> {
					recongnize3D.setElecComp(e);
				});
				content.getChildren().add(lbl);
			});
			t1.setContent(content);
		});
		return accordion;
	}
}

package com.cas.sim.tis.view.control.imp.jme;

import java.util.List;
import java.util.Map;

import com.cas.sim.tis.entity.ElecComp;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.action.ElecCompAction;
import com.cas.sim.tis.view.control.ILeftContent;

import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class TypicalCaseMenu implements ILeftContent {

	private TypicalCase3D typicalCase3D;

	public TypicalCaseMenu(TypicalCase3D typicalCase3D) {
		this.typicalCase3D = typicalCase3D;
	}

	@Override
	public Region getLeftContent() {
		VBox vb = new VBox();

		Button create = new Button(MsgUtil.getMessage("menu.button.new"));
		Button open = new Button(MsgUtil.getMessage("menu.button.open"));
		Button save = new Button(MsgUtil.getMessage("menu.button.save"));
		HBox menu = new HBox(15, create, open, save);

//		菜单
		vb.getChildren().add(menu);

//		查询元器件列表
		ElecCompAction elecCompAction = SpringUtil.getBean(ElecCompAction.class);
		Map<String, List<ElecComp>> map = elecCompAction.getElecCompMap();
		Accordion accordion = new Accordion();
		map.entrySet().forEach(entry -> {
//			
			TitledPane t1 = new TitledPane();
			t1.setLineSpacing(15);
			accordion.getPanes().add(t1);

			t1.setText(entry.getKey());
			VBox content = new VBox();
			entry.getValue().forEach(e -> {
				Label lbl = new Label(e.getName() + "(" + e.getModel() + ")");
				lbl.setOnMouseClicked(event -> {
//					TODO
				});
				content.getChildren().add(lbl);
			});
			t1.setContent(content);
		});
		vb.getChildren().add(accordion);

		return vb;
	}
}

package com.cas.sim.tis.view.control.imp.jme;

import java.util.List;

import com.cas.sim.tis.action.TypicalCaseAction;
import com.cas.sim.tis.entity.TypicalCase;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.control.ILeftContent;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

//		查询典型案例列表
		TypicalCaseAction typicalCaseAction = SpringUtil.getBean(TypicalCaseAction.class);
		List<TypicalCase> typicalCaseList = typicalCaseAction.getTypicalCaseList();
		VBox content = new VBox();
		typicalCaseList.forEach(typicalCase -> {
			Label lbl = new Label(typicalCase.getName());
			lbl.setOnMouseClicked(event -> {
				typicalCase3D.setCase(typicalCase);
			});
			content.getChildren().add(lbl);
		});

		vb.getChildren().add(content);

		return content;
	}
}

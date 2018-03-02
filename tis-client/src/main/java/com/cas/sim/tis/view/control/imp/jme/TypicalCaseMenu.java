package com.cas.sim.tis.view.control.imp.jme;

import com.cas.sim.tis.action.TypicalCaseAction;
import com.cas.sim.tis.entity.ElecComp;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.control.ILeftContent;
import com.cas.sim.tis.view.control.imp.dialog.Dialog;
import com.cas.sim.tis.view.control.imp.dialog.DialogPane;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
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
		open.setOnAction(e -> showCaseDialog());
		Button save = new Button(MsgUtil.getMessage("menu.button.save"));
		HBox menu = new HBox(15, create, open, save);

//		菜单
		vb.getChildren().add(menu);

		vb.getChildren().add(new ElecCompTree(elecComp -> typicalCase3D.selectedElecComp(elecComp)));

		return vb;
	}

	//open打开案例选择面板
	private void showCaseDialog() {
		Dialog<ElecComp> dialog = new Dialog<>();
		DialogPane<ElecComp> dialogPane = new DialogPane<>();
//		dialog.title.case-list
		dialogPane.setTitle("dialog.title.case-list");
		dialogPane.getChildren().add(createCaseListUI());
		dialog.setDialogPane(dialogPane);
		dialog.setPrefSize(300, 400);
		dialog.showAndWait();
	}

	private Node createCaseListUI() {
		VBox vb = new VBox(15);
		ScrollPane sp = new ScrollPane(vb);

		TypicalCaseAction action = SpringUtil.getBean(TypicalCaseAction.class);
		action.getTypicalCaseList().forEach(c -> {
			Button item = new Button(c.getName());
			item.setOnMouseClicked(e -> typicalCase3D.setupCase(c));
			vb.getChildren().add(item);
		});
		for (int i = 0; i < 20; i++) {
			Button item = new Button("Test-" + i);
			vb.getChildren().add(item);
		}

		return sp;
	}
}

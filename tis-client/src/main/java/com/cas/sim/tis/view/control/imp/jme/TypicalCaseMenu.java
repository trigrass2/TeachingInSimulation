package com.cas.sim.tis.view.control.imp.jme;

import java.util.Optional;

import com.cas.sim.tis.action.TypicalCaseAction;
import com.cas.sim.tis.entity.TypicalCase;
import com.cas.sim.tis.svg.SVGGlyph;
import com.cas.sim.tis.util.AlertUtil;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.control.ILeftContent;
import com.cas.sim.tis.view.control.imp.dialog.Dialog;
import com.cas.sim.tis.view.control.imp.preparation.TypicalCaseSelectDialog;
import com.cas.sim.tis.view.controller.PageController;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class TypicalCaseMenu implements ILeftContent {

	private TypicalCase3D typicalCase3D;

	public TypicalCaseMenu(TypicalCase3D typicalCase3D) {
		this.typicalCase3D = typicalCase3D;
	}

	@Override
	public Region getLeftContent() {
		VBox vb = new VBox(10);
		Button create = createMenu(MsgUtil.getMessage("menu.button.new"), new SVGGlyph("iconfont.svg.new", Color.WHITE, 32));
		create.setOnMouseClicked(e -> newCase());

		Button open = createMenu(MsgUtil.getMessage("menu.button.open"), new SVGGlyph("iconfont.svg.open", Color.WHITE, 32));
		open.setOnMouseClicked(e -> showCaseDialog());

		Button save = createMenu(MsgUtil.getMessage("menu.button.save"), new SVGGlyph("iconfont.svg.save", Color.WHITE, 32));
		save.setOnMouseClicked(e -> saveCase());

		HBox menu = new HBox(22, create, open, save);
		menu.setAlignment(Pos.CENTER);
//		菜单
		vb.getChildren().add(menu);

		vb.getChildren().add(new ElecCompTree(elecComp -> typicalCase3D.selectedElecComp(elecComp)));

		return vb;
	}

	private Button createMenu(String text, Node graphic) {
		Button btn = new Button(text, graphic);
		btn.setContentDisplay(ContentDisplay.TOP);
		btn.setPrefSize(45, 45);
		btn.setAlignment(Pos.CENTER);
		btn.getStyleClass().add("img-btn");
		btn.setTextFill(Color.WHITE);
		btn.setFont(new Font(14));
		return btn;
	}

	// open打开案例选择面板
	private void showCaseDialog() {
		Dialog<Integer> dialog = new Dialog<>();
		dialog.setDialogPane(new TypicalCaseSelectDialog(true));
		dialog.setTitle(MsgUtil.getMessage("typical.case.title.list"));
		dialog.setPrefSize(640, 500);
		dialog.showAndWait().ifPresent(id -> {
			if (id == null) {
				return;
			}
			SpringUtil.getBean(PageController.class).showLoading();
			TypicalCaseAction action = SpringUtil.getBean(TypicalCaseAction.class);
			TypicalCase typicalCase = action.findTypicalCaseById(id);
			typicalCase3D.setupCase(typicalCase);
		});
	}

	private void newCase() {
		SpringUtil.getBean(PageController.class).showLoading();
		TypicalCase typicalCase = new TypicalCase();
		typicalCase3D.setupCase(typicalCase);
	}

	private void saveCase() {
		SpringUtil.getBean(PageController.class).showLoading();
		TypicalCase typicalCase = typicalCase3D.getTypicalCase();
		if (typicalCase.getId() == null) {
			TextInputDialog steamIdDialog = new TextInputDialog();
			steamIdDialog.setTitle(MsgUtil.getMessage("menu.button.save"));
			steamIdDialog.setHeaderText(null);
			steamIdDialog.setContentText(MsgUtil.getMessage("typical.case.prompt.input.case"));
			Optional<String> steamID = steamIdDialog.showAndWait();
			if (steamID.isPresent()) {
				typicalCase.setName(steamID.get());
			}
		}
		typicalCase3D.save();
		AlertUtil.showAlert(AlertType.INFORMATION, MsgUtil.getMessage("alert.information.data.save.success"));
	}

}

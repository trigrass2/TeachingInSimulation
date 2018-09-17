package com.cas.sim.tis.view.control.imp.jme;

import java.util.Optional;

import com.cas.sim.tis.action.TypicalCaseAction;
import com.cas.sim.tis.consts.RoleConst;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.TypicalCase;
import com.cas.sim.tis.svg.SVGGlyph;
import com.cas.sim.tis.util.AlertUtil;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.control.ILeftContent;
import com.cas.sim.tis.view.control.imp.dialog.Dialog;
import com.cas.sim.tis.view.control.imp.dialog.Tip.TipType;
import com.cas.sim.tis.view.control.imp.preparation.TypicalCaseSelectDialog;
import com.cas.sim.tis.view.controller.PageController;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class TypicalCaseMenu implements ILeftContent {

	private ToggleGroup group = new ToggleGroup();

	private TypicalCase3D typicalCase3D;

	public TypicalCaseMenu(TypicalCase3D typicalCase3D) {
		this.typicalCase3D = typicalCase3D;
		this.group.selectedToggleProperty().addListener((b, o, n) -> {
			if (n == null) {
				this.group.selectToggle(o);
			}
		});
	}

	@Override
	public Region getLeftContent() {
		VBox vb = new VBox(10);

		HBox menu = new HBox(22);

		Button open = createMenu(MsgUtil.getMessage("menu.button.open"), new SVGGlyph("iconfont.svg.open", Color.WHITE, 24));
		open.setOnMouseClicked(e -> showCaseDialog());

		int role = Session.get(Session.KEY_LOGIN_ROLE, RoleConst.STUDENT);
		if (role == RoleConst.STUDENT) {
			menu.setAlignment(Pos.CENTER_LEFT);
			menu.getChildren().add(open);
		} else {
			menu.setAlignment(Pos.CENTER);
			Button create = createMenu(MsgUtil.getMessage("menu.button.new"), new SVGGlyph("iconfont.svg.new", Color.WHITE, 24));
			create.setOnMouseClicked(e -> newCase());

			Button save = createMenu(MsgUtil.getMessage("menu.button.save"), new SVGGlyph("iconfont.svg.save", Color.WHITE, 24));
			save.setOnMouseClicked(e -> saveCase());

			menu.getChildren().addAll(create, open, save);
		}

//		菜单
		vb.getChildren().add(menu);

		vb.getChildren().add(new ElecCompTree(elecComp -> typicalCase3D.selectedElecComp(elecComp), group));

		return vb;
	}

	private Button createMenu(String text, Node graphic) {
		Button btn = new Button(text, graphic);
		btn.setContentDisplay(ContentDisplay.TOP);
		btn.getStyleClass().add("img-btn");
		btn.setTextFill(Color.WHITE);
		btn.setTextAlignment(TextAlignment.JUSTIFY);
		btn.setFont(new Font(12));
		return btn;
	}

	// open打开案例选择面板
	private void showCaseDialog() {
		Dialog<Integer> dialog = new Dialog<>();
		int role = Session.get(Session.KEY_LOGIN_ROLE, RoleConst.STUDENT);
		dialog.setDialogPane(new TypicalCaseSelectDialog(role != RoleConst.STUDENT, role));
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
		// 0、判断当前是否有接线存在
		if (typicalCase3D.isClean()) {
			TypicalCase typicalCase = new TypicalCase();
			typicalCase3D.setupCase(typicalCase);
		} else {
			AlertUtil.showConfirm(MsgUtil.getMessage("typical.case.not.be.clean"), resp -> {
				if (resp == ButtonType.YES) {
					TypicalCase typicalCase = new TypicalCase();
					typicalCase3D.setupCase(typicalCase);
				}
			});
		}
	}

	private void saveCase() {
//		显示等待界面
		SpringUtil.getBean(PageController.class).showLoading();
		try {
			TypicalCase typicalCase = typicalCase3D.getTypicalCase();
//			如果该案例没有ID，则表明是新增的案例，此时需要用户提供一个案例名称
			if (typicalCase.getId() == null) {
//				创建一个输入对话框，让用户填写案例名称
				TextInputDialog steamIdDialog = new TextInputDialog();
				steamIdDialog.setTitle(MsgUtil.getMessage("menu.button.save"));
				steamIdDialog.setHeaderText(null);
				steamIdDialog.setContentText(MsgUtil.getMessage("typical.case.prompt.input.case"));
				Optional<String> steamID = steamIdDialog.showAndWait();
//				FIXME 用户输入了一个已经存在的案例名称当如何处理
				if (!steamID.isPresent()) {
					return;
				}
				typicalCase.setName(steamID.get());
			}
			typicalCase3D.save();

			AlertUtil.showTip(TipType.INFO, MsgUtil.getMessage("alert.information.data.save.success"));
		} finally {
//			保存完成，关闭等待界面
			Platform.runLater(() -> SpringUtil.getBean(PageController.class).hideLoading());
		}
	}

}

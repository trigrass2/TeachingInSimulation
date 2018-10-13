package com.cas.sim.tis.view.control.imp;

import com.cas.sim.tis.consts.RoleConst;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.svg.SVGGlyph;
import com.cas.sim.tis.util.MsgUtil;
import com.cas.sim.tis.view.control.ILeftContent;
import com.cas.sim.tis.view.control.imp.jme.ElecCompTree;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public abstract class ElecCaseMenu implements ILeftContent {

	private Label name = new Label();
	private ToggleGroup group = new ToggleGroup();

	protected ElecCase3D<?> elecCase3D;

	public ElecCaseMenu(ElecCase3D<?> elecCase3D) {
		this.elecCase3D = elecCase3D;
		this.group.selectedToggleProperty().addListener((b, o, n) -> {
			if (n == null) {
				this.group.selectToggle(o);
			}
		});
	}

	@Override
	public Region getLeftContent() {
		VBox vb = new VBox(10);

		name.getStyleClass().add("left-menu-orange");
		name.setWrapText(true);

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
		vb.getChildren().add(name);
		vb.getChildren().add(menu);
		vb.getChildren().add(new ElecCompTree(elecComp -> elecCase3D.selectedElecComp(elecComp), group));
		vb.setPadding(new Insets(10, 0, 0, 0));
		return vb;
	}

	public void setName(String name) {
		this.name.setText(name);
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

	protected abstract void showCaseDialog();

	protected abstract void newCase();

	protected abstract void saveCase();

}

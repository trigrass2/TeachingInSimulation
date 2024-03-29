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
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public abstract class ElecCaseMenu implements ILeftContent {

	protected VBox menu = new VBox(10);
	protected HBox options = new HBox(22);
	
//	private Label name = new Label();
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
//		name.getStyleClass().add("left-menu-orange");
//		name.setWrapText(true);

		Button open = createMenu(MsgUtil.getMessage("menu.button.open"), new SVGGlyph("iconfont.svg.open", Color.WHITE, 24));
		open.setOnAction(e -> showCaseDialog());

		int role = Session.get(Session.KEY_LOGIN_ROLE, RoleConst.STUDENT);
		if (role == RoleConst.STUDENT) {
			options.setAlignment(Pos.CENTER_LEFT);
			options.getChildren().add(open);
		} else {
			options.setAlignment(Pos.CENTER);
			Button create = createMenu(MsgUtil.getMessage("menu.button.new"), new SVGGlyph("iconfont.svg.new", Color.WHITE, 24));
			create.setOnAction(e -> newCase());

			Button save = createMenu(MsgUtil.getMessage("menu.button.save"), new SVGGlyph("iconfont.svg.save", Color.WHITE, 24));
			save.setOnAction(e -> saveCase());

			options.getChildren().addAll(create, open, save);
		}

//		菜单
//		menu.getChildren().add(name);
		menu.getChildren().add(options);
		menu.getChildren().add(new ElecCompTree(elecComp -> elecCase3D.selectedElecComp(elecComp), group));
		menu.setPadding(new Insets(10, 0, 0, 0));
		return menu;
	}

//	public void setName(String name) {
//		this.name.setText(name);
//	}

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

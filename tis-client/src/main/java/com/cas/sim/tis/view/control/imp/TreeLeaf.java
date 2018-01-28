package com.cas.sim.tis.view.control.imp;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class TreeLeaf extends VBox {
	// 该节点下子节点被是否折叠
	private boolean extend;
	// 子节点
	private List<TreeLeaf> subLeafs = new ArrayList<TreeLeaf>();
	@FXML
	private HBox leaf;
	@FXML
	private Label leafName;
	@FXML
	private Tooltip tip;
	@FXML
	private Label num;
	@FXML
	private VBox children;

	public static enum Level {
		Level1("lv1"), Level2("lv2"), Level3("lv3"), Level4("lv4");

		private String styleClass;

		private Level(String styleClass) {
			setStyleClass(styleClass);
		}

		public String getStyleClass() {
			return styleClass;
		}

		public void setStyleClass(String styleClass) {
			this.styleClass = styleClass;
		}
	}

	public TreeLeaf(String leafName, Level level, boolean collapsible) {
		this(leafName, 0, level, collapsible);
	}
	public TreeLeaf(String leafName, int num, Level level, boolean collapsible) {
		FXMLLoader loader = new FXMLLoader();
		URL fxmlUrl = this.getClass().getResource("/view/TreeLeaf.fxml");
		loader.setLocation(fxmlUrl);
		loader.setController(this);
		loader.setRoot(this);
		try {
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.leafName.setText(leafName);
		this.tip.setText(leafName);
		if (level != Level.Level4) {
			this.num.setText(num + "课时");
		}
		if (collapsible) {
			leaf.setOnMouseClicked(e -> {
				// FIXME 展开加载子节点，折叠去除子节点
				if (extend) {
					this.children.getChildren().removeAll(subLeafs);
					this.layout();
					extend = false;
				} else {
					this.children.getChildren().addAll(subLeafs);
					this.layout();
					extend = true;
				}
			});
		} else {
			leaf.setOnMouseClicked(e -> {
				// FIXME 跳转
			});
		}
		this.getStyleClass().add(level.getStyleClass());
		this.layout();
	}

	// FIXME
	public void loadChildren(TreeLeaf... leafs) {
		for (TreeLeaf leaf : leafs) {
			this.subLeafs.add(leaf);
		}
	}
}

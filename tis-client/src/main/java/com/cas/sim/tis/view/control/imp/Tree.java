package com.cas.sim.tis.view.control.imp;


import com.cas.sim.tis.view.control.ILeftMenu;
import com.cas.sim.tis.view.control.imp.TreeLeaf.Level;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class Tree extends ScrollPane implements ILeftMenu {

	private VBox parent = new VBox(5);

	public Tree() {
		this.setHbarPolicy(ScrollBarPolicy.NEVER);
		this.setFitToWidth(true);
		this.setContent(parent);
		this.layout();
		// FIXME
		loadTreeData();
	}

	public void loadTreeData() {
		// FIXME
		TreeLeaf leaf1 = new TreeLeaf("电工基础", 17, Level.Level1, true);
		TreeLeaf leaf2 = new TreeLeaf("常用低压电器", 1, Level.Level2, true);
		TreeLeaf leaf3 = new TreeLeaf("低压电基本知识", 1, Level.Level3, true);
		TreeLeaf leaf4 = new TreeLeaf("外型与符号", 0, Level.Level4, false);
		TreeLeaf leaf5 = new TreeLeaf("符号及含义", 0, Level.Level4, false);
		TreeLeaf leaf6 = new TreeLeaf("认识接触器", 2, Level.Level3, true);
		TreeLeaf leaf7 = new TreeLeaf("鼓风机控制线路的安装", 3, Level.Level3, true);
		leaf1.loadChildren(leaf2);
		leaf2.loadChildren(leaf3, leaf6, leaf7);
		leaf3.loadChildren(leaf4, leaf5);
		this.parent.getChildren().addAll(leaf1);
		this.layout();
	}

	@Override
	public Region getLeftContent() {
		return this;
	}
}

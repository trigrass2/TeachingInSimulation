package com.cas.sim.tis.view.control.imp.jme;

import com.cas.sim.tis.view.control.ILeftContent;

import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class RecongnizeMenu implements ILeftContent {

	private ToggleGroup group = new ToggleGroup();

	private Recongnize3D recongnize3D;

	public RecongnizeMenu(Recongnize3D recongnize3D) {
		this.recongnize3D = recongnize3D;
		this.group.selectedToggleProperty().addListener((b, o, n) -> {
			if (n == null) {
				this.group.selectToggle(o);
			}
		});
	}

	@Override
	public Region getLeftContent() {
		VBox vb = new VBox(new ElecCompTree(comp -> recongnize3D.setElecComp(comp), group));
		return vb;
	}
}

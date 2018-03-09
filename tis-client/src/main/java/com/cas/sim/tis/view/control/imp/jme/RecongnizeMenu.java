package com.cas.sim.tis.view.control.imp.jme;

import com.cas.sim.tis.view.control.ILeftContent;

import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class RecongnizeMenu implements ILeftContent {

	private Recongnize3D recongnize3D;

	public RecongnizeMenu(Recongnize3D recongnize3D) {
		this.recongnize3D = recongnize3D;
	}

	@Override
	public Region getLeftContent() {
		VBox vb = new VBox(new ElecCompTree(comp -> recongnize3D.setElecComp(comp)));
		return vb;
	}
}

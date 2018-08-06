package com.cas.sim.tis.app.listener;

import com.cas.circuit.component.ElecCompDef;
import com.cas.circuit.util.JmeUtil;
import com.cas.sim.tis.app.event.MouseEvent;
import com.cas.sim.tis.app.event.MouseEventAdapter;
import com.cas.sim.tis.app.state.CircuitState;
import com.cas.sim.tis.app.state.SceneCameraState;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.control.IContent;
import com.cas.sim.tis.view.control.imp.jme.TypicalCase3D;
import com.cas.sim.tis.view.controller.PageController;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.Spatial.CullHint;

import javafx.application.Platform;

public class ElecCompClickListener extends MouseEventAdapter {

	private CircuitState circuit;

	private Spatial selectedComp;

	private Geometry box;

	private SceneCameraState camera;

	public ElecCompClickListener(CircuitState circuit, Geometry box, SceneCameraState camera) {
		this.circuit = circuit;
		this.box = box;
		this.camera = camera;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		clearWireSelected();

		Spatial newWireNode = e.getSpatial();
		camera.setCamFocus(newWireNode.getWorldTranslation(), true);

//		如果两次选择的模型一样，则视为取消选中
		if (selectedComp == newWireNode) {
			selectedComp = null;
			return;
		}
		box.setCullHint(CullHint.Dynamic);
		JmeUtil.updateWiringBox(box, newWireNode);

		selectedComp = newWireNode;
		circuit.clearWireSelect();
	}

	@Override
	public void mouseRightClicked(MouseEvent e) {
		IContent content = SpringUtil.getBean(PageController.class).getIContent();
		ElecCompDef def = e.getSpatial().getUserData("entity");
		if (content instanceof TypicalCase3D) {
			Platform.runLater(() -> {
				((TypicalCase3D) content).showPopupMenu(def);
			});
		}
	}

	public void clearWireSelected() {
		if (selectedComp != null) {
//			如果有一根导线A被选中了，先将导线A的选中效果取消
			box.setCullHint(CullHint.Always);
			selectedComp = null;
		}
	}

	public ElecCompDef getSeletedElecComp() {
		if (selectedComp == null) {
			return null;
		} else {
			return selectedComp.getUserData("entity");
		}
	}
}

package com.cas.sim.tis.app.listener;

import com.cas.circuit.vo.ElecCompDef;
import com.cas.sim.tis.Application;
import com.cas.sim.tis.app.control.HintControl;
import com.cas.sim.tis.app.event.MouseEvent;
import com.cas.sim.tis.app.event.MouseEventAdapter;
import com.cas.sim.tis.app.state.CircuitState;
import com.cas.sim.tis.app.state.SceneCameraState;
import com.cas.sim.tis.util.JmeUtil;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.control.IContent;
import com.cas.sim.tis.view.control.imp.jme.TypicalCase3D;
import com.cas.sim.tis.view.controller.PageController;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.scene.shape.Box;

import javafx.application.Platform;

public class ElecCompClickListener extends MouseEventAdapter {

	private CircuitState circuit;

	private Spatial selectedComp;

	private Geometry box;

	private SceneCameraState camera;

	public ElecCompClickListener(Geometry box, SceneCameraState camera) {
		this.box = box;
		this.camera = camera;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (selectedComp != null) {
//			如果有一根导线A被选中了，先将导线A的选中效果取消
			box.setCullHint(CullHint.Always);
		}

		Spatial newWireNode = e.getSpatial();
		camera.setCamFocus(newWireNode.getLocalTranslation(), true);

//		如果两次选择的模型一样，则视为取消选中
		if (selectedComp == newWireNode) {
			selectedComp = null;
			return;
		}
		box.setCullHint(CullHint.Dynamic);
		JmeUtil.updateWiringBox(box, newWireNode);

		selectedComp = newWireNode;
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
}

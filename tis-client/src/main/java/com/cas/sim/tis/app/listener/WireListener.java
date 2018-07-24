package com.cas.sim.tis.app.listener;

import com.cas.circuit.component.Wire;
import com.cas.sim.tis.app.control.HintControl;
import com.cas.sim.tis.app.event.MouseEvent;
import com.cas.sim.tis.app.event.MouseEventAdapter;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.control.IContent;
import com.cas.sim.tis.view.control.imp.jme.TypicalCase3D;
import com.cas.sim.tis.view.controller.PageController;
import com.jme3.scene.Spatial;

import javafx.application.Platform;

public class WireListener extends MouseEventAdapter {

	private Spatial selectedWire;

	@Override
	public void mouseClicked(MouseEvent e) {
		if (selectedWire != null) {
//			如果有一根导线A被选中了，先将导线A的选中效果取消
			HintControl control = selectedWire.getControl(HintControl.class);
			control.setEnabled(false);
		}
		Spatial newWireNode = e.getSpatial();
//		如果两次选择的模型一样，则视为取消选中
		if (selectedWire == newWireNode) {
			selectedWire = null;
			return;
		}

		HintControl control = newWireNode.getControl(HintControl.class);
		if (control == null) {
			control = new HintControl();
			newWireNode.addControl(control);
		}

		control.setEnabled(true);
		selectedWire = newWireNode;
	}

	@Override
	public void mouseRightClicked(MouseEvent e) {
		// 选中显示导线菜单
		IContent content = SpringUtil.getBean(PageController.class).getIContent();
		Wire wire = e.getSpatial().getUserData("entity");
		if (content instanceof TypicalCase3D) {
			Platform.runLater(() -> {
				((TypicalCase3D) content).showPopupMenu(wire);
			});
		}
	}

	public Spatial getSelectedWire() {
		return selectedWire;
	}

	public void setSelectedWire(Spatial selectedWire) {
		this.selectedWire = selectedWire;
	}

}

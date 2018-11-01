package com.cas.sim.tis.app.listener;

import com.cas.circuit.component.Wire;
import com.cas.sim.tis.app.control.HintControl;
import com.cas.sim.tis.app.event.MouseEvent;
import com.cas.sim.tis.app.event.MouseEventAdapter;
import com.cas.sim.tis.app.state.typical.CircuitState;
import com.cas.sim.tis.consts.WireColor;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.view.control.IContent;
import com.cas.sim.tis.view.control.imp.ElecCase3D;
import com.cas.sim.tis.view.controller.PageController;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Spatial;

import javafx.application.Platform;

public class WireListener extends MouseEventAdapter {

	private Spatial selectedWire;

	private CircuitState circuitState;

	private boolean rightClickDisable;

	public WireListener(CircuitState circuitState) {
		this.circuitState = circuitState;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		clearWireSelected();

		Spatial newWireNode = e.getSpatial();
//		如果两次选择的模型一样，则视为取消选中
		if (selectedWire == newWireNode) {
			selectedWire = null;
			return;
		}
		
		Wire wire = newWireNode.getUserData("entity");

		HintControl control = newWireNode.getControl(HintControl.class);
		if (control == null) {
			WireColor color = WireColor.getWireColorByKey(wire.getProxy().getColor());
			control = new HintControl(color.getColorRGBA());
			newWireNode.addControl(control);
		}

		control.setEnabled(true);
		selectedWire = newWireNode;
		circuitState.clearElecCompSelect();
	}

	@Override
	public void mouseRightClicked(MouseEvent e) {
		if (rightClickDisable) {
			return;
		}
		// 选中显示导线菜单
		IContent content = SpringUtil.getBean(PageController.class).getIContent();
		Wire wire = e.getSpatial().getUserData("entity");
		if (content instanceof ElecCase3D<?>) {
			Platform.runLater(() -> {
				((ElecCase3D<?>) content).showPopupMenu(wire);
			});
		}
	}

	public void setRightClickDisable(boolean rightClickDisable) {
		this.rightClickDisable = rightClickDisable;
	}
	
	public Spatial getSelectedWire() {
		return selectedWire;
	}

	public void setSelectedWire(Spatial selectedWire) {
		this.selectedWire = selectedWire;
	}

	public void clearWireSelected() {
		if (selectedWire != null) {
//			如果有一根导线A被选中了，先将导线A的选中效果取消
			HintControl control = selectedWire.getControl(HintControl.class);
			control.setEnabled(false);
			selectedWire = null;
		}
	}

	public Wire getSeletedWire() {
		if (selectedWire == null) {
			return null;
		} else {
			return selectedWire.getUserData("entity");
		}
	}
}

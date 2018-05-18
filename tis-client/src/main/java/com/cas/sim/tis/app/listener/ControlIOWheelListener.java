package com.cas.sim.tis.app.listener;

import com.cas.circuit.vo.ControlIO;
import com.cas.sim.tis.app.event.MouseEvent;
import com.cas.sim.tis.app.event.MouseEventAdapter;
import com.cas.sim.tis.app.state.CircuitState;

public class ControlIOWheelListener extends MouseEventAdapter {
	private CircuitState circuit;

	public ControlIOWheelListener(CircuitState circuit) {
		this.circuit = circuit;
	}

	@Override
	public void mouseWheel(MouseEvent e) {
		ControlIO c = e.getSpatial().getUserData("entity");
		c.getSwitchCtrl().switchStateChanged(e.getWheel());
		c.playMotion();
	}
}

package com.cas.sim.tis.app.listener;

import com.cas.circuit.vo.ControlIO;
import com.cas.sim.tis.app.event.MouseEvent;
import com.cas.sim.tis.app.event.MouseEventAdapter;
import com.cas.sim.tis.app.state.CircuitState;

public class ControlIOClickListener extends MouseEventAdapter {

	private CircuitState circuit;

	public ControlIOClickListener(CircuitState circuit) {
		this.circuit = circuit;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		ControlIO c = e.getSpatial().getUserData("entity");
		c.switchStateChanged(null);
		c.playMotion();
	}

}

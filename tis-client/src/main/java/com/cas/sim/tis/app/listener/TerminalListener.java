package com.cas.sim.tis.app.listener;

import com.cas.circuit.component.Terminal;
import com.cas.sim.tis.app.event.MouseEvent;
import com.cas.sim.tis.app.event.MouseEventAdapter;
import com.cas.sim.tis.app.hold.HoldStatePro;
import com.cas.sim.tis.app.state.typical.CircuitState;
import com.cas.sim.tis.circuit.MeterPen;
import com.jme3.scene.Spatial;

public class TerminalListener extends MouseEventAdapter {
	private CircuitState circuit;

	public TerminalListener(CircuitState circuit) {
		this.circuit = circuit;
	}

	@Override
	public void mouseClicked(MouseEvent e) {

		Spatial spatial = HoldStatePro.ins.getHoldingSpatial();
		if (spatial != null) {
			HoldStatePro.ins.putDownOn(e.getSpatial());
		} else {
			Terminal terminal = e.getSpatial().getUserData("entity");
			circuit.onTernialClick(terminal);
		}
	}
}

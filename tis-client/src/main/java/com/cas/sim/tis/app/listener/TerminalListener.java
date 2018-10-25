package com.cas.sim.tis.app.listener;

import com.cas.circuit.component.Terminal;
import com.cas.sim.tis.app.event.MouseEvent;
import com.cas.sim.tis.app.event.MouseEventAdapter;
import com.cas.sim.tis.app.state.typical.CircuitState;
import com.cas.sim.tis.circuit.MeterPen;

public class TerminalListener extends MouseEventAdapter {
	private CircuitState circuit;

	public TerminalListener(CircuitState circuit) {
		this.circuit = circuit;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Terminal terminal = e.getSpatial().getUserData("entity");
//		if(MeterPen.BLACK.getDetected() == null) {
//			MeterPen.BLACK.connect(terminal);
//		}else {
//			MeterPen.RED.connect(terminal);
//		}
		if (MeterPen.RED.getDetected() == null) {
			MeterPen.RED.connect(terminal);
		} else {
			MeterPen.BLACK.connect(terminal);
		}
//		circuit.onTernialClick(terminal);
	}
}

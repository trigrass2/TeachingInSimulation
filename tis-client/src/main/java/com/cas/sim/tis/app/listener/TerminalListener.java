package com.cas.sim.tis.app.listener;

import com.cas.circuit.vo.Terminal;
import com.cas.sim.tis.app.event.MouseEvent;
import com.cas.sim.tis.app.event.MouseEventAdapter;
import com.cas.sim.tis.app.state.CircuitState;

public class TerminalListener extends MouseEventAdapter {
	private CircuitState circuit;

	public TerminalListener(CircuitState circuit) {
		this.circuit = circuit;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Terminal t = e.getSpatial().getUserData("entity");

		circuit.onTernialClick(t);
	}

}

package com.cas.sim.tis.app.listener;

import com.cas.circuit.vo.Terminal;
import com.cas.sim.tis.app.event.MouseEvent;
import com.cas.sim.tis.app.event.MouseEventAdapter;

public class TypicalCaseTermianlListener extends MouseEventAdapter {
	private Terminal terminal;

	public TypicalCaseTermianlListener(Terminal terminal) {
		this.terminal = terminal;
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}
}

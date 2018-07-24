package com.cas.sim.tis.app.listener;

import com.cas.circuit.component.ElecCompDef;
import com.cas.sim.tis.app.event.MouseEvent;
import com.cas.sim.tis.app.event.MouseEventAdapter;

public class TypicalCaseCompListener extends MouseEventAdapter {
	private ElecCompDef comp;

	public TypicalCaseCompListener(ElecCompDef comp) {
		this.comp = comp;
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}
}

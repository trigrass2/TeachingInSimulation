package com.cas.sim.tis.app.listener;

import com.cas.circuit.component.ElecCompDef;
import com.cas.sim.tis.app.event.MouseEvent;
import com.cas.sim.tis.app.event.MouseEventAdapter;
import com.cas.sim.tis.app.state.typical.TypicalCaseState;

public class ElecCompBaseClickListener extends MouseEventAdapter {

	private TypicalCaseState caseState;

	public ElecCompBaseClickListener(TypicalCaseState caseState) {
		this.caseState = caseState;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		ElecCompDef def = e.getSpatial().getUserData("entity");
		caseState.putDownOnBase(def);
	}
}

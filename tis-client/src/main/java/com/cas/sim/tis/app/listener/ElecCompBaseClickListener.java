package com.cas.sim.tis.app.listener;

import com.cas.sim.tis.app.event.MouseEvent;
import com.cas.sim.tis.app.event.MouseEventAdapter;
import com.cas.sim.tis.app.state.ElecCaseState;

public class ElecCompBaseClickListener extends MouseEventAdapter {

	private ElecCaseState<?> caseState;

	public ElecCompBaseClickListener(ElecCaseState<?> caseState) {
		this.caseState = caseState;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		caseState.putDownOnBase(e.getSpatial());
	}
}

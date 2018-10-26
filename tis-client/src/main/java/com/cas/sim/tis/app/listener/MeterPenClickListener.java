package com.cas.sim.tis.app.listener;

import com.cas.sim.tis.app.event.MouseEvent;
import com.cas.sim.tis.app.event.MouseEventAdapter;
import com.cas.sim.tis.app.hold.HoldStatePro;
import com.cas.sim.tis.circuit.MeterPen;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MeterPenClickListener extends MouseEventAdapter {
	private MeterPen pen;

	@Override
	public void mouseClicked(MouseEvent e) {
		pen.disconnect();

		HoldStatePro.ins.discard();
		HoldStatePro.ins.pickUp(e.getSpatial());
	}
}

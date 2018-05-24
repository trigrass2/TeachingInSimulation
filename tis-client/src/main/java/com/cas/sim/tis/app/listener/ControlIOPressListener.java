package com.cas.sim.tis.app.listener;

import com.cas.circuit.vo.ControlIO;
import com.cas.sim.tis.app.event.MouseEvent;
import com.cas.sim.tis.app.event.MouseEventAdapter;

public class ControlIOPressListener extends MouseEventAdapter {

	@Override
	public void mousePressed(MouseEvent e) {
		ControlIO c = e.getSpatial().getUserData("entity");
		c.getSwitchCtrl().switchStateChanged(null);
		c.playMotion();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		ControlIO c = e.getSpatial().getUserData("entity");
		c.getSwitchCtrl().switchStateChanged(null);
		c.playMotion();
	}

}

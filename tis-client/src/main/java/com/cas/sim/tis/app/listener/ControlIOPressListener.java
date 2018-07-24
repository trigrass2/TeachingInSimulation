package com.cas.sim.tis.app.listener;

import com.cas.circuit.component.ControlIO;
import com.cas.sim.tis.app.event.MouseEvent;
import com.cas.sim.tis.app.event.MouseEventAdapter;

public class ControlIOPressListener extends MouseEventAdapter {

	@Override
	public void mousePressed(MouseEvent e) {
		ControlIO c = e.getSpatial().getUserData("entity");
		c.absorbed();
		c.getSwitchElms().forEach(s -> s.doSwitch(true));
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		ControlIO c = e.getSpatial().getUserData("entity");
		c.unstuck();
		c.getSwitchElms().forEach(s -> s.doSwitch(false));
	}

}

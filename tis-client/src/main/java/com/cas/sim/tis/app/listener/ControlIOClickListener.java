package com.cas.sim.tis.app.listener;

import com.cas.circuit.component.ControlIO;
import com.cas.sim.tis.app.event.MouseEvent;
import com.cas.sim.tis.app.event.MouseEventAdapter;

public class ControlIOClickListener extends MouseEventAdapter {

	@Override
	public void mouseClicked(MouseEvent e) {
		ControlIO c = e.getSpatial().getUserData("entity");
		c.getSwitchElms().forEach(s -> s.doSwitch(false));
	}

}

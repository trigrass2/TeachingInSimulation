package com.cas.sim.tis.app.listener;

import com.cas.sim.tis.app.event.MouseEvent;
import com.cas.sim.tis.app.event.MouseEventAdapter;

public class ControlIOWheelListener extends MouseEventAdapter {

	@Override
	public void mouseWheel(MouseEvent e) {
//		ControlIO c = e.getSpatial().getUserData("entity");
//		c.getSwitchElms().forEach(ISwitch::doSwitch);
	}
}

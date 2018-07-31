package com.cas.sim.tis.app.listener;

import java.util.Optional;

import com.cas.circuit.component.ControlIO;
import com.cas.sim.tis.app.event.MouseEvent;
import com.cas.sim.tis.app.event.MouseEventAdapter;

public class ControlIOPressListener extends MouseEventAdapter {

	@Override
	public void mousePressed(MouseEvent e) {
		ControlIO c = e.getSpatial().getUserData("entity");
		c.absorbed(); // 吸合
		c.getSwitchElms().forEach(s -> s.doSwitch(true));

		Optional.ofNullable(c.getLinkage()).ifPresent(l -> {
			l.absorbed();
			l.getSwitchElms().forEach(s -> s.doSwitch(true));
		});
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		ControlIO c = e.getSpatial().getUserData("entity");
		c.unstuck(); // 松开
		c.getSwitchElms().forEach(s -> s.doSwitch(false));

		Optional.ofNullable(c.getLinkage()).ifPresent(l -> {
			l.unstuck();
			l.getSwitchElms().forEach(s -> s.doSwitch(false));
		});
	}

}

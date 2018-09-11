package com.cas.sim.tis.app.listener;

import java.util.Optional;

import com.cas.circuit.component.ControlIO;
import com.cas.sim.tis.app.event.MouseEvent;
import com.cas.sim.tis.app.event.MouseEventAdapter;

public class ControlIOPressListener extends MouseEventAdapter {

	@Override
	public void mousePressed(MouseEvent e) {
		ControlIO c = e.getSpatial().getUserData("entity");
		c.on(); // 吸合
		c.getSwitchElms().forEach(s -> s.doSwitch(true));
		
		Optional.ofNullable(c.getLinkage()).ifPresent(l -> {
			l.on();
			l.getSwitchElms().forEach(s -> s.doSwitch(true));
		});
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		ControlIO c = e.getSpatial().getUserData("entity");
		c.off(); // 松开
		c.getSwitchElms().forEach(s -> s.doSwitch(false));

		Optional.ofNullable(c.getLinkage()).ifPresent(l -> {
			l.off();
			l.getSwitchElms().forEach(s -> s.doSwitch(false));
		});
	}

}

package com.cas.sim.tis.circuit.meter;

import com.cas.circuit.component.Terminal;
import com.cas.circuit.element.WireElm;
import com.cas.sim.tis.circuit.AbstractMeter;

/**
 * 电流表/安培表
 * @author zzy
 */
public class AmMeter extends AbstractMeter {
	private WireElm wire;

	public AmMeter(Function... modes) {
		super(modes);
		wire = new WireElm();
		wire.setPostPoint(0, new Terminal("Meter_Current_0"));
		wire.setPostPoint(1, new Terminal("Meter_Current_1"));

		elmList.add(wire);

	}

	/*
	 * 电阻两端电压降
	 */
	@Override
	public double getValue() {
		return wire.getCurrent();
	}
}

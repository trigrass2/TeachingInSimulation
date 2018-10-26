package com.cas.sim.tis.circuit.meter;

import com.cas.circuit.component.Terminal;
import com.cas.circuit.element.ResistorElm;
import com.cas.sim.tis.circuit.AbstractMeter;

/**
 * 电流表/安培表
 * @author zzy
 */
public class AmMeter extends AbstractMeter {
	private ResistorElm resistor;

	public AmMeter(Function... modes) {
		super(modes);
		resistor = new ResistorElm(1E-3);
		resistor.setPostPoint(0, new Terminal("Meter_Current_0"));
		resistor.setPostPoint(1, new Terminal("Meter_Current_1"));

		elmList.add(resistor);

	}

	/*
	 * 电阻两端电压降
	 */
	@Override
	public double getValue() {
		return resistor.getCurrent();
	}
}

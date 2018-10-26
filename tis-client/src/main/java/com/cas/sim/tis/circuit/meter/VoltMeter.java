package com.cas.sim.tis.circuit.meter;

import com.cas.circuit.component.Terminal;
import com.cas.circuit.element.ResistorElm;
import com.cas.sim.tis.circuit.AbstractMeter;

/**
 * 电压表。工作原理：并联上一个阻值很大的电阻R（100MΩ）， 实际测量的是电阻R两端的电压。
 * @author zzy
 */
public class VoltMeter extends AbstractMeter {
	private ResistorElm resistor;

	public VoltMeter(Function... modes) {
		super(modes);
		resistor = new ResistorElm(100E6); // 100MΩ
		resistor.setPostPoint(0, new Terminal("Meter_Volt_0"));
		resistor.setPostPoint(1, new Terminal("Meter_Volt_1"));

		elmList.add(resistor);
	}

	/*
	 * 电阻两端电压降
	 */
	@Override
	public double getValue() {
		if (isHold()) {
			return value;
		}
//		System.out.println(resistor.getVoltageDiff());
		double diff = resistor.getVoltageDiff();
		value = Math.max(Math.abs(diff), value);
		return value;
	}

	@Override
	public void reset() {
		value = 0;
		super.reset();
	}

}

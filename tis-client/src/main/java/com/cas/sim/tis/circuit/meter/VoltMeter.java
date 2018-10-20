package com.cas.sim.tis.circuit.meter;

import com.cas.circuit.element.ResistorElm;
import com.cas.sim.tis.circuit.AbstractMeter;

/**
 * 电压表
 * @author zzy
 */
public class VoltMeter extends AbstractMeter {

	private ResistorElm resistor;

	public VoltMeter() {
		resistor = new ResistorElm(100E6); // 100MΩ
	}

	public VoltMeter(Range[] ranges, Mode[] modes) {
		super(ranges, modes);
	}

}

package com.cas.sim.tis.circuit.meter;

import com.cas.circuit.element.WireElm;
import com.cas.sim.tis.circuit.AbstractMeter;

/**
 * 电流表/安培表
 * @author zzy
 */
public class AmMeter extends AbstractMeter {

	private WireElm wire;

	public AmMeter() {
		wire = new WireElm();
	}

	public AmMeter(Range[] ranges, Mode[] modes) {
		super(ranges, modes);
	}

}

package com.cas.sim.tis.circuit.meter;

import com.cas.sim.tis.circuit.AbstractMeter;

/**
 * 电容表
 * @author zzy
 */
public class CapacitanceMeter extends AbstractMeter {

	public CapacitanceMeter() {
		super();
	}

	public CapacitanceMeter(Function... modes) {
		super(modes);
	}

	@Override
	protected void initPen() {

	}
}

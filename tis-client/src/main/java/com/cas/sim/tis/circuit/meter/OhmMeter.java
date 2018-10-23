package com.cas.sim.tis.circuit.meter;

import com.cas.circuit.element.VoltageElm;
import com.cas.sim.tis.circuit.AbstractMeter;

/**
 * 欧姆表
 * @author zzy
 */
public class OhmMeter extends AbstractMeter {

	private VoltageElm voltage;

	public OhmMeter() {
		voltage = new VoltageElm(VoltageElm.WF_DC);
	}

	public OhmMeter(Function... modes) {
		super(modes);
	}

	@Override
	protected void initPen() {

	}

}

package com.cas.sim.tis.circuit.meter;

import com.cas.circuit.component.Terminal;
import com.cas.circuit.component.Wire;
import com.cas.circuit.element.ResistorElm;
import com.cas.circuit.element.VoltageElm;
import com.cas.sim.tis.circuit.AbstractMeter;

import lombok.Getter;

/**
 * 欧姆表
 * @author zzy
 */
public class OhmMeter extends AbstractMeter {

	private VoltageElm power;
	private ResistorElm resistor;

	@Getter
	private Terminal redTerminal;
	@Getter
	private Terminal blackTerminal;

	public OhmMeter(Function... modes) {
		super(modes);
//		相当于两节AA干电池： 电源1.5*2 = 3V , 内阻 0.5*2 = 1;
		power = new VoltageElm(VoltageElm.WF_DC);
		power.setMaxVoltage(3); //
		blackTerminal = new Terminal("Meter_power_0");
		Terminal power_1 = new Terminal("Meter_power_1");
		power.setPostPoint(0, blackTerminal);
		power.setPostPoint(1, power_1);

		resistor = new ResistorElm(1);//
		Terminal resistor_0 = new Terminal("Meter_power_resistor_0");
		redTerminal = new Terminal("Meter_power_resistor_1");
		resistor.setPostPoint(0, resistor_0);
		resistor.setPostPoint(1, redTerminal);

		Wire wire = new Wire(true);
		wire.bind(power_1);
		wire.bind(resistor_0);

		elmList.add(power);
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
		double current = power.getCurrent();
		if (current == 0) {
			value = 0;
		} else {
			double voltDiff = power.getVoltageDiff();
			value = voltDiff / current - resistor.resistance;
		}
		return value;
	}
}

package com.cas.sim.tis.circuit.meter;

import com.cas.sim.tis.circuit.Meter;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * 这个枚举是万用表的功能档位的类。列出了一个万用表中所集成的仪表。<br />
 * 同时该类也实现了Meter接口，充当仪表的代理类(静态代理)。
 */
@Slf4j
public enum Rotary implements Meter {
	/* 关闭 */
	OFF(null),
	/* 交流电压 */
	AV(new VoltMeter(new Range[] { //
			new Range(0.001E0, 4.000E0, 3, 1, "V"), //
			new Range(00.01E0, 40.00E0, 2, 1, "V"), //
			new Range(000.1E0, 400.0E0, 1, 1, "V"), //
			new Range(0001.E0, 1000.E0, 0, 1, "V"), //
	}, null)),
	/* 直流电压 */
	DV(new VoltMeter(new Range[] { //
			new Range(0.001E0, 4.000E0, 3, 1, "V"), //
			new Range(00.01E0, 40.00E0, 2, 1, "V"), //
			new Range(000.1E0, 400.0E0, 1, 1, "V"), //
			new Range(0001.E0, 1000.E0, 0, 1, "V"), //
	}, null)),
	/* 直流电压 微伏 */
	DmV(new VoltMeter(new Range[] { //
			new Range(0001.E-3, 400.0E-3, 1, 1E-3, "mV"), // 400mV
	}, null)),
	/* 欧姆档 */
	Ohms(new OhmMeter(new Range[] { //
			new Range(000.1E3, 400.0E0, 1, 1E0, " Ω"), // 400 Ω
			new Range(0.001E3, 4.000E3, 3, 1E3, "KΩ"), // 1Ω - 4.000 KΩ
			new Range(00.01E3, 40.00E3, 2, 1E3, "KΩ"), // 10Ω - 40.00 KΩ
			new Range(000.1E3, 400.0E3, 1, 1E3, "KΩ"), // 400.0 KΩ
			new Range(0.001E6, 4.000E6, 3, 1E6, "MΩ"), // 1KΩ - 4.000 MΩ
			new Range(00.01E6, 40.00E6, 2, 1E6, "MΩ"), // 10KΩ - 40.00 MΩ
	}, new Mode[] { //
			Mode.Ohm, // 默认测电阻
			Mode.Diode })),
	/* 电容 */
	C(new CapacitanceMeter(new Range[] { //
			new Range(00.01E-9, 40.00E-9, 2, 1E-9, "nF"), // 40.00 nF
			new Range(000.1E-9, 400.0E-9, 1, 1E-9, "nF"), // 400.0 nF
			new Range(0.001E-6, 4.000E-6, 3, 1E-6, "μF"), // 4.000 μF
			new Range(00.01E-6, 40.00E-6, 2, 1E-6, "μF"), // 40.00 μF
			new Range(000.1E-6, 400.0E-6, 1, 1E-6, "μF"), // 400.0 μF
			new Range(0001.E-6, 1000.E-6, 0, 1E-6, "μF"), // 1000 μF
	}, null)),
	/* 安培 */
	A(new AmMeter(new Range[] { //
			new Range(0.001E0, 4.000E0, 2, 1, "A"), //
			new Range(00.01E0, 10.00E0, 1, 1, "A"), //
	}, new Mode[] { //
			Mode.DC, //
			Mode.AC })),
	/* 毫安 */
	mA(new AmMeter(new Range[] { //
			new Range(00.01E-3, 40.00E-3, 2, 1E-3, "mA"), //
			new Range(000.1E-3, 400.0E-3, 1, 1E-3, "mA"), //
	}, new Mode[] { //
			Mode.DC, //
			Mode.AC })),
	/* 微安 */
	muA(new AmMeter(new Range[] { //
			new Range(000.1E-6, 400.0E-6, 1, 1E-6, "μA"), //
			new Range(0001.E-6, 4000.E-6, 0, 1E-6, "μA"), //
	}, new Mode[] { //
			Mode.DC, //
			Mode.AC })),
	/* 温度 */
//	Temperature(new Range[] { //
//			new Range(050.0E0, 400.0E0, 1, 1E0, "℃"), //
//			new Range(0000.E0, 050.0E0, 1, 1E0, "℃"), //
//			new Range(-055.0E0, 0000.E0, 1, 1E0, "℃"), //
//	}, null);
	Temperature(null);

	/**
	 * 每一个档位对应一种万用表
	 */
	@Getter
	private Meter meter;

	private Rotary(Meter meter) {
		this.meter = meter;
	}

	@Override
	public void reset() {
		if (meter == null) {
			log.warn("档位没有对应的仪表");
			return;
		}
		meter.reset();
	}

	@Override
	public boolean range() {
		if (meter == null) {
			log.warn("档位没有对应的仪表");
			return false;
		}
		return meter.range();
	}

	@Override
	public Range getRange() {
		if (meter == null) {
			log.warn("档位没有对应的仪表");
			return null;
		}
		return meter.getRange();
	}

	@Override
	public boolean isAutoRange() {
		if (meter == null) {
			log.warn("档位没有对应的仪表");
			return false;
		}
		return meter.isAutoRange();
	}

	@Override
	public boolean hold() {
		if (meter == null) {
			log.warn("档位没有对应的仪表");
			return false;
		}
		return meter.hold();
	}

	@Override
	public boolean mode() {
		if (meter == null) {
			log.warn("档位没有对应的仪表");
			return false;
		}
		return meter.mode();
	}

	@Override
	public Mode getMode() {
		if (meter == null) {
			log.warn("档位没有对应的仪表");
			return null;
		}
		return meter.getMode();
	}

	@Override
	public String format(double input) {
		if (meter == null) {
			log.warn("档位没有对应的仪表");
			return "";
		}
		return meter.format(input);
	}

	@Override
	public double getValue() {
		if (meter == null) {
			log.warn("档位没有对应的仪表");
			return 0.0;
		}
		return meter.getValue();
	}

}

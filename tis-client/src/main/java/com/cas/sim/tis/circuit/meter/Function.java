package com.cas.sim.tis.circuit.meter;

import lombok.Getter;

public class Function {

	@Getter
	private Range[] ranges;
//	当前量程(记录索引)
	private int rangeIndex;
	@Getter
	private ModeType type;

	private boolean auto;

	public Function(ModeType type, Range... ranges) {
		this.type = type;
		this.ranges = ranges;
	}

	public void reset() {
		rangeIndex = 0;
//		如果有多个量程， 则默认在自动档位
		auto = ranges.length > 0;
	}

	public boolean range() {
		if (auto) {
			auto = false;
			return true;
		}

		int currentIndex = rangeIndex;
		rangeIndex++;
		if (rangeIndex > ranges.length - 1) {
			rangeIndex = 0;
		}
		return currentIndex != rangeIndex;
	}

	public Range getRange() {
		return ranges[rangeIndex];
	}

	public boolean isAutoRange() {
		return auto;
	}

	public double format(double input) {
		return ranges[rangeIndex].formatValue(input);
	}

}

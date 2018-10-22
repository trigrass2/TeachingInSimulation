package com.cas.sim.tis.circuit;

import com.cas.sim.tis.circuit.meter.Mode;
import com.cas.sim.tis.circuit.meter.Range;

public abstract class AbstractMeter implements Meter {

//	仪表工作模式
	private Mode[] modes;
//	当前量程(记录索引)
	private int modeIndex;
//	仪表量程
	private Range[] ranges;
//	当前量程(记录索引)
	private int rangeIndex;
//	锁定示数（锁定后，测量数据不会再发生变化）
	private boolean hold;

	private boolean auto;

	public AbstractMeter() {
	}

	public AbstractMeter(Range[] ranges, Mode[] modes) {
		this.ranges = ranges;
		this.modes = modes;
	}

	@Override
	public void reset() {
		rangeIndex = 0;
		hold = false;
//		如果有多个量程， 则默认在自动档位
		auto = ranges.length > 0;
	}

	private Range currentRange() {
		return ranges[rangeIndex];
	}

	@Override
	public boolean hold() {
		hold = !hold;
		return hold;
	}

	@Override
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

	@Override
	public Range getRange() {
		return ranges[rangeIndex];
	}

	@Override
	public boolean isAutoRange() {
		return auto;
	}

	@Override
	public boolean mode() {
		if (modes == null) {
			return false;
		}
		int currentIndex = modeIndex;
		modeIndex++;
		if (modeIndex > modes.length - 1) {
			modeIndex = 0;
		}
		boolean result = currentIndex != modeIndex;

		if(result) {
			reset();
		}
		return result;
	}

	@Override
	public Mode getMode() {
		return modes[modeIndex];
	}

	@Override
	public String format(double input) {
		return currentRange().getValue(input);
	}

	@Override
	public double getValue() {
		return 0;
	}
}

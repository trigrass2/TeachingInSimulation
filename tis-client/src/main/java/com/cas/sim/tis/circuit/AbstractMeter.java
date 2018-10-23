package com.cas.sim.tis.circuit;

import com.cas.sim.tis.circuit.meter.Function;
import com.cas.sim.tis.circuit.meter.Range;

public abstract class AbstractMeter implements Meter {
//	仪表工作模式
	private Function[] functions;
//	当前量程(记录索引)
	private int functionIndex;

//	锁定示数（锁定后，测量数据不会再发生变化）
	private boolean hold;

	private MeterPen blackPen; // 默认接在com
	private MeterPen redPen; //

	public AbstractMeter(Function... functions) {
		this.functions = functions;

		blackPen = new MeterPen();
		redPen = new MeterPen();

		initPen();
	}

	protected abstract void initPen();

	@Override
	public void reset() {
		functionIndex = 0;
		functions[functionIndex].reset();
		hold = false;
	}

	@Override
	public boolean hold() {
		hold = !hold;
		return hold;
	}

	@Override
	public boolean range() {
		return functions[functionIndex].range();
	}

	@Override
	public Range getRange() {
		return functions[functionIndex].getRange();
	}

	@Override
	public boolean isAutoRange() {
		return functions[functionIndex].isAutoRange();
	}

	@Override
	public boolean function() {
		if (functions == null) {
			return false;
		}
		int currentIndex = functionIndex;
		functionIndex++;
		if (functionIndex > functions.length - 1) {
			functionIndex = 0;
		}
		boolean result = currentIndex != functionIndex;

		if (result) {
			getFunction().reset();
		}
		return result;
	}

	@Override
	public Function getFunction() {
		return functions[functionIndex];
	}

	@Override
	public String format(double input) {
		return functions[functionIndex].getValue(input);
	}

	@Override
	public double getValue() {
		return 0;
	}
}

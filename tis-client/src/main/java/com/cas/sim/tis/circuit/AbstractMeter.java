package com.cas.sim.tis.circuit;

import java.util.ArrayList;
import java.util.List;

import com.cas.circuit.component.Terminal;
import com.cas.circuit.element.CircuitElm;
import com.cas.sim.tis.circuit.meter.Function;
import com.cas.sim.tis.circuit.meter.Range;

import lombok.Getter;

public abstract class AbstractMeter implements Meter {
//	
	@Getter
	protected List<CircuitElm> elmList = new ArrayList<>();
//	仪表工作模式
	private Function[] functions;
//	当前量程(记录索引)
	private int functionIndex;

//	锁定示数（锁定后，测量数据不会再发生变化）
	private boolean hold;

//	private MeterPen blackPen = MeterPen.BLACK; // 黑表笔
//	private MeterPen redPen = MeterPen.RED; // 红表笔

	public AbstractMeter(Function... functions) {
		this.functions = functions;
	}

	@Override
	public void reset() {
		functionIndex = 0;
		functions[functionIndex].reset();
		hold = false;

		MeterPen.BLACK.setTerminal_in_meter(getBlackTerminal());
		MeterPen.RED.setTerminal_in_meter(getRedTerminal());
	}

	protected Terminal getBlackTerminal() {
		if (elmList.size() == 0) {
			return null;
		}
		return elmList.get(0).getPostPoint(0);
	}

	protected Terminal getRedTerminal() {
		if (elmList.size() == 0) {
			return null;
		}

		return elmList.get(0).getPostPoint(1);
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
	public double format() {
		return functions[functionIndex].format(this.getValue());
	}

	@Override
	public double getValue() {
		return 0;
	}
}

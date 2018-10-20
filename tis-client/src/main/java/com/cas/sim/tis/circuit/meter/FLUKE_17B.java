package com.cas.sim.tis.circuit.meter;

import com.cas.sim.tis.circuit.Multimeter;

import lombok.Getter;

/**
 * 万用表(测量电压、电阻)
 */
public class FLUKE_17B implements Multimeter {

	/* 当前万用表档位 */
	@Getter
	private Rotary rotary = Rotary.OFF; // 默认在OFF档位

	@Override
	public boolean hasPreGear() {
		return rotary.ordinal() > 0;
	}

	@Override
	public boolean hasNextGear() {
		return rotary.ordinal() < Rotary.values().length - 1;
	}

	/**
	 * 旋钮<br/>
	 * -1: 逆时针<br />
	 * +1: 顺时针
	 */
	@Override
	public void rotary(int cw) {
		int len = Rotary.values().length;
		int index = rotary.ordinal() + cw;
		if (index < 0) {
			index = len - 1;
		} else if (index > len - 1) {
			index = 0;
		}
		rotary = Rotary.values()[index];
		rotary.reset();
	}

	/**
	 * 量程
	 */
	@Override
	public boolean range() {
		return rotary.range();
	}

	@Override
	public Range getRange() {
		return rotary.getRange();
	}

	/**
	 * 结果保持
	 * @return
	 */
	@Override
	public boolean hold() {
		return rotary.hold();
	}

	/**
	 * （黄色按钮）切换模式
	 */
	@Override
	public boolean mode() {
		return rotary.mode();
	}

	@Override
	public Mode getMode() {
		return rotary.getMode();
	}

	public String meaure(double input) {
		return rotary.format(input);
	}

	@Override
	public void reset() {
		rotary.reset();
	}

	@Override
	public String format(double input) {
		return rotary.format(input);
	}

	@Override
	public double getValue() {
		return rotary.getValue();
	}

	@Override
	public boolean isAutoRange() {
		return rotary.isAutoRange();
	}

}

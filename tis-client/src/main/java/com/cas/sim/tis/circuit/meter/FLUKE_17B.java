package com.cas.sim.tis.circuit.meter;

import java.util.List;

import com.cas.circuit.CirSim;
import com.cas.circuit.element.CircuitElm;
import com.cas.sim.tis.circuit.ISpeaker;
import com.cas.sim.tis.circuit.Multimeter;

import lombok.Getter;

/**
 * 万用表(测量电压、电阻)
 */
public class FLUKE_17B implements Multimeter {
	/* 当前万用表档位 */
	@Getter
	private Rotary rotary = Rotary.OFF; // 默认在OFF档位
	private ISpeaker speaker;

	public FLUKE_17B() {
		super();
	}

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
		rotary.getElmList().forEach(CirSim.ins::removeCircuitElm);

		int len = Rotary.values().length;
		int index = rotary.ordinal() + cw;
		if (index < 0) {
			index = len - 1;
		} else if (index > len - 1) {
			index = 0;
		}
		rotary = Rotary.values()[index];

		rotary.getElmList().forEach(CirSim.ins::addCircuitElm);

		CirSim.ins.needAnalyze();
		rotary.reset();
	}

	/**
	 * 量程
	 */
	@Override
	public boolean range() {
		speaker.buzzer_short();
		boolean result = rotary.range();
		if (!result) {
			speaker.buzzer_shortDelay();
		}
		return result;
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
		speaker.buzzer_short();
		return rotary.hold();
	}

	@Override
	public boolean isHold() {
		return rotary.isHold();
	}

	/**
	 * （黄色按钮）切换模式
	 */
	@Override
	public boolean function() {
		speaker.buzzer_short();
		boolean result = rotary.function();
		if (!result) {
			speaker.buzzer_shortDelay();
		}
		return result;
	}

	@Override
	public Function getFunction() {
		return rotary.getFunction();
	}

	@Override
	public void reset() {
		rotary.reset();
	}

	@Override
	public double format() {
		return rotary.format();
	}

	@Override
	public List<CircuitElm> getElmList() {
		return rotary.getElmList();
	}

	@Override
	public double getValue() {
		return rotary.getValue();
	}

	@Override
	public double getRealValue() {
		return rotary.getRealValue();
	}

	@Override
	public boolean isAutoRange() {
		return rotary.isAutoRange();
	}

	@Override
	public void setSpeaker(ISpeaker speaker) {
		this.speaker = speaker;
	}

	@Override
	public void update() {
//		System.out.println("FLUKE_17B.update()");
//		System.out.println(rotary.getRealValue());
		if (rotary == Rotary.Ohms && rotary.getFunction().getType() == FuncType.On_Off && getRealValue() < 50) {
			speaker.buzzer_continuity();
		} else {
			speaker.buzzer_stop();
		}
	}

}

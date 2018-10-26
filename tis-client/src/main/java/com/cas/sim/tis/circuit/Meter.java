package com.cas.sim.tis.circuit;

import java.util.List;

import com.cas.circuit.element.CircuitElm;
import com.cas.sim.tis.circuit.meter.Function;
import com.cas.sim.tis.circuit.meter.Range;

public interface Meter {
	/**
	 * 重置仪表
	 */
	void reset();

	/**
	 * 切换仪表的量程
	 * @return 切换成功
	 */
	boolean range();

	/**
	 * 获取仪表量程
	 */
	Range getRange();

	/**
	 * 当前仪表是否是自动量程
	 */
	boolean isAutoRange();

	/**
	 * 固定当前测量的结果
	 * @return hold功能是否启用
	 */
	boolean hold();

	boolean isHold();
	
	/**
	 * 切换工作模式。 例如：电流档可以测交流和直流。
	 * @return
	 */
	boolean function();

	/**
	 * @return 仪表功能模式
	 */
	Function getFunction();

	List<CircuitElm> getElmList();
	
	/**
	 * 测量结果格式化
	 */
	double format(); // 格式化后的值

	double getValue(); // 真实值
}

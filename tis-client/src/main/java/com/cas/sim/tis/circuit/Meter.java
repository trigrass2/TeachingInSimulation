package com.cas.sim.tis.circuit;

import com.cas.sim.tis.circuit.meter.Mode;
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

	/**
	 * 切换工作模式。 例如：电流档可以测交流和直流。
	 * @return 
	 */
	boolean mode();

	/**
	 * @return 仪表功能模式
	 */
	Mode getMode();

	/**
	 * 测量结果格式化
	 */
	String format(double input);

	double getValue();

}

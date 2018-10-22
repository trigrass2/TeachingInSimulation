package com.cas.sim.tis.circuit;

import com.cas.sim.tis.circuit.meter.Rotary;

/**
 * 我的理解是：万用表并非一种测量仪器，而是集成了很多种测量仪器的合体， 通过旋钮切换对应的仪表。
 */
public interface Multimeter extends Meter{
	/**
	 * 判断万用表是否有上一个档位
	 */
	boolean hasPreGear();

	/**
	 * 判断万用表是否有下一个档位
	 */
	boolean hasNextGear();

	/**
	 * 切换档位
	 */
	void rotary(int cw);

	/**
	 * 获取当前档位
	 */
	Rotary getRotary();
}

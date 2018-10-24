package com.cas.sim.tis.circuit;

import com.cas.circuit.CirSim;
import com.cas.circuit.component.Terminal;
import com.cas.circuit.component.Wire;

import lombok.Getter;

/**
 * 表笔
 * @author zzy
 */
public enum MeterPen {
	/**
	 * 黑表笔（COM端）
	 */
	BLACK,
	/**
	 * 红表笔
	 */
	RED;

	private Terminal terminal_in_meter;

	/**
	 * 一根<b>内部</b>导线，一头连接万用表侧连接头，另一头等待连接到监测点
	 */
	private Wire wire = new Wire(true);

	/**
	 * 表笔测量的连接头(检测点)
	 */
	@Getter
	private Terminal detected;

	/**
	 * 将表笔连接到用电器侧的连接头上
	 */
	public void connect(Terminal terminal) {
		if (detected != null) {
			disconnect(detected);
		}
		if (terminal == null) {
			return;
		}
		detected = terminal;
		System.out.println(this.name() + "-连接到-" + terminal);
		wire.bind(terminal);
		CirSim.ins.needAnalyze();
	}

	/**
	 * 将表笔连接到用电器侧的连接头上
	 */
	public void disconnect(Terminal terminal) {
		wire.unbind(terminal);
		CirSim.ins.needAnalyze();
		System.out.println(this.name() + "-拔出-" + terminal);
	}

	public void setTerminal_in_meter(Terminal terminal) {
		if (terminal == null) {
			return;
		}

		if (terminal_in_meter != null) {
			System.out.println(this.name() + "-拔出-" + terminal);
			wire.unbind(terminal_in_meter);
		}

		terminal_in_meter = terminal;
		wire.bind(terminal);
		CirSim.ins.needAnalyze();

		System.out.println(this.name() + "-连接到-" + terminal);
		System.out.println(wire);
	}

}

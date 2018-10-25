package com.cas.sim.tis.circuit;

import java.io.IOException;

import com.cas.circuit.CirSim;
import com.cas.circuit.component.Terminal;
import com.cas.circuit.component.Wire;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.Savable;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * 表笔
 * @author zzy
 */
@Slf4j
public enum MeterPen implements Savable {
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
		log.info(this.name() + "-连接到-" + terminal);
		wire.bind(terminal);
		CirSim.ins.needAnalyze();
	}

	/**
	 * 将表笔连接到用电器侧的连接头上
	 */
	public void disconnect(Terminal terminal) {
		wire.unbind(terminal);
		CirSim.ins.needAnalyze();
		log.info(this.name() + "-拔出-" + terminal);
	}

	public void setTerminal_in_meter(Terminal terminal) {
		if (terminal == null) {
			return;
		}

		if (terminal_in_meter != null) {
			log.info(this.name() + "-拔出-" + terminal);
			wire.unbind(terminal_in_meter);
		}

		terminal_in_meter = terminal;
		wire.bind(terminal);
		CirSim.ins.needAnalyze();

		log.info(this.name() + "-连接到-" + terminal);
	}

	@Override
	public void write(JmeExporter ex) throws IOException {
//		nothing to write
	}

	@Override
	public void read(JmeImporter im) throws IOException {
//		nothing to read
	}

}

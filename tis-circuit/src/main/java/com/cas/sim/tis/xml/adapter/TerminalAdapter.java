package com.cas.sim.tis.xml.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import com.cas.circuit.vo.Terminal;

public class TerminalAdapter extends XmlAdapter<String, Terminal> {

	@Override
	public Terminal unmarshal(String v) throws Exception {
		if (v == null) {
			return null;
		}
		int index = Integer.parseInt(v);
		return new Terminal(index);
	}

	@Override
	public String marshal(Terminal v) throws Exception {
		if (v == null || v.getIndex() == null) {
			return null;
		}
		return String.valueOf(v.getIndex());
	}

}

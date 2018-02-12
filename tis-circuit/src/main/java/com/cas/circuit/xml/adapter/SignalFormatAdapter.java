package com.cas.circuit.xml.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import com.cas.circuit.vo.Signal;

public class SignalFormatAdapter extends XmlAdapter<String, String> {

	@Override
	public String unmarshal(String v) throws Exception {
		return Signal.formatSignal(v);
	}

	@Override
	public String marshal(String v) throws Exception {
		return v;
	}

}
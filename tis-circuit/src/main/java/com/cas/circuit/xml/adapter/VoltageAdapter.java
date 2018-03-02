package com.cas.circuit.xml.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import com.cas.circuit.Voltage;

public class VoltageAdapter extends XmlAdapter<String, Voltage> {

	@Override
	public Voltage unmarshal(String v) throws Exception {
		if (v == null) {
			return null;
		}

		Voltage volt = new Voltage();
		if (v.startsWith("+") || v.startsWith("-")) {
			volt.setType(Voltage.IS_DC);
			v = v.substring(1);
		}
		if (v.endsWith("V") || v.endsWith("v")) {
			v = v.substring(0, v.length() - 1);
		}
		volt.setValue(Float.parseFloat(v));
		return volt;
	}

	@Override
	public String marshal(Voltage v) throws Exception {
		if (v == null) {
			return null;
		}
		return ((v.getType() == Voltage.IS_AC) ? "" : "+") + v.getValue() + "v";
	}

}

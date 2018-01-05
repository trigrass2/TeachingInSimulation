package com.cas.circuit.po;

import javax.xml.bind.annotation.XmlAttribute;

public class SensorIO {
	private String switchIn;

	@XmlAttribute
	public String getSwitchIn() {
		return switchIn;
	}

	public void setSwitchIn(String switchIn) {
		this.switchIn = switchIn;
	}
}

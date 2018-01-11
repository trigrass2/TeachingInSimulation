package com.cas.circuit.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.NONE)
public class SignalMapping {
	@XmlAttribute
	private String stitch;
	@XmlAttribute
	private String address;

	private Jack jack;

	public SignalMapping() {
	}

	public void setJack(Jack jack) {
		this.jack = jack;
	}

	public String getStitchName() {
		return jack.getName() + "-" + stitch;
	}

	public String getAddress() {
		return address;
	}

}

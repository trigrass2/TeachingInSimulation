package com.cas.circuit.po;

import javax.xml.bind.annotation.XmlAttribute;

public class SignalMapping {
	private String stitch;
	private String address;

	@XmlAttribute
	public String getStitch() {
		return stitch;
	}

	public void setStitch(String stitch) {
		this.stitch = stitch;
	}

	@XmlAttribute
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}

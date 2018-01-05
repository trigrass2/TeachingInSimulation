package com.cas.circuit.po;

import javax.xml.bind.annotation.XmlAttribute;

public class LightIO {

	private String name;
	private String mdlName;
	private String glowColor;

	@XmlAttribute
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlAttribute
	public String getMdlName() {
		return mdlName;
	}

	public void setMdlName(String mdlName) {
		this.mdlName = mdlName;
	}

	@XmlAttribute
	public String getGlowColor() {
		return glowColor;
	}

	public void setGlowColor(String glowColor) {
		this.glowColor = glowColor;
	}
}

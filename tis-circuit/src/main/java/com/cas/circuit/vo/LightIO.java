package com.cas.circuit.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.cas.sim.tis.xml.adapter.ColorRGBAAdapter;
import com.jme3.math.ColorRGBA;

@XmlAccessorType(XmlAccessType.NONE)
public class LightIO {
	@XmlAttribute
	private String name;
	@XmlAttribute
	private String mdlName;
	@XmlAttribute
	@XmlJavaTypeAdapter(ColorRGBAAdapter.class)
	private ColorRGBA glowColor;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMdlName() {
		return mdlName;
	}

	public void setMdlName(String mdlName) {
		this.mdlName = mdlName;
	}

	public ColorRGBA getGlowColor() {
		return glowColor;
	}

	public void setGlowColor(ColorRGBA glowColor) {
		this.glowColor = glowColor;
	}
}

package com.cas.circuit.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.cas.sim.tis.xml.adapter.ColorRGBAAdapter;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Spatial;

@XmlAccessorType(XmlAccessType.NONE)
public class LightIO {
	@XmlAttribute
	private String name;
	@XmlAttribute
	private String mdlName;
	@XmlAttribute
	@XmlJavaTypeAdapter(ColorRGBAAdapter.class)
	private ColorRGBA glowColor;

	private Spatial model;

	public String getName() {
		return name;
	}

	public String getMdlName() {
		return mdlName;
	}

	public ColorRGBA getGlowColor() {
		return glowColor;
	}

	public void setModel(Spatial model) {
		this.model = model;
	}

	public void openLight() {
//		JmeUtil.setSpatialHighLight(model, glowColor);
	}

	public void closeLight() {
//		JmeUtil.setSpatialHighLight(model, ColorRGBA.BlackNoAlpha);
	}
}

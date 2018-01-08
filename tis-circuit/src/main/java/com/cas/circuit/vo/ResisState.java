package com.cas.circuit.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.cas.sim.tis.xml.adapter.ColorRGBAAdapter;
import com.jme3.math.ColorRGBA;
import com.sun.tools.internal.xjc.runtime.ZeroOneBooleanAdapter;

@XmlAccessorType(XmlAccessType.NONE)
public class ResisState {

	@XmlAttribute
	private String id;
	@XmlAttribute
	@XmlJavaTypeAdapter(ZeroOneBooleanAdapter.class)
	private Boolean isDef;
	@XmlAttribute
	@XmlJavaTypeAdapter(ColorRGBAAdapter.class)
	private ColorRGBA glowColor;

	@XmlElement(name = "ResisRelation")
	private List<ResisRelation> resisRelationList;

	/**
	 * 
	 */
	public ResisState() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Boolean getIsDef() {
		return isDef;
	}

	public void setIsDef(Boolean isDef) {
		this.isDef = isDef;
	}

	public ColorRGBA getGlowColor() {
		return glowColor;
	}

	public void setGlowColor(ColorRGBA glowColor) {
		this.glowColor = glowColor;
	}

	public List<ResisRelation> getResisRelationList() {
		return resisRelationList;
	}

}

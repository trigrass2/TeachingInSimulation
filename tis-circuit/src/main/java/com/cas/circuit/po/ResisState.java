package com.cas.circuit.po;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class ResisState {

	private String id;
	private String isDef;
	private String glowColor;

	private List<ResisRelation> resisRelationList;

	@XmlAttribute
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@XmlAttribute
	public String getIsDef() {
		return isDef;
	}

	public void setIsDef(String isDef) {
		this.isDef = isDef;
	}

	@XmlAttribute
	public String getGlowColor() {
		return glowColor;
	}

	public void setGlowColor(String glowColor) {
		this.glowColor = glowColor;
	}

	@XmlElement(name = "ResisRelation")
	public List<ResisRelation> getResisRelationList() {
		return resisRelationList;
	}

	public void setResisRelationList(List<ResisRelation> resisRelationList) {
		this.resisRelationList = resisRelationList;
	}

}

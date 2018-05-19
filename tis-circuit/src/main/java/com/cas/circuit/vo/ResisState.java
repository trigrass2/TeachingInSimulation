package com.cas.circuit.vo;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.cas.circuit.xml.adapter.BooleanIntAdapter;
import com.cas.circuit.xml.adapter.ColorRGBAAdapter;
import com.jme3.math.ColorRGBA;

@XmlAccessorType(XmlAccessType.NONE)
public class ResisState {// extends BaseVO<ResisStatePO> {

	@XmlAttribute
	private String id;
	@XmlAttribute
	@XmlJavaTypeAdapter(BooleanIntAdapter.class)
	private Boolean isDef = Boolean.FALSE;
	@XmlAttribute
	@XmlJavaTypeAdapter(ColorRGBAAdapter.class)
	private ColorRGBA glowColor;

	@XmlElement(name = "ResisRelation")
	private List<ResisRelation> resisRelationList = new ArrayList<>();

//	-------------------------------------------------

	private ElecCompDef elecCompDef;

	public void beforeUnmarshal(Unmarshaller u, Object parent) {
		if (parent instanceof ElecCompDef) {
			this.elecCompDef = (ElecCompDef) parent;
		}
	}

	public ElecCompDef getElecCompDef() {
		return elecCompDef;
	}

	public String getId() {
		return id;
	}

	public ColorRGBA getGlowColor() {
		return glowColor;
	}

	public Boolean getIsDef() {
		return isDef;
	}

	public List<ResisRelation> getResisRelationList() {
		return resisRelationList;
	}

}

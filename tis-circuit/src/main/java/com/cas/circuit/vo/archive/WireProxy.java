package com.cas.circuit.vo.archive;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.cas.circuit.xml.adapter.ColorRGBAAdapter;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;

@XmlAccessorType(XmlAccessType.NONE)
public class WireProxy {
//	元器件1的tagName
	@XmlAttribute
	private String tagName1;
	@XmlAttribute
	private String ternimalId1;

//	元器件2的tagName
	@XmlAttribute
	private String tagName2;
	@XmlAttribute
	private String ternimalId2;

	private List<Vector3f> pointList = new ArrayList<>();

	@XmlAttribute
	private String number;

	@XmlAttribute
	@XmlJavaTypeAdapter(value = ColorRGBAAdapter.class)
	private ColorRGBA color = ColorRGBA.Black;

	private float width = 1;

	public String getTagName1() {
		return tagName1;
	}

	public void setTagName1(String tagName1) {
		this.tagName1 = tagName1;
	}

	public String getTernimalId1() {
		return ternimalId1;
	}

	public void setTernimalId1(String ternimalId1) {
		this.ternimalId1 = ternimalId1;
	}

	public String getTagName2() {
		return tagName2;
	}

	public void setTagName2(String tagName2) {
		this.tagName2 = tagName2;
	}

	public String getTernimalId2() {
		return ternimalId2;
	}

	public void setTernimalId2(String ternimalId2) {
		this.ternimalId2 = ternimalId2;
	}

	public ColorRGBA getColor() {
		return color;
	}

	public void setColor(ColorRGBA color) {
		this.color = color;
	}

	public float getWidth() {
		return width;
	}

	public List<Vector3f> getPointList() {
		return pointList;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

}

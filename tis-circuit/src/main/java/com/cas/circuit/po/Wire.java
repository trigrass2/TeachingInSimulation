package com.cas.circuit.po;

import javax.xml.bind.annotation.XmlAttribute;

public class Wire {
	private String stitch1;
	private String stitch2;
	private String mark;

	private String width;
	private String color;

	@XmlAttribute
	public String getStitch1() {
		return stitch1;
	}

	public void setStitch1(String stitch1) {
		this.stitch1 = stitch1;
	}

	@XmlAttribute
	public String getStitch2() {
		return stitch2;
	}

	public void setStitch2(String stitch2) {
		this.stitch2 = stitch2;
	}

	@XmlAttribute
	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	@XmlAttribute
	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	@XmlAttribute
	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

}

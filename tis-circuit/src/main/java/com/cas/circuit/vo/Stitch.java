package com.cas.circuit.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.NONE)
public class Stitch extends Terminal {
	@XmlAttribute
	private Integer index;

	private Jack jack;

	public Stitch() {
	}

	public Stitch(Integer index) {
		super();
		this.index = index;
	}

	public Integer getIndex() {
		return index;
	}
	
	public Jack getJack() {
		return jack;
	}

	public void setJack(Jack jack) {
		this.jack = jack;
	}

}

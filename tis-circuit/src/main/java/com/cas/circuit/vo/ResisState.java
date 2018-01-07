package com.cas.circuit.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.sun.tools.internal.xjc.runtime.ZeroOneBooleanAdapter;
@XmlAccessorType(XmlAccessType.NONE)
public class ResisState {
	@XmlAttribute
	private String id;
	@XmlAttribute
	@XmlJavaTypeAdapter(ZeroOneBooleanAdapter.class)
	private Boolean isDef;

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

	public boolean isDef() {
		return isDef;
	}

	public void setDef(boolean isDef) {
		this.isDef = isDef;
	}

}

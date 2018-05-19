package com.cas.circuit.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.cas.circuit.xml.adapter.StringArrayAdapter;

@XmlAccessorType(XmlAccessType.NONE)
public class SensorIO {

	@XmlAttribute
	@XmlJavaTypeAdapter(StringArrayAdapter.class)
	private String[] switchIn;

	public String[] getSwitchIn() {
		return switchIn;
	}

}

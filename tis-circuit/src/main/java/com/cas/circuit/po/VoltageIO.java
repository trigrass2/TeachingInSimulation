package com.cas.circuit.po;

import javax.xml.bind.annotation.XmlAttribute;

public class VoltageIO {

	private String type;
	private String term1Id;
	private String term2Id;
	private String value;
	private String switchIn;
	private String deviation;
	private String group;

	@XmlAttribute
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@XmlAttribute
	public String getTerm1Id() {
		return term1Id;
	}

	public void setTerm1Id(String term1Id) {
		this.term1Id = term1Id;
	}

	@XmlAttribute
	public String getTerm2Id() {
		return term2Id;
	}

	public void setTerm2Id(String term2Id) {
		this.term2Id = term2Id;
	}

	@XmlAttribute
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@XmlAttribute
	public String getSwitchIn() {
		return switchIn;
	}

	public void setSwitchIn(String switchIn) {
		this.switchIn = switchIn;
	}

	@XmlAttribute
	public String getDeviation() {
		return deviation;
	}

	public void setDeviation(String deviation) {
		this.deviation = deviation;
	}

	@XmlAttribute
	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

}

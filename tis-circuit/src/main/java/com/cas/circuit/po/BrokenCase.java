package com.cas.circuit.po;

import javax.xml.bind.annotation.XmlAttribute;

public class BrokenCase {
	// 故障名称
	private String name;
	// 对应机型
	private String model;
	// 故障路径
	private String casePath;

	@XmlAttribute
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlAttribute
	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	@XmlAttribute
	public String getCasePath() {
		return casePath;
	}

	public void setCasePath(String casePath) {
		this.casePath = casePath;
	}

}

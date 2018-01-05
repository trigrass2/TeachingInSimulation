package com.cas.circuit.po;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class Jack {
	private String id;
	/**
	 * 当前插口对应线缆类型
	 */
	private String cable;
	/**
	 * 当前插口对应线缆的插头制式
	 */
	private String format;
	private String mdlName;
	private String name;
	private String module;
	// 插座的方向
	private String jackDirection;
	// 插座上线连出去的方向
	private String direction;
	private String rotation;
	private String isPositive;
	private String core;
	private String belongElecComp;

	private List<Terminal> terminalList;

	@XmlAttribute
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@XmlAttribute
	public String getCable() {
		return cable;
	}

	public void setCable(String cable) {
		this.cable = cable;
	}

	@XmlAttribute
	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	@XmlAttribute
	public String getMdlName() {
		return mdlName;
	}

	public void setMdlName(String mdlName) {
		this.mdlName = mdlName;
	}

	@XmlAttribute
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlAttribute
	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	@XmlAttribute
	public String getJackDirection() {
		return jackDirection;
	}

	public void setJackDirection(String jackDirection) {
		this.jackDirection = jackDirection;
	}

	@XmlAttribute
	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	@XmlAttribute
	public String getRotation() {
		return rotation;
	}

	public void setRotation(String rotation) {
		this.rotation = rotation;
	}

	@XmlAttribute
	public String getIsPositive() {
		return isPositive;
	}

	public void setIsPositive(String isPositive) {
		this.isPositive = isPositive;
	}

	@XmlAttribute
	public String getCore() {
		return core;
	}

	public void setCore(String core) {
		this.core = core;
	}

	@XmlAttribute
	public String getBelongElecComp() {
		return belongElecComp;
	}

	public void setBelongElecComp(String belongElecComp) {
		this.belongElecComp = belongElecComp;
	}

	@XmlElement(name = "Terminal")
	public List<Terminal> getTerminalList() {
		return terminalList;
	}

	public void setTerminalList(List<Terminal> terminalList) {
		this.terminalList = terminalList;
	}

}

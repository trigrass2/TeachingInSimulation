package com.cas.circuit.po;

import javax.xml.bind.annotation.XmlAttribute;

public class Terminal {
	private String id;
	private String name;
	private String mdlName;
	private String direction;
	private String index;
	private String mark;
	private String voltage;
	private String switchIn;
	private String type;
	private String team;
	private String num;// 限制可连接导线的数量，要么是1，要么是2.

	@XmlAttribute
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@XmlAttribute
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlAttribute
	public String getMdlName() {
		return mdlName;
	}

	public void setMdlName(String mdlName) {
		this.mdlName = mdlName;
	}

	@XmlAttribute
	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	@XmlAttribute
	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	@XmlAttribute
	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	@XmlAttribute
	public String getVoltage() {
		return voltage;
	}

	public void setVoltage(String voltage) {
		this.voltage = voltage;
	}

	@XmlAttribute
	public String getSwitchIn() {
		return switchIn;
	}

	public void setSwitchIn(String switchIn) {
		this.switchIn = switchIn;
	}

	@XmlAttribute
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@XmlAttribute
	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	@XmlAttribute
	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

}

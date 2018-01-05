package com.cas.circuit.po;

import javax.xml.bind.annotation.XmlAttribute;

public class ControlIO {

	private String name;
	private String mdlName;
	private String controlModName;
	private String type;
	private String interact;
	private String defSwitch;
	private String switchIn;
	private String motion;
	private String motionParams;
	private String axis;
	private String speed;
	private String smooth;
	private String elementText;

	@XmlAttribute
	public String getElementText() {
		return elementText;
	}

	public void setElementText(String elementText) {
		this.elementText = elementText;
	}

	@XmlAttribute
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlAttribute
	public String getDefSwitch() {
		return defSwitch;
	}

	public void setDefSwitch(String defSwitch) {
		this.defSwitch = defSwitch;
	}

	@XmlAttribute
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@XmlAttribute
	public String getInteract() {
		return interact;
	}

	public void setInteract(String interact) {
		this.interact = interact;
	}

	@XmlAttribute
	public String getMdlName() {
		return mdlName;
	}

	public void setMdlName(String mdlName) {
		this.mdlName = mdlName;
	}

	@XmlAttribute
	public String getSwitchIn() {
		return switchIn;
	}

	public void setSwitchIn(String switchIn) {
		this.switchIn = switchIn;
	}

	@XmlAttribute
	public String getMotion() {
		return motion;
	}

	public void setMotion(String motion) {
		this.motion = motion;
	}

	@XmlAttribute
	public String getMotionParams() {
		return motionParams;
	}

	public void setMotionParams(String motionParams) {
		this.motionParams = motionParams;
	}

	@XmlAttribute
	public String getAxis() {
		return axis;
	}

	public void setAxis(String axis) {
		this.axis = axis;
	}

	@XmlAttribute
	public String getSpeed() {
		return speed;
	}

	public void setSpeed(String speed) {
		this.speed = speed;
	}

	@XmlAttribute
	public String getControlModName() {
		return controlModName;
	}

	public void setControlModName(String controlModName) {
		this.controlModName = controlModName;
	}

	@XmlAttribute
	public String getSmooth() {
		return smooth;
	}

	public void setSmooth(String smooth) {
		this.smooth = smooth;
	}
}

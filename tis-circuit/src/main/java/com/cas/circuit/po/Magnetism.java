package com.cas.circuit.po;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class Magnetism {
	private String id;

	private List<ControlIO> controlIOList;

	private List<VoltageIO> voltageIOList;

	private List<LightIO> lightIOList;

	private List<SensorIO> sensorIOList;

	@XmlAttribute
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@XmlElement(name = "ControlIO")
	public List<ControlIO> getControlIOList() {
		return controlIOList;
	}

	public void setControlIOList(List<ControlIO> controlIOList) {
		this.controlIOList = controlIOList;
	}

	@XmlElement(name = "VoltageIO")
	public List<VoltageIO> getVoltageIOList() {
		return voltageIOList;
	}

	public void setVoltageIOList(List<VoltageIO> voltageIOList) {
		this.voltageIOList = voltageIOList;
	}

	@XmlElement(name = "LightIO")
	public List<LightIO> getLightIOList() {
		return lightIOList;
	}

	public void setLightIOList(List<LightIO> lightIOList) {
		this.lightIOList = lightIOList;
	}

	@XmlElement(name = "SensorIO")
	public List<SensorIO> getSensorIOList() {
		return sensorIOList;
	}

	public void setSensorIOList(List<SensorIO> sensorIOList) {
		this.sensorIOList = sensorIOList;
	}

}

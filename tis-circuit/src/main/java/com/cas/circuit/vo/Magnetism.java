package com.cas.circuit.vo;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.NONE)
public class Magnetism {// extends BaseVO<MagnetismPO> {
	@XmlAttribute
	private String id;

	@XmlElement(name = "VoltageIO")
	private List<VoltageIO> voltageIOList = new ArrayList<>();
	@XmlElement(name = "ControlIO")
	private List<ControlIO> controlIOList = new ArrayList<>();
	@XmlElement(name = "LightIO")
	private List<LightIO> lightIOList = new ArrayList<>();
	@XmlElement(name = "SensorIO")
	private List<SensorIO> sensorIOList = new ArrayList<>();

	private boolean effect;

	private List<VoltageIO> inputVoltageIOs = new ArrayList<VoltageIO>();
	private List<VoltageIO> outputVoltageIOs = new ArrayList<VoltageIO>();
	private List<ControlIO> outputControlIOs = new ArrayList<ControlIO>();

	public Magnetism() {
		super();
	}

	public boolean isEffect() {
		return effect;
	}

	public List<VoltageIO> getVoltageIOList() {
		return voltageIOList;
	}

	public List<ControlIO> getControlIOList() {
		return controlIOList;
	}

	public List<LightIO> getLightIOList() {
		return lightIOList;
	}

	public List<SensorIO> getSensorIOList() {
		return sensorIOList;
	}

	public List<VoltageIO> getInputVoltageIOs() {
		return inputVoltageIOs;
	}

	public List<VoltageIO> getOutputVoltageIOs() {
		return outputVoltageIOs;
	}

	public List<ControlIO> getOutputControlIOs() {
		return outputControlIOs;
	}

	public void setEffect(boolean effect) {
		this.effect = effect;
	}
}

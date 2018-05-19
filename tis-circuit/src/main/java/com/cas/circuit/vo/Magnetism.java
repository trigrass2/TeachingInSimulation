package com.cas.circuit.vo;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.Unmarshaller;
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

//	--------------------------------------------------------------------

	private boolean effect;

	private List<VoltageIO> inputVoltageIOs = new ArrayList<VoltageIO>();
	private List<VoltageIO> outputVoltageIOs = new ArrayList<VoltageIO>();
	private List<ControlIO> outputControlIOs = new ArrayList<ControlIO>();

	private ElecCompDef elecCompDef;

	public Magnetism() {
		super();
	}

	public void beforeUnmarshal(Unmarshaller u, Object parent) {
		this.elecCompDef = (ElecCompDef) parent;
	}
//	public void afterUnmarshal(Unmarshaller u, Object parent) {
//		voltageIOList.forEach(io -> {
//			Terminal term1 = getTerminal(io.getTerm1Id());
//			Terminal term2 = getTerminal(io.getTerm2Id());
//			term1.getVoltIOs().add(io);
//			io.setTerm1(term1);
//			term2.getVoltIOs().add(io);
//			io.setTerm2(term2);
//		});
//	}

	public ElecCompDef getElecCompDef() {
		return elecCompDef;
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

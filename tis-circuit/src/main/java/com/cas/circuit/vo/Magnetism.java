package com.cas.circuit.vo;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.NONE)
public class Magnetism  {
	@XmlAttribute
	private String id;

	@XmlElement(name="VoltageIO")
	private List<VoltageIO> voltageIOs = new ArrayList<>();
	@XmlElement(name="ControlIO")
	private List<ControlIO> controlIOs = new ArrayList<>();
	@XmlElement(name="LightIO")
	private List<LightIO> lightIOs = new ArrayList<>();
	@XmlElement(name="SensorIO")
	private List<SensorIO> sensorIOs = new ArrayList<>();

	private boolean effect; 
	private List<VoltageIO> inputVoltageIOs = new ArrayList<VoltageIO>();
	private List<VoltageIO> outputVoltageIOs = new ArrayList<VoltageIO>();
	private List<ControlIO> outputControlIOs = new ArrayList<ControlIO>();
//
//	@Override
//	protected void addChild(BaseVO<?> child) {
//		super.addChild(child);
//		if (child instanceof ControlIO) {
//			ElecCompDef elecComp = (ElecCompDef) parent;
//			ControlIO control = (ControlIO) child;
//			control.setElecComp(elecComp);
//			controlIOs.add((ControlIO) child);
//			if (control.getPO().getType().contains(CfgConst.SWITCH_CTRL_OUTPUT)) {
//				outputControlIOs.add(control);
//			}
//		} else if (child instanceof VoltageIO) {
//			ElecCompDef elecComp = (ElecCompDef) parent;
//			VoltageIO voltIO = (VoltageIO) child;
//			voltIO.setElecComp(elecComp);
//			voltageIOs.add(voltIO);
//			if (voltIO.getPO().getType().contains(CfgConst.SWITCH_CTRL_OUTPUT)) {
//				outputVoltageIOs.add(voltIO);
//			}
//			if (voltIO.getPO().getType().contains(CfgConst.SWITCH_CTRL_INPUT)) {
//				inputVoltageIOs.add(voltIO);
//			}
//		} else if (child instanceof SensorIO) {
//			ElecCompDef elecComp = (ElecCompDef) parent;
//			SensorIO sensorIO = (SensorIO) child;
//			sensorIO.setElecComp(elecComp);
//			sensorIOs.add(sensorIO);
//		} else if (child instanceof LightIO) {
//			LightIO lightIO = (LightIO) child;
//			lightIOs.add(lightIO);
//		}
//	}
	
	public List<ControlIO> getControlIOs() {
		return controlIOs;
	}

	public String getId() {
		return id;
	}

	public List<VoltageIO> getInputVoltageIOs() {
		return inputVoltageIOs;
	}

	public List<LightIO> getLightIOs() {
		return lightIOs;
	}

	public List<ControlIO> getOutputControlIOs() {
		return outputControlIOs;
	}

	public List<VoltageIO> getOutputVoltageIOs() {
		return outputVoltageIOs;
	}

	public List<SensorIO> getSensorIOs() {
		return sensorIOs;
	}
//
//	/*
//	 * (non-Javadoc)
//	 * @see com.cas.cfg.vo.BaseVO#cleanUp()
//	 */
//	@Override
//	protected void cleanUp() {
//		voltageIOs = new ArrayList<VoltageIO>();
//		controlIOs = new ArrayList<ControlIO>();
//		lightIOs = new ArrayList<LightIO>();
//		effect = false;
//
//		inputVoltageIOs = new ArrayList<VoltageIO>();
//		outputVoltageIOs = new ArrayList<VoltageIO>();
//		outputControlIOs = new ArrayList<ControlIO>();
//		sensorIOs = new ArrayList<SensorIO>();
//
////		ioGroups = new ArrayList<List<VoltageIO>>();// [[0--1, 1--2], [11--12, 12--13], [14--15, 15--16, 16--17], [18--19]]
////		termGroups = new ArrayList<List<String>>();// [[0, 1, 2], [11, 12, 13], [14, 15, 16, 17], [18, 19]]
////		Key: 连接头ID , 与该连接头有关系的VoltageIOs
////		termVoltageIOGroupMap = new HashMap<String, List<VoltageIO>>();
//	}

	public List<VoltageIO> getVoltageIOs() {
		return voltageIOs;
	}

	public boolean isEffect() {
		return effect;
	}

	public void setEffect(boolean effect) {
		this.effect = effect;
	}

	public void setId(String id) {
		this.id = id;
	}
}

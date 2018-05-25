package com.cas.circuit.vo;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import com.cas.circuit.consts.IOType;

import lombok.Getter;
import lombok.Setter;

@XmlAccessorType(XmlAccessType.NONE)
@Getter
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

	@Setter
	private boolean effect;

	private List<VoltageIO> inputVoltageIOs = new ArrayList<VoltageIO>();
	private List<VoltageIO> outputVoltageIOs = new ArrayList<VoltageIO>();
	private List<ControlIO> outputControlIOs = new ArrayList<ControlIO>();

	@Getter
	private ElecCompDef elecCompDef;

	public Magnetism() {
		super();
	}

	public void beforeUnmarshal(Unmarshaller u, Object parent) {
		this.elecCompDef = (ElecCompDef) parent;
	}

	public void afterUnmarshal(Unmarshaller u, Object parent) {
		voltageIOList.forEach(v -> {
			if (v.getType() != IOType.OUTPUT) {
				inputVoltageIOs.add(v);
			}
			if (v.getType() != IOType.INPUT) {
				outputVoltageIOs.add(v);
			}
		});
		
		controlIOList.forEach(c->{
			if(c.getType() != IOType.INPUT) {
				outputControlIOs.add(c);
			}
		});

//		voltageIOList.forEach(io -> {
//			Terminal term1 = getTerminal(io.getTerm1Id());
//			Terminal term2 = getTerminal(io.getTerm2Id());
//			term1.getVoltIOs().add(io);
//			io.setTerm1(term1);
//			term2.getVoltIOs().add(io);
//			io.setTerm2(term2);
//		});
	}
}

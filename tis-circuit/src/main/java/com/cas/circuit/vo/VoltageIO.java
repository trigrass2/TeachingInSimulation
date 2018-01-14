package com.cas.circuit.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlList;

import com.cas.circuit.consts.IOType;

@XmlAccessorType(XmlAccessType.NONE)
public class VoltageIO{// extends SwitchCtrl<VoltageIOPO> {
	@XmlAttribute
	private String type;//input|output|both
	@XmlAttribute
	private String term1Id;
	@XmlAttribute
	private String term2Id;
	@XmlAttribute
	private Integer voltType; //Voltage.IS_DC/Voltage.IS_AC
	@XmlAttribute
	private Float value;
	@XmlList
	private List<String> switchIn;
	@XmlAttribute
	private Float deviation;
	@XmlAttribute
	private String group;
	
	public String getType() {
		if(null == type) {
			return IOType.BOTH;
		}
		return type;
	}
	public String getTerm1Id() {
		return term1Id;
	}
	public String getTerm2Id() {
		return term2Id;
	}
	public Float getValue() {
		return value;
	}
	public Integer getVoltType() {
		return voltType;
	}
	public Float getDeviation() {
		return deviation;
	}
	public List<String> getSwitchIn() {
		return switchIn;
	}

	private Terminal term1;
	private Terminal term2;
	public boolean relateWith(List<VoltageIO> voltIos) {
		boolean contain = false;
		for (VoltageIO voltIO : voltIos) {
			if (voltIO.getTerm1Id().equals(getTerm1Id()) || voltIO.getTerm1Id().equals(getTerm2Id()) || voltIO.getTerm2Id().equals(getTerm1Id()) || voltIO.getTerm2Id().equals(getTerm2Id())) {
				contain = true;
			}
		}
		return contain;
	}

	public boolean contains(Terminal term) {
//		FIXME contains(Terminal term)
//		ElecCompDef elecComp = (ElecCompDef) parent.getParent();
//		if (!elecComp.getChildren().contains(term)) {
			return false;
//		}
//		return po.getTerm1Id().equals(term.getPO().getId()) || po.getTerm2Id().equals(term.getPO().getId());
	}
//
	public Terminal getAnotherTerm(Terminal term) {
//		FIXME getAnotherTerm(Terminal term)
//		ElecCompDef elecComp = (ElecCompDef) parent.getParent();
//		if (po.getTerm1Id().equals(term.getPO().getId())) {
//			return elecComp.getTerminal(po.getTerm2Id());
//		} else if (po.getTerm2Id().equals(term.getPO().getId())) {
//			return elecComp.getTerminal(po.getTerm1Id());
//		} else {
			return null;
//		}
	}
	public Terminal getTerm1() {
		return term1;
	}

	public void setTerm1(Terminal term1) {
		this.term1 = term1;
	}

	public Terminal getTerm2() {
		return term2;
	}

	public void setTerm2(Terminal term2) {
		this.term2 = term2;
	}
}
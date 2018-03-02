package com.cas.circuit.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.cas.circuit.Voltage;
import com.cas.circuit.consts.IOType;
import com.cas.circuit.xml.adapter.StringArrayAdapter;
import com.cas.circuit.xml.adapter.VoltageAdapter;

@XmlAccessorType(XmlAccessType.NONE)
public class VoltageIO extends SwitchCtrl {
	@XmlAttribute
	private String type;// input|output|both
	@XmlAttribute
	private String term1Id;
	@XmlAttribute
	private String term2Id;
	@XmlAttribute
	@XmlJavaTypeAdapter(VoltageAdapter.class)
	private Voltage voltage;
	@XmlAttribute
	@XmlJavaTypeAdapter(StringArrayAdapter.class)
	private String[] switchIn;
	@XmlAttribute
	private Float deviation;
	@XmlAttribute
	private String group;

	private Terminal term1;
	private Terminal term2;
	private float requireVolt;

	public String getType() {
		if (null == type) {
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

	@Override
	protected void changeStateIndex(Integer index) {

	}

	public boolean relateWith(List<VoltageIO> voltIos) {
		boolean contain = false;
		for (VoltageIO voltIO : voltIos) {
			if (voltIO.getTerm1Id().equals(term1Id) || voltIO.getTerm1Id().equals(term2Id) || voltIO.getTerm2Id().equals(term1Id) || voltIO.getTerm2Id().equals(term2Id)) {
				contain = true;
			}
		}
		return contain;
	}

	public boolean contains(Terminal term) {
//		ElecCompDef elecComp = (ElecCompDef) parent.getParent();
//		if (!elecComp.getChildren().contains(term)) {
//			return false;
//		}
		return term1Id.equals(term.getId()) || term2Id.equals(term.getId());
	}

	public Terminal getAnotherTerm(Terminal term) {
		if (term1 == term) {
			return term2;
		} else if (term2 == term) {
			return term1;
		} else {
			throw new RuntimeException("");
		}
	}

	public float getRequireVolt() {
		return requireVolt;
	}

	public float getDeviation() {
		return deviation;
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

	public Voltage getVoltage() {
		return voltage;
	}

	@Override
	public String toString() {
		return getTerm1Id() + "--" + getTerm2Id();
	}
}

package com.cas.circuit.vo;

import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.cas.circuit.Voltage;
import com.cas.circuit.consts.IOType;
import com.cas.circuit.xml.adapter.StringArrayAdapter;
import com.cas.circuit.xml.adapter.VoltageAdapter;

import lombok.Getter;
import lombok.Setter;

@XmlAccessorType(XmlAccessType.NONE)
public class VoltageIO {
	@XmlAttribute
	@Getter
	private String type = IOType.BOTH;// input|output|both
	@XmlAttribute
	@Getter
	private String term1Id;
	@XmlAttribute
	@Getter
	private String term2Id;
	@XmlAttribute
	@Getter
	@XmlJavaTypeAdapter(VoltageAdapter.class)
	private Voltage voltage;
	@XmlAttribute
	@Getter
	@XmlJavaTypeAdapter(StringArrayAdapter.class)
	private String[] switchIn;
	@XmlAttribute
	@Getter
	private Float deviation;
	@XmlAttribute
	@Getter
	private String group;
//	--------------------------------------------------------------------
	@Getter
	private ElecCompDef elecCompDef;
	@Getter
	private SwitchCtrl switchCtrl;
	@Getter
	@Setter
	private Terminal term1;
	@Getter
	@Setter
	private Terminal term2;
	@Getter
	private float requireVolt;

	public void beforeUnmarshal(Unmarshaller u, Object parent) {
		this.elecCompDef = ((Magnetism) parent).getElecCompDef();
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

	@Override
	public String toString() {
		return getTerm1Id() + "--" + getTerm2Id();
	}

}

package com.cas.circuit.po;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Comps")
public class CompRoot {
	private List<ElecCompDef> eleCompList;

	@XmlElement(name = "ElecCompDef")
	public List<ElecCompDef> getEleCompList() {
		return eleCompList;
	}

	public void setEleCompList(List<ElecCompDef> eleCompList) {
		this.eleCompList = eleCompList;
	}

}

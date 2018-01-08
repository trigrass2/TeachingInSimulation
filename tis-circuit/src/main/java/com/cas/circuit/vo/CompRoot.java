package com.cas.circuit.vo;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Comps")
public class CompRoot {
	private List<ElecCompDef> eleCompList;

	private Map<String, ElecCompDef> eleCompMap;

	@XmlElement(name = "ElecCompDef")
	public List<ElecCompDef> getEleCompList() {
		return eleCompList;
	}

	public void setEleCompList(List<ElecCompDef> eleCompList) {
		this.eleCompList = eleCompList;
	}

	public Map<String, ElecCompDef> getEleCompMap() {
		return eleCompMap;
	}

	public void build() {
		eleCompMap = eleCompList.stream().collect(Collectors.toMap(ElecCompDef::getModel, data -> data));
		eleCompList.stream().forEach(ElecCompDef::build);
	}
}

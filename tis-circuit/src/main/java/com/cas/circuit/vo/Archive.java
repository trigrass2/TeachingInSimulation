package com.cas.circuit.vo;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.cas.circuit.vo.archive.ElecCompProxy;
import com.cas.circuit.vo.archive.WireProxy;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Archive")
public class Archive {

	@XmlAttribute(name = "name")
	private String name;

	@XmlElement(name = "Layout")
	@XmlElementWrapper(name = "Layouts")
	private List<ElecCompProxy> compList = new ArrayList<>();
	@XmlElement(name = "Wire")
	@XmlElementWrapper(name = "Wires")
	private List<WireProxy> wireList = new ArrayList<>();

	public List<ElecCompProxy> getCompList() {
		return compList;
	}

	public List<WireProxy> getWireList() {
		return wireList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 将元器件信息注入，等待保存
	 */
	public void injectComp() {

	}

	/**
	 * 将导线信息注入，等待保存
	 */
	public void injectWire() {

	}
}

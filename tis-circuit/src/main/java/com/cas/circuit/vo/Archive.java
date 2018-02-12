package com.cas.circuit.vo;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.cas.circuit.vo.archive.ElecCompProxy;
import com.cas.circuit.vo.archive.WireProxy;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement
public class Archive {

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

package com.cas.circuit.po;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import com.cas.gas.po.BlockState;

public class ElecCompDef {
//	元气件名称
	private String name;
//	元气件型号
	private String model;
//	元气件简介
	private String desc;
//	元气件的处理逻辑类
	private String appStateCls;
//	BaseVO中规定的属性用来保存标签的文本节点内容
	private String elementText;
//	
	private String isCable;
	private List<Terminal> terminalList;
	private List<Jack> jackList;
	private List<Magnetism> magnetismList;
	private List<ResisState> resisStateList;
	private List<BlockState> blockStateList;
	private List<LightIO> lightIOList;

	@XmlAttribute
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlAttribute
	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	@XmlAttribute
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@XmlAttribute
	public String getAppStateCls() {
		return appStateCls;
	}

	public void setAppStateCls(String appStateCls) {
		this.appStateCls = appStateCls;
	}

	@XmlAttribute
	public String getElementText() {
		return elementText;
	}

	public void setElementText(String elementText) {
		this.elementText = elementText;
	}

	@XmlAttribute
	public String getIsCable() {
		return isCable;
	}

	public void setIsCable(String isCable) {
		this.isCable = isCable;
	}

	@XmlElement(name = "Terminal")
	public List<Terminal> getTerminalList() {
		return terminalList;
	}

	public void setTerminalList(List<Terminal> terminalList) {
		this.terminalList = terminalList;
	}

	@XmlElement(name = "Jack")
	public List<Jack> getJackList() {
		return jackList;
	}

	public void setJackList(List<Jack> jackList) {
		this.jackList = jackList;
	}

	@XmlElement(name = "Magnetism")
	public List<Magnetism> getMagnetismList() {
		return magnetismList;
	}

	public void setMagnetismList(List<Magnetism> magnetismList) {
		this.magnetismList = magnetismList;
	}

	@XmlElement(name = "ResisState")
	public List<ResisState> getResisStateList() {
		return resisStateList;
	}

	public void setResisStateList(List<ResisState> resisStateList) {
		this.resisStateList = resisStateList;
	}

	@XmlElement(name = "BlockState")
	public List<BlockState> getBlockStateList() {
		return blockStateList;
	}

	public void setBlockStateList(List<BlockState> blockStateList) {
		this.blockStateList = blockStateList;
	}

	@XmlElement(name = "LightIO")
	public List<LightIO> getLightIOList() {
		return lightIOList;
	}

	public void setLightIOList(List<LightIO> lightIOList) {
		this.lightIOList = lightIOList;
	}

}

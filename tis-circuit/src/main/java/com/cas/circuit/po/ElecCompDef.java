package com.cas.circuit.po;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import com.cas.circuit.vo.ResisState;
import com.cas.gas.po.BlockState;
import com.cas.gas.po.GasPort;

@XmlAccessorType(value = XmlAccessType.NONE)
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

//	Key:电缆插孔的名字
//	Value:电缆插孔
	private Map<String, Jack> jackMap;
//	Key: id
//	Value:Terminal
//	存放所有的连接头
	private Map<String, Terminal> terminalMap;
//	Key: id
//	Value:Terminal
//	存放所有连接头及插孔中的针脚
	private Map<String, Terminal> termAndStich;
//  Key: id
//	Value: GasPort
//	存放所有的气口
	private Map<String, GasPort> gasPortMap;
	private Map<String, ResisState> resisStatesMap;
	private Map<String, BlockState> blockStatesMap;

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

	public void build() {
		jackMap = jackList.stream().collect(Collectors.toMap(Jack::getId, data -> data));
		terminalMap = terminalList.stream().collect(Collectors.toMap(Terminal::getId, data -> data));
		resisStatesMap = resisStateList.stream().collect(Collectors.toMap(ResisState::getId, data -> data));
		blockStatesMap = blockStateList.stream().collect(Collectors.toMap(BlockState::getId, data -> data));
//		jackList.stream().forEach(Jack::getTerminalList);
	}

	public Map<String, Jack> getJackMap() {
		return jackMap;
	}

	public Map<String, Terminal> getTerminalMap() {
		return terminalMap;
	}

	public Map<String, Terminal> getTermAndStich() {
		return termAndStich;
	}

	public Map<String, GasPort> getGasPortMap() {
		return gasPortMap;
	}

}

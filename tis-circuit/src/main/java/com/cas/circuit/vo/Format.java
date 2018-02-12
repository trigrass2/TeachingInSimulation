package com.cas.circuit.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
@XmlAccessorType(XmlAccessType.NONE)
public class Format {// extends BaseVO<FormatPO> {
	@XmlAttribute
	private String id;
	@XmlAttribute
	private String mdlRef;
	@XmlAttribute
	private String centerModName;
	@XmlAttribute
	private String vMdlRef;
	@XmlAttribute
	private int turns;

	private Map<String, Cable> cableMap = new HashMap<String, Cable>();
	private List<Cable> cableList = new ArrayList<Cable>();

	public int getTurns() {
		return turns;
	}

	public Map<String, Cable> getCableMap() {
		return cableMap;
	}

	public List<Cable> getCableList() {
		return cableList;
	}

}

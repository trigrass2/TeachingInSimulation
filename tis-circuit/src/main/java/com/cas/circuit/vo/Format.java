package com.cas.circuit.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.NONE)
public class Format {
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

//	private Map<String, Cable> cableMap = new HashMap<String, Cable>();
//	private List<Cable> cableList = new ArrayList<Cable>();
//
//	@Override
//	protected void addChild(BaseVO<?> child) {
//		super.addChild(child);
//		cableMap.put(((Cable) child).getPO().getId(), (Cable) child);
//	}

	@Override
	public String toString() {
		return "Format [turns=" + turns + "]";
	}
}

package com.cas.circuit.po;

import javax.xml.bind.annotation.XmlAttribute;

public class ElecComp {
	private String id;
//	元器件模型名称
	private String mdlName;
//	元器件标签名
	private String tagName;
//	元器件额外信息
	private String elementText;

	@XmlAttribute
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@XmlAttribute
	public String getMdlName() {
		return mdlName;
	}

	public void setMdlName(String mdlName) {
		this.mdlName = mdlName;
	}

	@XmlAttribute
	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	@XmlAttribute
	public String getElementText() {
		return elementText;
	}

	public void setElementText(String elementText) {
		this.elementText = elementText;
	}

}

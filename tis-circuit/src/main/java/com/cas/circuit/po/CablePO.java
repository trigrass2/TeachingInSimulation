package com.cas.circuit.po;

import javax.xml.bind.annotation.XmlAttribute;

public class CablePO {
	private String id;
	private String name;
	private String format;
	private String width;
	private String desc;
	private String mdlRef;
	private String core;

	@XmlAttribute
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@XmlAttribute
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@XmlAttribute
	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	@XmlAttribute
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlAttribute
	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	@XmlAttribute
	public String getMdlRef() {
		return mdlRef;
	}

	public void setMdlRef(String mdlRef) {
		this.mdlRef = mdlRef;
	}

	@XmlAttribute
	public String getCore() {
		return core;
	}

	public void setCore(String core) {
		this.core = core;
	}

}

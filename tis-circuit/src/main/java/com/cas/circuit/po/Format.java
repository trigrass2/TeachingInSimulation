package com.cas.circuit.po;

import javax.xml.bind.annotation.XmlAttribute;

public class Format {

	private String id;
	private String mdlRef;
	private String turns;
	private String centerModName;
	private String vMdlRef;

	@XmlAttribute
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@XmlAttribute
	public String getMdlRef() {
		return mdlRef;
	}

	public void setMdlRef(String mdlRef) {
		this.mdlRef = mdlRef;
	}

	@XmlAttribute
	public String getTurns() {
		return turns;
	}

	public void setTurns(String turns) {
		this.turns = turns;
	}

	@XmlAttribute
	public String getCenterModName() {
		return centerModName;
	}

	public void setCenterModName(String centerModName) {
		this.centerModName = centerModName;
	}

	@XmlAttribute
	public String getVMdlRef() {
		return vMdlRef;
	}

	public void setVMdlRef(String vMdlRef) {
		this.vMdlRef = vMdlRef;
	}

}

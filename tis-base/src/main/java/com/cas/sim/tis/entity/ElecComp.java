package com.cas.sim.tis.entity;

import java.io.Serializable;

import javax.persistence.Id;

/**
 * 元器件实体类
 * @author Administrator
 */
public class ElecComp implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2680718661511929185L;
	//
	@Id
	private Integer id;
//	元器件类型
	private String type;
//	元器件型号
	private String name;
//	元器件型号
	private String model;

	private String mdlPath;

	private String cfgPath;

	private String animPath;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getMdlPath() {
		return mdlPath;
	}

	public void setMdlPath(String mdlPath) {
		this.mdlPath = mdlPath;
	}

	public String getCfgPath() {
		return cfgPath;
	}

	public void setCfgPath(String cfgPath) {
		this.cfgPath = cfgPath;
	}

	public String getAnimPath() {
		return animPath;
	}

	public void setAnimPath(String animPath) {
		this.animPath = animPath;
	}

	@Override
	public String toString() {
		return "ElecComp [id=" + id + "\r\n type=" + type + "\r\n name=" + name + "\r\n model=" + model + "\r\n mdlPath=" + mdlPath + "\r\n cfgPath=" + cfgPath + "]";
	}

}

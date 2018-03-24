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

	public static final int COMBINE_NONE = 0;
	public static final int COMBINE_TOP = 1;
	public static final int COMBINE_BUTTOM = 2;
	//
	@Id
	private Integer id;
//	元器件类型
	private Integer type;
//	元器件组合
	private Integer combine;
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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getCombine() {
		return combine;
	}

	public void setCombine(Integer combine) {
		this.combine = combine;
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

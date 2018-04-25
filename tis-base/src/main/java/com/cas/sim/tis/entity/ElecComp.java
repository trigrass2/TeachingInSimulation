package com.cas.sim.tis.entity;

import java.io.Serializable;

import javax.persistence.Id;
import javax.persistence.Transient;

import com.jme3.scene.Spatial;

import lombok.Getter;
import lombok.Setter;

/**
 * 元器件实体类
 * @author Administrator
 */
@Getter
@Setter
public class ElecComp implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2680718661511929185L;

	public static final int COMBINE_NONE = 0;
	public static final int COMBINE_RELY_ON = 1;
	public static final int COMBINE_BASE = 2;
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
	@Transient
	private Spatial spatial;
}

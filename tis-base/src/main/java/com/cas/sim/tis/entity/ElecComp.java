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
	private static final long serialVersionUID = 3752718132675421709L;
//	
	@Id
	private Integer id;
//	元器件类型
	private String type;
//	元器件型号
	private String name;
//	元器件型号
	private String model;

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

}

package com.cas.sim.tis.vo;

import java.io.Serializable;

public class PreparationInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4785303497037493566L;

	private Integer id;
	/**
	 * icon用于区分图标，0-7对应资源图标，8对应link图标
	 */
	private Integer icon;
	private String name;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIcon() {
		return icon;
	}

	public void setIcon(Integer icon) {
		this.icon = icon;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

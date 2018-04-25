package com.cas.sim.tis.vo;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
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
}

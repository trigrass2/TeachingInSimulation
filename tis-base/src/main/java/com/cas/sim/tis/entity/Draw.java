package com.cas.sim.tis.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
//图纸
public class Draw implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4887032744373795614L;
	@Id
	private Integer id;
	private String name;
	private String paths;
	private Integer creatorId;
	private Date createDate;
	private Boolean del = false;
}

package com.cas.sim.tis.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class Collection implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3265893938729887984L;
	@Id
	private Integer id;
	@Column(name = "RID")
	private Integer resourceId;
	private Integer creator;
	private Date createDate;
	private Integer del;
}

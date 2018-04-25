package com.cas.sim.tis.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class BrowseHistory implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7428023805019214737L;
	@Id
	private Integer id;
	@Column(name = "RID")
	private Integer resourceId;
	private Integer creator;
	private Date createDate;
}

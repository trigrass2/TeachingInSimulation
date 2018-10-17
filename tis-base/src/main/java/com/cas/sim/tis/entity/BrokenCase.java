package com.cas.sim.tis.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrokenCase implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8103115364848459642L;
	@Id
	private Integer id;
	@Column(name = "TID")
	private Integer typicalId;
//	电路名称
	private String name;
//	电路存档的路径
	private String archivePath;
	private Integer creator;
	private Date createDate;
	private Integer updater;
	private Date updateDate;
	private Boolean del = false;
	private Boolean publish = false;
}
